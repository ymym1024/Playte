package com.cmc.recipe.data.model.response

data class ReviewScoreResponse(
    val code: String,
    val data: ScoreData,
    val message: String
)

data class ScoreData(
    val fivePoint: Float,
    val fourPoint: Float,
    val onePoint: Float,
    val threePoint: Float,
    val totalRating: Float,
    val twoPoint: Float
)