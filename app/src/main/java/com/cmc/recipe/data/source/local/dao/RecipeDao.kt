package com.cmc.recipe.data.source.local.dao

import androidx.room.*
import com.cmc.recipe.data.model.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM Recipe ORDER BY createdDate DESC")
    fun selectAll() : Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RecipeEntity)
}