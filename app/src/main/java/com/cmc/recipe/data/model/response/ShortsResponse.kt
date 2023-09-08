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
    var comments_count: Int,
    val created_date: String,
    val ingredients: List<Ingredient>,
    var is_liked: Boolean,
    var is_saved: Boolean,
    var likes_count: Int,
    var saved_count: Int,
    val shortform_description: String,
    val shortform_id: Int,
    val shortform_name: String,
    val video_time: String,
    val video_url: String,
    val writtenBy: String
):java.io.Serializable
