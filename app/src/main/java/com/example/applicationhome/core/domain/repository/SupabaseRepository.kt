package com.example.applicationhome.core.domain.repository

interface SupabaseRepository {
    suspend fun sendOtp(email : String): Result<Unit>
    suspend fun verifyOtp(email : String, code : String): Result<Boolean>
}