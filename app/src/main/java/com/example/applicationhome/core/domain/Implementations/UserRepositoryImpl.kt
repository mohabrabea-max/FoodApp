package com.example.applicationhome.core.domain.Implementations

import android.util.Log
import com.example.applicationhome.core.domain.repository.SupabaseRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.ChickEmailStates
import com.example.applicationhome.data.data.model.LoginStates
import com.example.applicationhome.data.data.model.UserClassFireBase
import com.example.applicationhome.data.local.dao.FavoriteDao
import com.example.applicationhome.data.local.dao.UsersDao
import com.example.applicationhome.data.local.entity.UserClass
import com.example.applicationhome.data.remote.FoodAppAPIs
import com.example.applicationhome.core.domain.module.ApplicationScope
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UserRepositoryImpl @Inject constructor(
    private val supabaseRepository : SupabaseRepository,
    private val userdao : UsersDao,
    private val favoriteDao: FavoriteDao,
    private val api : FoodAppAPIs,
    @ApplicationScope externalScope : CoroutineScope
): UserRepository {
    private val _isLogin = MutableStateFlow(false)
    override val isLogin : StateFlow<Boolean> = _isLogin

    override val userData : StateFlow<UserClass> =
        userdao.getActiveUser()
            .map { userInDb ->
                userInDb ?: UserClass(firstname = "Guest")
            }.stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UserClass(firstname = "Guest")
            )


    override suspend fun setUserDataToDatabase(emailstate : String): LoginStates {
        return try {
            val formatEmail = "\"$emailstate\""
            val response = api.getUserData(order = "\"email\"", value = formatEmail)
            val userMap = response.body()
            val user = userMap?.values?.firstOrNull()

            if(response.isSuccessful && user != null){

                val userId = userMap.keys.firstOrNull().toString()
                val data = UserClass(
                    userId,
                    user.firstname,
                    user.lastname,
                    user.email,
                    user.phonenumber,
                    user.birthday,
                    user.governorate,
                    user.city,
                    user.address,
                    true
                )
                userdao.addUser(data)
                LoginStates.Success

            } else {
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
                LoginStates.Error(errorMessage)
            }
        } catch (e : Exception){
            LoginStates.Error("خطأ في الشبكة: ${e.message}")
        }
    }

    override suspend fun checkEmailInApi(emailstate : String): ChickEmailStates {
        return try {
            val formatEmail = "\"$emailstate\""
            val response = api.getUserData(order = "\"email\"", value = formatEmail)
            val userMap = response.body()
            if(response.isSuccessful){
                if(userMap.isNullOrEmpty()){
                    ChickEmailStates.EmailIsNotTrue
                }else{
                    ChickEmailStates.Success
                }
            }else{
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
                ChickEmailStates.NetworkError(errorMessage)
            }
        } catch (e : Exception){
            ChickEmailStates.NetworkError("خطأ في الشبكة: ${e.message}")
        }
    }


    override suspend fun logOut(){
        userdao.deleteUserFromDatabase()
    }

    override suspend fun signUp(userId : String, userRequest : UserClassFireBase): Result<Unit> {
        return try {
            val response = api.editeProfile(userId, userRequest)
            if (response.isSuccessful && response.body() != null) {
                val userData = UserClass(
                    userId,
                    userRequest.firstname,
                    userRequest.lastname,
                    userRequest.email,
                    userRequest.phonenumber,
                    userRequest.birthday,
                    userRequest.governorate,
                    userRequest.city,
                    userRequest.address,
                    true
                )
                userdao.addUser(userData)

                Result.success(Unit)
            } else {
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }

                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("خطأ في الشبكة: ${e.message}"))
        }
    }

    override fun login(){
        _isLogin.value = true
    }

    override fun logout(){
        _isLogin.value = false
    }

    override suspend fun validateUserOnAppLaunch(): Boolean {
        return try {
            supabaseRepository.retrieveUser()
            true
        } catch (e : CancellationException){
            throw e
        }catch (e: RestException) {
            if(e.statusCode == 401 || e.statusCode == 403){
                clearLocalData()
                false
            }else{
                true
            }
        } catch(e: Exception) {
            Log.e("ValidateUser", "Network error, continuing offline: ${e.message}")
            true
        }
    }

    override suspend fun observeSessionStatus(){
        supabaseRepository.sessionStatus.collect { status ->
            if(status is SessionStatus.NotAuthenticated){
                clearLocalData()
            }
        }
    }

    private suspend fun clearLocalData(){
        userdao.deleteUserFromDatabase()
        favoriteDao.deleteAllFromFavorite()
    }
}