package com.cmc.recipe.data.model.response

data class MyInfoResponse(
    val code : String,
    val message: String,
    val data : MyInfo,
)

data class MyInfo(
    val memberId : Int,
    val email : String,
    val nickname : String,
    val provider: String
)