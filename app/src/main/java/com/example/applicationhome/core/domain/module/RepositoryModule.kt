package com.example.applicationhome.core.domain.module

import com.example.applicationhome.core.domain.Implementations.PaymobRepositoryImpl
import com.example.applicationhome.core.domain.repository.PaymobRepository
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
}