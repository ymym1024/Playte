package com.cmc.recipe.data.model.response

import com.cmc.recipe.data.model.RecipeItem

data class ReviewResponse(
    val code: String,
    val data: ReviewData,
    val hasNext: Boolean,
    val message: String,
    val offsetId: Int,
    val pageNumber: Int,
    val pageSize: Int
)

data class ReviewData(
    val content: List<ReviewContent>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: Sort
)

data class ReviewContent(
    val liked: Boolean,
    val modified_at: String,
    val rating: Int,
    val review_content: String,
    val review_id: Int,
    val review_images: List<String>,
    val review_title: String,
    val writtenby: String
)