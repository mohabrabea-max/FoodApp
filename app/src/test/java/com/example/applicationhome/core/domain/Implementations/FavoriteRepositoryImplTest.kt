package com.example.applicationhome.core.domain.Implementations

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import app.cash.turbine.test
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.local.dao.FavoriteDao
import com.example.applicationhome.data.local.entity.UserClass
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteRepositoryImplTest {
    private val dispatcher = UnconfinedTestDispatcher()

    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val favoriteDao = mockk<FavoriteDao>(relaxed = true)
    private val workManager = mockk<WorkManager>(relaxed = true)


    @Test
    fun favoriteMeals_whenUserDataEmitted_returnsMealsFromDao() = runTest {
        val repository = getFavoriteRepository(backgroundScope)

        repository.favoriteMeals.test {
            assertThat(awaitItem()).isEmpty()
            assertThat(awaitItem()).hasSize(3)
        }
    }

    @Test
    fun favoriteSnacks_whenUserDataEmitted_returnsSnacksFromDao() = runTest {
        val repository = getFavoriteRepository(backgroundScope)

        repository.favoriteSnacks.test {
            assertThat(awaitItem()).isEmpty()
            assertThat(awaitItem()).hasSize(2)
        }
    }

    @Test
    fun favoriteRestaurants_whenUserDataEmitted_returnsRestaurantsFromDao() = runTest {
        val repository = getFavoriteRepository(backgroundScope)

       repository.favoriteRestaurantsFromDatabase.test {
           assertThat(awaitItem()).isEmpty()
           assertThat(awaitItem()).hasSize(4)
       }
    }


    @Test
    fun addFoodToFavorite_shouldCallDao() = runTest {
        val favoriteMealEntity = FakesFavoriteEntity.fakesMealsFavoriteEntity().first().favoriteInfo!!

        val repository = getFavoriteRepository(backgroundScope)
        repository.addFoodToFavorite(favoriteMealEntity)

        coVerify(exactly = 1) {
            favoriteDao.addFoodToFavorite(listOf(favoriteMealEntity))
        }
        verify(exactly = 1) { workManager.enqueueUniqueWork(any(), any(), any<OneTimeWorkRequest>()) }
    }

    @Test
    fun addSnackToFavorite_shouldCallDao() = runTest {
        val favoriteSnackEntity = FakesFavoriteEntity.fakesSnacksFavoriteEntity().first().favoriteInfo!!

        val repository = getFavoriteRepository(backgroundScope)
        repository.addSnackToFavorite(favoriteSnackEntity)

        coVerify(exactly = 1) {
            favoriteDao.addSnacksToFavorite(listOf(favoriteSnackEntity))
        }
        verify(exactly = 1) { workManager.enqueueUniqueWork(any(), any(), any<OneTimeWorkRequest>()) }
    }

    @Test
    fun addRestaurantToFavorite_shouldCallDao() = runTest {
        val favoriteRestaurantEntity = FakesFavoriteEntity.fakesRestaurantsFavoriteEntity().first().favoriteInfo!!

        val repository = getFavoriteRepository(backgroundScope)
        repository.addRestaurantToFavorite(favoriteRestaurantEntity)

        coVerify(exactly = 1) {
            favoriteDao.addRestaurantToFavorite(listOf(favoriteRestaurantEntity))
        }
        verify(exactly = 1) { workManager.enqueueUniqueWork(any(), any(), any<OneTimeWorkRequest>()) }
    }


    @Test
    fun deleteFoodFromFavorite_shouldCallDaoAndTriggerWorker() = runTest {
        val mealId = FakesFavoriteEntity.fakesMealsFavoriteEntity().first().meal.id
        val userId = "aaaaa"

        val repository = getFavoriteRepository(backgroundScope)
        repository.deleteFoodFromFavorite(userId, mealId)

        coVerify(exactly = 1) {
            favoriteDao.markFoodAsDeletedOffline(userId, mealId)
        }
        verify(exactly = 1) { workManager.enqueueUniqueWork(any(), any(), any<OneTimeWorkRequest>()) }
    }

    @Test
    fun deleteSnackFromFavorite_shouldCallDaoAndTriggerWorker() = runTest {
        val snackId = FakesFavoriteEntity.fakesSnacksFavoriteEntity().first().snack.id
        val userId = "aaaaa"

        val repository = getFavoriteRepository(backgroundScope)
        repository.deleteSnackFromFavorite(userId, snackId)

        coVerify(exactly = 1) {
            favoriteDao.markSnacksAsDeletedOffline(userId, snackId)
        }
        verify(exactly = 1) { workManager.enqueueUniqueWork(any(), any(), any<OneTimeWorkRequest>()) }
    }

    @Test
    fun deleteRestaurantFromFavorite_shouldCallDaoAndTriggerWorker() = runTest {
        val resId = FakesFavoriteEntity.fakesRestaurantsFavoriteEntity().first().restaurant.id
        val userId = "aaaaa"

        val repository = getFavoriteRepository(backgroundScope)
        repository.deleteRestaurantFromFavorite(userId, resId)

        coVerify(exactly = 1) {
            favoriteDao.markRestaurantsAsDeletedOffline(userId, resId)
        }
        verify(exactly = 1) { workManager.enqueueUniqueWork(any(), any(), any<OneTimeWorkRequest>()) }
    }


    @Test
    fun addGuestFavoriteToUser_shouldCallAllThreeDaoMethods() = runTest {
        val userId = "aaaaa"

        val repository = getFavoriteRepository(backgroundScope)
        repository.addGuestFavoriteToUser(userId)

        coVerify(exactly = 1) { favoriteDao.addGuestMealsFavoriteToUser(userId) }
        coVerify(exactly = 1) { favoriteDao.addGuestSnacksFavoriteToUser(userId) }
        coVerify(exactly = 1) { favoriteDao.addGuestRestaurantsFavoriteToUser(userId) }
    }


    @Test
    fun deleteAllFromFavorite_shouldCallDao() = runTest {
        val repository = getFavoriteRepository(backgroundScope)
        repository.deleteAllFromFavorite()

        coVerify(exactly = 1) { favoriteDao.deleteAllFromFavorite() }
    }


    private fun getFavoriteRepository(testScope: CoroutineScope): FavoriteRepositoryImpl {
        val fakeUser = UserClass(id = "aaaaa")
        val fakeFavoriteMeals = FakesFavoriteEntity.fakesMealsFavoriteEntity()
        val fakeFavoriteSnacks = FakesFavoriteEntity.fakesSnacksFavoriteEntity()
        val fakeFavoriteRestaurants = FakesFavoriteEntity.fakesRestaurantsFavoriteEntity()

        every { userRepository.userData } returns MutableStateFlow(fakeUser)
        every { favoriteDao.getFoodFromDatabase(fakeUser.id) } returns flowOf(fakeFavoriteMeals)
        every { favoriteDao.getSnacksFromDatabase(fakeUser.id) } returns flowOf(fakeFavoriteSnacks)
        every { favoriteDao.getRestaurantsFromDatabase(fakeUser.id) } returns flowOf(fakeFavoriteRestaurants)

        return FavoriteRepositoryImpl(
            userRepository = userRepository,
            favoriteDao = favoriteDao,
            workManager = workManager,
            externalScope = testScope,
            dispatcher = dispatcher
        )
    }
}