package com.cmc.recipe.data.source.local.dao

import androidx.room.*
import com.cmc.recipe.domain.model.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM Recipe ORDER BY createdDate DESC")
    fun selectAll() : Flow<List<RecipeEntity>>
}