package com.cmc.recipe.di

import com.cmc.recipe.data.source.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Singleton
    @Provides
    fun providesRecipeDao(appDatabase: AppDatabase) = appDatabase.recipeDao()

    @Singleton
    @Provides
    fun providesSearchDao(appDatabase: AppDatabase) = appDatabase.searchDao()

    @Singleton
    @Provides
    fun providesShortsDao(appDatabase: AppDatabase) = appDatabase.shortsDao()
}