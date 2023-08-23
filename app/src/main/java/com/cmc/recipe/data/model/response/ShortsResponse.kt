package com.cmc.recipe.data.model.response

data class ShortsResponse(
    val code: String,
    val data: ShortsData,
    val hasNext: Boolean,
    val message: String,
    val offsetId: Int,
    val pageNumber: Int,
    val pageSize: Int
)

data class ShortsData(
    val content: List<ShortsContent>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: Sort
)

data class ShortsContent(
    val comment_count: Int,
    val cook_time: Int,
    val created_date: String,
    val is_saved: Boolean,
    val nickname: String,
    val rating: Int,
    val recipe_id: Int,
    val recipe_name: String,
    val recipe_thumbnail_img: String
)