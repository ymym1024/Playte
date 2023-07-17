package com.cmc.recipe.di

import com.cmc.recipe.data.source.remote.api.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun providesUserService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)
}