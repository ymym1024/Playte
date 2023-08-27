package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.LoginResponse
import com.cmc.recipe.data.model.response.SignupResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("/api/v1/auth/google/signup")
    suspend fun signup(@Header("auth-token") accessToken: String?, @Body nickname: RequestNickname) : Response<SignupResponse>

    @POST("/api/v1/auth/google/signin")
    suspend fun login(@Header("auth-token") accessToken: String?) : Response<LoginResponse>

    @POST("/api/v1/auth/logout")
    suspend fun logout(@Header("Refresh-Token") refreshToken: String?) : Response<BaseResponse>

    @POST("/api/v1/auth/withdrawal")
    suspend fun withdrawal() : Response<BaseResponse>

    @POST("/api/v1/auth/refresh")
    suspend fun refreshToken(@Header("Refresh-Token") refreshToken: String?) : Response<SignupResponse>

}