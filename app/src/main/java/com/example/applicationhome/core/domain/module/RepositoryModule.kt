package com.example.applicationhome.core.domain.module

import com.example.applicationhome.core.data.repository.AddressesRepositoryImpl
import com.example.applicationhome.core.data.repository.CartRepositoryImpl
import com.example.applicationhome.core.data.repository.FavoriteRepositoryImpl
import com.example.applicationhome.core.data.repository.LocationRepositoryImpl
import com.example.applicationhome.core.data.repository.OrderRepositoryImpl
import com.example.applicationhome.core.data.repository.PaymobRepositoryImpl
import com.example.applicationhome.core.data.repository.ProfileRepositoryImpl
import com.example.applicationhome.core.data.repository.RestaurantRepositoryImpl
import com.example.applicationhome.core.data.repository.ReviewsRepositoryImpl
import com.example.applicationhome.core.data.repository.SearchRepositoryImpl
import com.example.applicationhome.core.data.repository.SupabaseUserRemoteDataSource
import com.example.applicationhome.core.data.repository.SyncAllDataRepositoryImpl
import com.example.applicationhome.core.data.repository.UserRepositoryImpl
import com.example.applicationhome.core.data.repository.WelcomeScreenRepositoryImpl
import com.example.applicationhome.core.domain.repository.AddressesRepository
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.LocationRepository
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.PaymobRepository
import com.example.applicationhome.core.domain.repository.ProfileRepository
import com.example.applicationhome.core.domain.repository.RestaurantRepository
import com.example.applicationhome.core.domain.repository.ReviewsRepository
import com.example.applicationhome.core.domain.repository.SearchRepository
import com.example.applicationhome.core.domain.repository.SupabaseRepository
import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.repository.WelcomeScreenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPaymobRepository(
        paymobRepositoryImpl : PaymobRepositoryImpl
    ): PaymobRepository

    @Binds
    @Singleton
    abstract fun bindSupabaseRepository(
        supabaseUserRemoteDataSource : SupabaseUserRemoteDataSource
    ): SupabaseRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl : CartRepositoryImpl
    ): CartRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl : FavoriteRepositoryImpl
    ): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindSyncAllDataRepository(
        syncAllDataRepositoryImpl : SyncAllDataRepositoryImpl
    ): SyncAllDataRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationRepositoryImpl : LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        orderRepositoryImpl : OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl : ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindRestaurantRepository(
        restaurantRepositoryImpl : RestaurantRepositoryImpl
    ): RestaurantRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImpl : SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl : UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindWelcomeScreenRepository(
        welcomeScreenRepositoryImpl : WelcomeScreenRepositoryImpl
    ): WelcomeScreenRepository

    @Binds
    @Singleton
    abstract fun bindAddressesRepository(
        addressesRepositoryImpl : AddressesRepositoryImpl
    ): AddressesRepository

    @Binds
    @Singleton
    abstract fun bindReviewsRepository(
        reviewsRepositoryImpl : ReviewsRepositoryImpl
    ): ReviewsRepository
}