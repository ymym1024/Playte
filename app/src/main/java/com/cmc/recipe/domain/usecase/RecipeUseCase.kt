package com.cmc.recipe.domain.usecase

import android.util.Log
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.domain.repository.AuthRepository
import com.cmc.recipe.domain.repository.RecipeRepository
import com.cmc.recipe.domain.repository.UserRepository
import javax.inject.Inject

class RecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
    ) {
    fun getRecipes() = repository.getRecipes()

    fun getRecipesDetail(id:Int) = repository.getRecipesDetail(id)

    fun postRecipesSave(id:Int) = repository.postRecipesSave(id)

    fun postRecipesNotSave(id:Int) = repository.postRecipesNotSave(id)

    // 리뷰
    fun getRecipesReview(id:Int) = repository.getRecipesReview(id)

    fun getRecipesReviewPhotos(id:Int) = repository.getRecipesReviewPhotos(id)

    fun getRecipesReviewScores(id:Int) = repository.getRecipesReviewScores(id)

    fun postRecipesReview(request:ReviewRequest) = repository.postRecipesReview(request)
}