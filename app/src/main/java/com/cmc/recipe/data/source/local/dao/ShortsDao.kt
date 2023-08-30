package com.cmc.recipe.data.source.local.dao

import androidx.room.*
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.entity.ShortsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortsDao {

    @Query("SELECT * FROM Shorts ORDER BY createdDate DESC")
    fun selectAll() : Flow<List<ShortsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShortsEntity)
}