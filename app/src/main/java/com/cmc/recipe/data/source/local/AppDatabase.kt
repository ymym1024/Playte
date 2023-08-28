package com.cmc.recipe.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cmc.recipe.data.source.local.dao.RecipeDao
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.entity.SearchEntity
import com.cmc.recipe.data.source.local.dao.SearchDao

@Database(entities = [RecipeEntity::class,SearchEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase :RoomDatabase(){
    abstract fun recipeDao() : RecipeDao

    abstract fun searchDao() : SearchDao
}