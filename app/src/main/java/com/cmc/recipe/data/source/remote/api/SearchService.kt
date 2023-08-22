package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.RecipeDetailResponse
import com.cmc.recipe.data.model.response.RecipesResponse
import com.cmc.recipe.data.model.response.SignupResponse
import retrofit2.Response
import retrofit2.http.*

interface SearchService {

    @GET("/api/v1/search/recipe")
    suspend fun getSearchRecipe(@Query("keyword") keyword:String) : Response<RecipesResponse>

}