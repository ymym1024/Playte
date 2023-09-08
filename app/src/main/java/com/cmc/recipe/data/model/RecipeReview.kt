package com.cmc.recipe.data.model

data class RecipeReview(
    val nick : String,
    val date : String,
    val content : String,
    val stars : Int,
    val thumb_up:Int,
    val thumb_down: Int,
    val image_list : List<String>?,
)
