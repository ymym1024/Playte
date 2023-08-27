package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.RecipesResponse
import com.cmc.recipe.data.model.response.ReviewMyResponse
import com.cmc.recipe.data.model.response.ReviewResponse
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    fun getMyReview() : Flow<NetworkState<ReviewMyResponse>>

    fun deleteReview(id:Int) : Flow<NetworkState<BaseResponse>>

    fun getSaveRecipe() : Flow<NetworkState<RecipesResponse>>

}