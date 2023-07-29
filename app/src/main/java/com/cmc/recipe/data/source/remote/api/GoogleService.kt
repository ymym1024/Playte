package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.GoogleLoginResponse
import com.cmc.recipe.data.source.remote.request.GoogleLoginRequest
import com.cmc.recipe.data.source.remote.request.RequestVerifyNickname
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GoogleService {

    @POST("oauth2/v4/token")
    suspend fun fetchGoogleAuthInfo(@Body request: GoogleLoginRequest) : Response<GoogleLoginResponse>
}