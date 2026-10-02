package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.exception.AuthException
import com.example.applicationhome.core.domain.model.AuthError
import com.example.applicationhome.core.domain.model.UserClassFireBase
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.SearchRepository
import com.example.applicationhome.core.domain.repository.SupabaseRepository
import com.example.applicationhome.core.domain.repository.UserRepository
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
        val result = userRepository.signUp(
            userId,
            UserClassFireBase(
                firstname = firstName,
                lastname = lastName,
                email = email
            )
        )

        result.onSuccess {
            favoriteRepository.addGuestFavoriteToUser(userId)
            searchRepository.addGuestSearchHistoryToUser(userId)
        }.onFailure { error ->
            return Result.failure(error)
        }

        return Result.success(Unit)
    }

    suspend fun performSignUp(
        firstName : String,
        lastName : String,
        email : String,
        password : String
    ): Result<Unit> {
        val userId = supabaseRepository.signUp(email, password).getOrElse { error ->
            return Result.failure(error)
        }

        return saveToFirebaseAndSync(userId, firstName, lastName, email)
            .recoverCatching { firebaseError ->
                runCatching { supabaseRepository.deleteUser() }
                throw AuthException(
                        AuthError.UnknownError("Failed to save user data: ${firebaseError.message}")
                    )

            }
    }
}