package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow

interface ShortsRepository {

    fun getRecipesShortform() : Flow<NetworkState<ShortsResponse>>

    fun getRecipesShortformDetail(id:Int) : Flow<NetworkState<ShortsDetailResponse>>

}