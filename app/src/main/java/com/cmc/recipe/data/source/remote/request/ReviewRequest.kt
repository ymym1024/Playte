package com.cmc.recipe.data.source.remote.request

data class ReviewRequest(
    val review_content: String,
    val review_imgs: List<String>,
    val review_rating: Int
)