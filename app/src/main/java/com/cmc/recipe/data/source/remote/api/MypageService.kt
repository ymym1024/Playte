package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.model.response.ReviewResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MypageService {

    @POST("/api/v1/reviews/my")
    suspend fun getMyReview(@Body nickname: RequestNickname) : Response<ReviewResponse>


}