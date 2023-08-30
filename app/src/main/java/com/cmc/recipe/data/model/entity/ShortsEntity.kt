package com.cmc.recipe.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "Shorts")
data class ShortsEntity(
    @PrimaryKey(false)
    val shorts_id: Int,
    @ColumnInfo
    val shortform_name: String,
    @ColumnInfo
    val recipe_thumbnail_img: String,
    @ColumnInfo
    val createdDate: Date,
):Serializable
