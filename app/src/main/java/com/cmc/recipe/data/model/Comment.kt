package com.cmc.recipe.data.model

data class Comment(
    val nickname : String,
    val comment_time : String,
    val comment : String,
    val is_like : Boolean,
    val is_reply : Boolean
)
