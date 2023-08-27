package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.ReviewMyResponse
import com.cmc.recipe.data.model.response.ReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface MyPageService {

    @GET("/api/v1/reviews/my")
    suspend fun getMyReview() : Response<ReviewMyResponse>


}