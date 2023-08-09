package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/api/v1/users/verify-nickname")
    suspend fun verifyNickname(@Body nickname: RequestNickname) : Response<BaseResponse>

    @POST("/api/v1/users/me")
    suspend fun getMyInfo(@Body nickname: RequestNickname) : Response<MyInfoResponse>


}