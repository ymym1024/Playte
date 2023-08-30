package com.cmc.recipe.data.model.response

data class NicknameResponse(
    val code: String,
    val `data`: NicknameData,
    val message: String
)

data class NicknameData(
    val isDuplicated: Boolean
)