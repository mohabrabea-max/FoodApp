package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.exception.AuthException
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.SearchRepository
import com.example.applicationhome.core.domain.repository.SupabaseRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.AuthError
import com.example.applicationhome.data.data.model.UserClassFireBase
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val supabaseRepository: SupabaseRepository,
    private val userRepository: UserRepository,
    private val favoriteRepository : FavoriteRepository,
    private val searchRepository : SearchRepository
){
    private suspend fun saveToFirebaseAndSync(
        userId : String,
        firstName : String,
        lastName : String,
        email : String
    ): Result<Unit> {
        return userRepository.signUp(
            userId,
            UserClassFireBase(
                firstname = firstName,
                lastname = lastName,
                email = email
            )
        ).onSuccess {
            favoriteRepository.addGuestFavoriteToUser(userId)
            searchRepository.addGuestSearchHistoryToUser(userId)
        }
    }

    suspend fun performSignUp(
        firstName : String,
        lastName : String,
        email : String,
        password : String
    ): Result<Unit> {
        val supabaseResult = supabaseRepository.signUp(email, password)

        val userId = supabaseResult.getOrElse { error ->
            return Result.failure(error)
        }

        val firebaseResult = saveToFirebaseAndSync(userId, firstName, lastName, email)

        if(firebaseResult.isFailure){
            supabaseRepository.deleteUser()

            val firebaseException = firebaseResult.exceptionOrNull()

            return Result.failure(
                AuthException(
                    AuthError.UnknownError("Failed to save user data: ${firebaseException?.message}")
                )
            )
        }

        return Result.success(Unit)
    }
}