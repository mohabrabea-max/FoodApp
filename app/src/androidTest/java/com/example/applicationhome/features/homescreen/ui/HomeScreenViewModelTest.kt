package com.example.applicationhome.features.homescreen.ui

import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.FavoriteUseCase
import com.example.applicationhome.data.local.entity.CategoriesEntity
import com.example.applicationhome.data.remote.NetworkObserver
import com.example.applicationhome.fakes.FakeRestaurants
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    private val syncAllDataRepository = mockk<SyncAllDataRepository>(relaxed = true)
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val getFavoriteUseCase = mockk<FavoriteUseCase>(relaxed = true)

    @Before
    fun setUp(){
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun addRestaurantsFavorite_shouldCallUseCase() = runTest{
        val favoriteRestaurantEntity = FakeRestaurants.favoriteRestaurantEntityFakes().first()

        val viewModel = getViewModel()

        viewModel.addRestaurantsFavorite(favoriteRestaurantEntity)

        advanceUntilIdle() // أمر بالانتظار وتفريغ طابور المهام قبل الـ coVerify

        coVerify(exactly = 1) {
            getFavoriteUseCase.addRestaurantsFavorite(favoriteRestaurantEntity)
        }
    }

    @Test
    fun select_Category_shouldUpdateSelectedCategoryState() = runTest {
        val favoriteRestaurantEntity = FakeRestaurants.favoriteRestaurantEntityFakes().first()
        val restaurantsEntityFakes = FakeRestaurants.restaurantsEntityFakes().find { it.id == favoriteRestaurantEntity.resId }

        val viewModel = getViewModel()
        val category = CategoriesEntity(
                name = restaurantsEntityFakes?.typ?.first()?.title ?: "",
                type = restaurantsEntityFakes?.typ?.first()?.title ?: ""
            )

        viewModel.select(category)

        advanceUntilIdle()

        assertEquals(category.type, viewModel.typ.value)
    }

    @Test
    fun unSelect_Category_shouldUpdateSelectedCategoryState() = runTest {
        val viewModel = getViewModel()

        viewModel.unSelected()

        advanceUntilIdle()

        assertEquals("All", viewModel.typ.value)
    }


    private fun getViewModel(): HomeScreenViewModel {
        val networkObserver = mockk<NetworkObserver>(relaxed = true)

        return HomeScreenViewModel(
            syncAllDataRepository,
            userRepository,
            getFavoriteUseCase,
            networkObserver,
            dispatcher
        )
    }
}