package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.data.source.remote.request.UploadShortsRequest
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

    @GET("/api/v1/recipes/shortform")
    suspend fun getRecipesShortform() : Response<ShortsResponse>

    @GET("/api/v1/recipes/shortform/detail/{shortform-id}")
    suspend fun getRecipesShortformDetail(@Path("shortform-id")id:Int) : Response<ShortsDetailResponse>

    //레시피 리뷰
    @GET("/api/v1/reviews/recipe/{recipe-id}")
    suspend fun getRecipesReview(@Path("recipe-id")id:Int) : Response<ReviewResponse>

    @GET("/api/v1/reviews/photos/recipe/{recipe-id}")
    suspend fun getRecipesReviewPhotos(@Path("recipe-id")id:Int) : Response<PhotoResponse>

    @GET("/api/v1/reviews/recipe/{recipe-id}/scores")
    suspend fun getRecipesReviewScores(@Path("recipe-id")id:Int) : Response<ReviewScoreResponse>

    @POST("/api/v1/reviews/recipe/{recipe-id}")
    suspend fun postRecipesReview(@Path("recipe-id")id:Int, @Body recipe: ReviewRequest) : Response<BaseResponse>

    @POST("/api/v1/reviews/{review-id}/like")
    suspend fun updateReviewLike(@Path("review-id")id:Int) : Response<BaseResponse>

    @POST("/api/v1/reviews/{review-id}/unlike")
    suspend fun updateReviewUnLike(@Path("review-id")id:Int) : Response<BaseResponse>

    @POST("/api/v1/reviews/{review-id}/report")
    suspend fun postReviewReport(@Path("review-id")id:Int) : Response<BaseResponse>


}