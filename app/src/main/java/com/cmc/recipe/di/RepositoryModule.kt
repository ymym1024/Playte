package com.cmc.recipe.di

import com.cmc.recipe.data.datasource.*
import com.cmc.recipe.data.source.local.dao.RecipeDao
import com.cmc.recipe.data.source.local.dao.SearchDao
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
    fun providesMyPageRepository(
        service: MyPageService
    ) : MyPageRepository = MyPageRepositoryImpl(service)

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
        service: RecipeService,
        dao : RecipeDao
    ) : RecipeRepository = RecipeRepositoryImpl(service,dao)

    @Provides
    @ViewModelScoped
    fun providesSearchRepository(
        service: SearchService,
        dao:SearchDao
    ) : SearchRepository = SearchRepositoryImpl(service,dao)

    @Provides
    @ViewModelScoped
    fun providesUploadRepository(
        service: UploadService
    ) : UploadRepository = UploadRepositoryImpl(service)

    @Provides
    @ViewModelScoped
    fun providesShortsRepository(
        service: ShortsService
    ) : ShortsRepository = ShortsRepositoryImpl(service)

    @Provides
    @ViewModelScoped
    fun providesGoogleRepository(
        service: GoogleService
    ) : GoogleLoginRepository = GoogleLoginRepositoryImpl(service)
}