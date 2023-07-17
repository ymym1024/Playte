package com.cmc.recipe.data.model.response

data class BaseResponse(
    val code: String,
    val message: String,
    val data : Any,
)