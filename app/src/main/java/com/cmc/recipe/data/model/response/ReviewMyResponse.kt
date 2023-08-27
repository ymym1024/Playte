package com.cmc.recipe.data.model.response

data class ReviewMyResponse(
    val code: String,
    val data: List<ReviewMyData>,
    val message: String
)

data class ReviewMyData(
    val review_id:Int,
    val img_list: List<String>,
    val like_count: Int,
    val recipe_name: String,
    val review_content: String,
    val review_rating: Int,
    val written_date: String
)