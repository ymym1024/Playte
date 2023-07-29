package com.cmc.recipe.data.model.response

data class SignupResponse(
    val code: String,
    val message: String,
    val data : SignupData,
)

data class SignupData(
    val accessToken : String,
    val refreshToken : String
)