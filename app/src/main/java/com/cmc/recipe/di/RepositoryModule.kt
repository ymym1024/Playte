package com.cmc.recipe.di

import com.cmc.recipe.data.datasource.GoogleLoginRepositoryImpl
import com.cmc.recipe.data.datasource.UserRepositoryImpl
import com.cmc.recipe.data.source.remote.api.GoogleService
import com.cmc.recipe.data.source.remote.api.UserService
import com.cmc.recipe.domain.repository.GoogleLoginRepository
import com.cmc.recipe.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providesAuthRepository(
        contentService: UserService
    ) : UserRepository = UserRepositoryImpl(contentService)

    @Provides
    @ViewModelScoped
    fun providesGoogleRepository(
        service: GoogleService
    ) : GoogleLoginRepository = GoogleLoginRepositoryImpl(service)
}