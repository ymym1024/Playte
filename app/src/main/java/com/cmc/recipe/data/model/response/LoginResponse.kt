package com.cmc.recipe.data.model.response

data class LoginResponse(
    val code: String,
    val message: String,
    val data : SignupData,
)

data class SignupData(
    val isMember : Boolean,
    val jwtTokens: jwtTokens
)
