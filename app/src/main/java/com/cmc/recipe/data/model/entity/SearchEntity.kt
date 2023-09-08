package com.cmc.recipe.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "Search")
data class SearchEntity(
    @PrimaryKey (autoGenerate = true)
    val search_id: Int,
    @ColumnInfo
    val keyword : String,
    @ColumnInfo
    val createdDate: Date,
):Serializable
