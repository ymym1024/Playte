package com.cmc.recipe.data.model.response

data class GoogleLoginResponse(
    var access_token: String = "",
    var expires_in: Int = 0,
    var scope: String = "",
    var token_type: String = "",
    var id_token: String = "",
)