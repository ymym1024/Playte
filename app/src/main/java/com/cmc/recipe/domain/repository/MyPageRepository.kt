package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    fun getMyReview() : Flow<NetworkState<ReviewMyResponse>>

    fun deleteReview(id:Int) : Flow<NetworkState<BaseResponse>>

    fun getSaveRecipe() : Flow<NetworkState<SaveWriteRecipeResponse>>

    fun getWrittenRecipe() : Flow<NetworkState<SaveWriteRecipeResponse>>

    fun deleteRecipe(id:Int) : Flow<NetworkState<BaseResponse>>
}