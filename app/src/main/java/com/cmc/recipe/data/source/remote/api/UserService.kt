package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.model.response.NicknameResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @POST("/api/v1/users/verify-nickname")
    suspend fun verifyNickname(@Body nickname: RequestNickname) : Response<NicknameResponse>

    @GET("/api/v1/users/me")
    suspend fun getMyInfo() : Response<MyInfoResponse>

    @POST("/api/v1/users/change-nickname")
    suspend fun changeNickname(@Body nickname: RequestNickname) : Response<BaseResponse>


}