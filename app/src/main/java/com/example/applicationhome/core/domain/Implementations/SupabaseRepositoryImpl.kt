package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.core.domain.repository.SupabaseRepository
import com.example.applicationhome.data.data.model.AuthError
import com.example.applicationhome.data.data.model.ErrorsType
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.OTP
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseRepositoryImpl @Inject constructor(
    private val auth : Auth
): SupabaseRepository {
    private fun mapExceptionToAuthError(e: Exception): AuthError {
        val message = e.message ?: ""

        return when {
            e is java.net.UnknownHostException ||
                    e is java.io.IOException ||
                    message.contains("Unable to resolve host", ignoreCase = true) ||
                    message.contains("Failed to connect", ignoreCase = true) -> {
                AuthError.NetworkError
            }

            message.contains("User already registered", ignoreCase = true) ||
                    message.contains("already exists", ignoreCase = true) -> {
                AuthError.EmailAlreadyExists
            }

            else -> AuthError.UnknownError(message)
        }
    }

    override suspend fun signUp(email: String, pass: String): Result<String> {
        return try {
            auth.signUpWith(Email){
                this.email = email
                this.password = pass
            }

            val userId = auth.currentUserOrNull()?.id

            if(userId != null){
                Result.success(userId)
            }else{
                Result.failure(Exception(ErrorsType.DATA.toString()))
            }
        } catch (e: Exception) {
            val authError = mapExceptionToAuthError(e)
            Result.failure(AuthException(authError))
        }
    }

    class AuthException(val error: AuthError) : Exception()


    override suspend fun login(email: String, pass: String): Result<String> {
        return try {
            auth.signInWith(Email){
                this.email = email
                this.password = pass
            }

            val userId = auth.currentUserOrNull()?.id

            if(userId != null){
                Result.success(userId)
            }else{
                Result.failure(Exception(ErrorsType.DATA.toString()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            auth.updateUser {
                password = newPassword
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUserOrNull()?.id
    }


    override suspend fun sendOtp(email: String): Result<Unit> {
        return try {
            auth.signInWith(OTP) {
                this.email = email
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(email: String, code: String): Result<Boolean> {
        return try {
            auth.verifyEmailOtp(
                type = OtpType.Email.EMAIL,
                email = email,
                token = code
            )
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}