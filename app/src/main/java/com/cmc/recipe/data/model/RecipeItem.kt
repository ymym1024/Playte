package com.cmc.recipe.data.model

data class RecipeItem(
    val image_url : String,
    val name : String,
    val time : Int,
    val nickName:String,
    val star : Int,
    val flag : Boolean = true
)
