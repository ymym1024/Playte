package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.ReviewMyResponse
import com.cmc.recipe.data.model.response.ReviewResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MyPageService {

    @GET("/api/v1/reviews/my")
    suspend fun getMyReview() : Response<ReviewMyResponse>

    @DELETE("/api/v1/reviews/{review-id}")
    suspend fun deleteReview(@Path("review-id")id:Int) : Response<BaseResponse>


}