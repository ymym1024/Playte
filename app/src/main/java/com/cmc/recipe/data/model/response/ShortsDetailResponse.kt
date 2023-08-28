package com.cmc.recipe.data.model.response

data class ShortsDetailResponse(
    val code: String,
    val data: ShortsDetailData,
    val message: String
)

data class ShortsDetailData(
    val comments_count: Int,
    val created_date: String,
    val ingredients: List<Ingredient>,
    val is_liked: Boolean,
    val is_saved: Boolean,
    val liked_count: Int,
    val saved_count: Int,
    val shortform_recipe_description: String,
    val shortform_recipe_id: Int,
    val shortform_recipe_name: String,
    val video_url: String,
    val writtenby: String
)
