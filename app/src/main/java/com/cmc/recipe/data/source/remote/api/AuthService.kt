package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.SignupResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("/api/v1/auth/google/signup")
    suspend fun signup(@Header("auth-token") accessToken: String?, @Body nickname: RequestNickname) : Response<SignupResponse>
}