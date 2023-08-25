package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.IngredientsResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.data.source.remote.request.UploadRecipeRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UploadService {

    //재료 조회
    @GET("/api/v1/ingredients")
    suspend fun getIngredients() : Response<IngredientsResponse>

    @Multipart
    @POST("/api/v1/file/upload/image")
    suspend fun uploadImage(@Part file: MultipartBody.Part) : Response<BaseResponse>

    @POST("/api/v1/recipes")
    suspend fun uploadRecipe(@Body recipe: UploadRecipeRequest) : Response<BaseResponse>
}