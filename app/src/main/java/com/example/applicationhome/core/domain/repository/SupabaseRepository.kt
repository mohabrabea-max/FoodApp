package com.example.applicationhome.core.domain.repository

interface SupabaseRepository {
    suspend fun signUp(email: String, pass: String): Result<String>
    suspend fun login(email: String, pass: String): Result<String>
    suspend fun updatePassword(newPassword: String): Result<Unit>
    fun getCurrentUserId(): String?

    suspend fun sendOtp(email : String): Result<Unit>
    suspend fun verifyOtp(email : String, code : String): Result<Boolean>
}