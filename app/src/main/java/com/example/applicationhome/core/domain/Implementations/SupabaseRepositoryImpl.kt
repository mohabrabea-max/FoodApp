package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.core.domain.repository.SupabaseRepository
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.providers.builtin.OTP
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseRepositoryImpl @Inject constructor(
    private val auth : Auth
): SupabaseRepository {
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