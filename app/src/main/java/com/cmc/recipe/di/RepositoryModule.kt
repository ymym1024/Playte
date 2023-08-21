package com.cmc.recipe.di

import com.cmc.recipe.data.datasource.AuthRepositoryImpl
import com.cmc.recipe.data.datasource.GoogleLoginRepositoryImpl
import com.cmc.recipe.data.datasource.RecipeRepositoryImpl
import com.cmc.recipe.data.datasource.UserRepositoryImpl
import com.cmc.recipe.data.source.remote.api.AuthService
import com.cmc.recipe.data.source.remote.api.GoogleService
import com.cmc.recipe.data.source.remote.api.RecipeService
import com.cmc.recipe.data.source.remote.api.UserService
import com.cmc.recipe.domain.repository.AuthRepository
import com.cmc.recipe.domain.repository.GoogleLoginRepository
import com.cmc.recipe.domain.repository.RecipeRepository
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
    fun providesGoogleRepository(
        service: GoogleService
    ) : GoogleLoginRepository = GoogleLoginRepositoryImpl(service)
}