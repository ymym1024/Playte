package com.cmc.recipe.data.source.local.dao

import androidx.room.*
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.entity.SearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Query("SELECT * FROM Search ORDER BY createdDate DESC")
    fun selectAll() : Flow<List<SearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SearchEntity)
}