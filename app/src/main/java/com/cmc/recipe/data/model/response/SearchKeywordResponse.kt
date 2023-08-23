package com.cmc.recipe.data.model.response

data class SearchKeywordResponse(
    val code: String,
    val message: String,
    val data : List<String>,
)
