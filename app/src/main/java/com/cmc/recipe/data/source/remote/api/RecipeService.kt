package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.RecipeDetailResponse
import com.cmc.recipe.data.model.response.RecipesResponse
import com.cmc.recipe.data.model.response.SignupResponse
import retrofit2.Response
import retrofit2.http.*

interface RecipeService {

    @GET("/api/v1/recipes")
    suspend fun getRecipes() : Response<RecipesResponse>

    @GET("/api/v1/recipes/{recipe-id}/detail")
    suspend fun getRecipesDetail(@Path("recipe-id")id:Int) : Response<RecipeDetailResponse>

    @POST("/api/v1/recipes/{recipe-id}/save")
    suspend fun postRecipesSave(@Path("recipe-id")id:Int) : Response<BaseResponse>

    @POST("/api/v1/recipes/{recipe-id}/unsave")
    suspend fun postRecipesNotSave(@Path("recipe-id")id:Int) : Response<BaseResponse>


}