package com.cmc.recipe.di

import com.cmc.recipe.data.datasource.*
import com.cmc.recipe.data.source.remote.api.*
import com.cmc.recipe.domain.repository.*
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
    fun providesUserRepository(
        service: UserService
    ) : UserRepository = UserRepositoryImpl(service)

    @Provides
    @ViewModelScoped
    fun providesAuthRepository(
        service: AuthService
    ) : AuthRepository = AuthRepositoryImpl(service)

    @Provides
    @ViewModelScoped
    fun providesRecipeRepository(
        service: RecipeService
    ) : RecipeRepository = RecipeRepositoryImpl(service)

    @Provides
    @ViewModelScoped
    fun providesSearchRepository(
        service: SearchService
    ) : SearchRepository = SearchRepositoryImpl(service)

    @Provides
    @ViewModelScoped
    fun providesUploadRepository(
        service: UploadService
    ) : UploadRepository = UploadRepositoryImpl(service)

    @Provides
    @ViewModelScoped
    fun providesGoogleRepository(
        service: GoogleService
    ) : GoogleLoginRepository = GoogleLoginRepositoryImpl(service)
}