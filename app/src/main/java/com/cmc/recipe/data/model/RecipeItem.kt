package com.cmc.recipe.data.model

data class RecipeItem(
    val comment_count: Int,
    val created_date: String,
    val is_saved: Boolean,
    val nickname: String,
    val rating: Int,
    val recipe_id: Int,
    val recipe_name: String,
    val recipe_thumbnail_img: String
)
