package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.core.domain.exception.AppDomainException
import com.example.applicationhome.core.domain.exception.AuthException
import com.example.applicationhome.core.domain.repository.SupabaseRepository
import com.example.applicationhome.data.data.model.AuthError
import com.example.applicationhome.data.data.model.ErrorsType
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.OTP
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.rpc
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseRepositoryImpl @Inject constructor(
    private val auth : Auth,
    private val postgrest : Postgrest
): SupabaseRepository {
    private fun mapExceptionToAuthError(e: Exception): AuthError {
        return when (e) {
            // 1. أخطاء الشبكة والاتصال (Network Errors)
            is UnknownHostException,
            is IOException -> {
                AuthError.NetworkError
            }

            // 2. أخطاء سوبابيز المحددة عبر RestException
            is RestException -> {
                when (e.error) {
                    "user_already_exists" -> AuthError.EmailAlreadyExists
                    "over_email_send_rate_limit" -> AuthError.TooManyRequests
                    else -> {

                        // لو الكود مش معروف، بنفحص الـ StatusCode
                        when (e.statusCode) {
                            422 -> AuthError.EmailAlreadyExists
                            429 -> AuthError.TooManyRequests
                            else -> AuthError.UnknownError(e.error ?: e.message ?: "")
                        }
                    }
                }
            }

            // 3. أي Exception تاني مش متوقع
            else -> AuthError.UnknownError(e.message ?: "An unexpected error occurred")
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
                Result.failure(AppDomainException(ErrorsType.DATA))
            }
        } catch (e: Exception) {
            val errorType = when (e) {
                // 1. أخطاء انقطاع الشبكة والإنترنت
                is UnknownHostException,
                is IOException -> ErrorsType.NETWORK

                // 2. أخطاء سوبابيز (بيانات غلط، حساب مش موجود، إلخ)
                is RestException -> {
                    when (e.error) {
                        "invalid_credentials",
                        "invalid_grant",
                        "user_not_found" -> ErrorsType.DATA
                        else -> ErrorsType.DATA
                    }
                }

                // 3. احتياطي: فحص نص الرسالة لو سوبابيز رميت Exception عام
                else -> {
                    val message = e.message ?: ""
                    if (message.contains("invalid", ignoreCase = true) || message.contains("credentials", ignoreCase = true)) {
                        ErrorsType.DATA
                    } else {
                        ErrorsType.NETWORK
                    }
                }
            }

            Result.failure(AppDomainException(errorType))
        }
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            auth.updateUser {
                password = newPassword
            }
            Result.success(Unit)
        } catch (e: Exception) {
            if (e is kotlin.coroutines.cancellation.CancellationException) throw e

            val errorType = when (e) {
                is IOException,
                is SocketTimeoutException,
                is io.github.jan.supabase.exceptions.HttpRequestException -> ErrorsType.NETWORK

                // ⚡ أخطاء سوبابيز (RestException)
                is RestException -> {
                    val errorMsg = e.message.orEmpty().lowercase()
                    val errorCode = e.error.orEmpty().lowercase()

                    when {
                        // 🔑 لو الباسورد الجديد هو نفس القديم
                        errorCode == "same_password" ||
                                errorMsg.contains("same") ||
                                errorMsg.contains("different") -> ErrorsType.DATA

                        else -> ErrorsType.UNKNOWNERROR
                    }
                }

                // 🛡️ احتياطي لو اترما Exception عام بنص الرسالة
                else -> {
                    val msg = e.message.orEmpty().lowercase()
                    when {
                        msg.contains("same password") || msg.contains("different") -> ErrorsType.DATA
                        msg.contains("invalid") || msg.contains("credentials") -> ErrorsType.UNKNOWNERROR
                        else -> ErrorsType.NETWORK
                    }
                }
            }

            Result.failure(AppDomainException(errorType))
        }
    }

    override suspend fun deleteUser(): Result<Unit> {
        return runCatching {
            postgrest.rpc("delete_user")
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