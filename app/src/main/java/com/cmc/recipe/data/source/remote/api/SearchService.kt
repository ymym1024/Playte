package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface SearchService {

    @GET("/api/v1/search/recipe")
    suspend fun getSearchRecipe(@Query("keyword") keyword:String) : Response<RecipesResponse>

    @GET("/api/v1/search/shortform")
    suspend fun getSearchShortform(@Query("keyword") keyword:String) : Response<ShortsResponse>

}