package com.example.applicationhome.core.domain.module

import com.example.applicationhome.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASEURL,
            supabaseKey = BuildConfig.SUPABASEAPIKEY
        ) {
            install(Auth){
                alwaysAutoRefresh = true
                autoLoadFromStorage = true
            }

            install(Postgrest)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(supabaseClient: SupabaseClient): Auth {
        return supabaseClient.auth
    }

    @Provides
    @Singleton
    fun provideSupabasePostgrest(supabaseClient: SupabaseClient): Postgrest {
        return supabaseClient.postgrest
    }
}
