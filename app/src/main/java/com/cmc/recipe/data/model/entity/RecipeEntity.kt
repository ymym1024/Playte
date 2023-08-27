package com.cmc.recipe.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "Recipe")
data class RecipeEntity(
    @PrimaryKey(false)
    val recipe_id: Int,
    @ColumnInfo
    val recipe_name: String,
    @ColumnInfo
    val recipe_thumbnail_img: String,
    @ColumnInfo
    val cook_time:Int,
    @ColumnInfo
    val createdDate: Date,
):Serializable
