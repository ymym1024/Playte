package com.cmc.recipe.di

import com.cmc.recipe.data.datasource.AuthRepositoryImpl
import com.cmc.recipe.data.source.remote.api.AuthService
import com.cmc.recipe.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providesAuthRepository(
        contentService: AuthService
    ) : AuthRepository = AuthRepositoryImpl(contentService)
}