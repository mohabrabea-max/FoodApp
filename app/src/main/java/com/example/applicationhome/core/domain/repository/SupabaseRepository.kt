package com.example.applicationhome.core.domain.repository

import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.StateFlow

interface SupabaseRepository {
    suspend fun signUp(email: String, pass: String): Result<String>
    suspend fun login(email: String, pass: String): Result<String>
    suspend fun updatePassword(newPassword: String): Result<Unit>
    suspend fun deleteUser(): Result<Unit>
    fun getCurrentUserId(): String?

    suspend fun retrieveUser(): UserInfo
    val sessionStatus : StateFlow<SessionStatus>

    suspend fun sendOtp(email : String): Result<Unit>
    suspend fun verifyOtp(email : String, code : String): Result<Boolean>
}