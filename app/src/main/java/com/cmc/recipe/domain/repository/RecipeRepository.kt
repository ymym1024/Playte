package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.response.RecipesResponse
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getRecipes(accessToken:String) : Flow<NetworkState<RecipesResponse>>

}