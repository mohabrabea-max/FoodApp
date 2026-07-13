package com.example.applicationhome.domain

import com.example.applicationhome.data.data.repository.FavoriteRepository
import com.example.applicationhome.data.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository : FavoriteRepository,
    @ApplicationScope private val externalScope: CoroutineScope
){

}