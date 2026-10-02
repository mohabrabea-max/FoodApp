package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.SupabaseRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val supabaseRepository : SupabaseRepository,
    private val favoriteRepository : FavoriteRepository,
    private val userRepository : UserRepository
){
    suspend operator fun invoke(email : String, password : String): Result<Unit>{
        supabaseRepository.login(email, password)
            .onFailure { error ->
                return Result.failure(error)
            }

        supabaseRepository.deleteUser().onFailure { error ->
                return Result.failure(error)
            }

        favoriteRepository.deleteAllFromFavorite()
        userRepository.logOut()

        return Result.success(Unit)
    }
}