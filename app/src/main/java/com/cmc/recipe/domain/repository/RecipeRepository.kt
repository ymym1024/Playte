package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getRecipes() : Flow<NetworkState<RecipesResponse>>

    fun getRecipesDetail(id:Int) : Flow<NetworkState<RecipeDetailResponse>>

    fun postRecipesSave(id:Int) : Flow<NetworkState<BaseResponse>>

    fun postRecipesNotSave(id:Int) : Flow<NetworkState<BaseResponse>>

    fun getRecipesShortform() : Flow<NetworkState<ShortsResponse>>

    fun getRecipesShortformDetail(id:Int) : Flow<NetworkState<ShortsDetailResponse>>

    //리뷰
    fun getRecipesReview(id:Int) : Flow<NetworkState<ReviewResponse>>

    fun getRecipesReviewPhotos(id:Int) : Flow<NetworkState<PhotoResponse>>

    fun getRecipesReviewScores(id:Int) : Flow<NetworkState<ReviewScoreResponse>>

    fun postRecipesReview(id:Int,request: ReviewRequest) : Flow<NetworkState<BaseResponse>>

    fun updateReviewLike(id:Int) : Flow<NetworkState<BaseResponse>>

    fun updateReviewUnLike(id:Int) : Flow<NetworkState<BaseResponse>>

    fun postReviewReport(id:Int) : Flow<NetworkState<BaseResponse>>

    // 최근 본 레시피 저장
    suspend fun insertRecentRecipe(item: RecipeItem): Boolean

    fun loadRecentRecipes() : Flow<List<RecipeEntity>>


}