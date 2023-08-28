package com.cmc.recipe.data.model.response

data class PhotoResponse(
    val code: String,
    val message: String,
    val data : List<String>,
)