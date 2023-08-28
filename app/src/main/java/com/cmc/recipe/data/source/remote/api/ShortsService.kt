package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.data.source.remote.request.UploadShortsRequest
import retrofit2.Response
import retrofit2.http.*

interface ShortsService {

    @GET("/api/v1/recipes/shortform")
    suspend fun getRecipesShortform() : Response<ShortsResponse>

    @GET("/api/v1/recipes/shortform/detail/{shortform-id}")
    suspend fun getRecipesShortformDetail(@Path("shortform-id")id:Int) : Response<ShortsDetailResponse>

}