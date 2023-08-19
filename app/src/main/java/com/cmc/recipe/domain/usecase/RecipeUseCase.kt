package com.cmc.recipe.domain.usecase

import android.util.Log
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.AuthRepository
import com.cmc.recipe.domain.repository.RecipeRepository
import com.cmc.recipe.domain.repository.UserRepository
import javax.inject.Inject

class RecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
    ) {
    fun getRecipes(accessToken:String) = repository.getRecipes(accessToken)

    fun getRecipesDetail(accessToken:String,id:Int) = repository.getRecipesDetail(accessToken,id)

    fun postRecipesSave(accessToken:String,id:Int) = repository.postRecipesSave(accessToken,id)
}