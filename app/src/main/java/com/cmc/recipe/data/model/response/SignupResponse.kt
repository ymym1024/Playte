package com.cmc.recipe.data.model.response

data class SignupResponse(
    val code: String,
    val message: String,
    val data : jwtTokens,
)

data class jwtTokens(
    val accessToken : String,
    val refreshToken : String
)