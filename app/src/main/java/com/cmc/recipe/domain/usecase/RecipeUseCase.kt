package com.cmc.recipe.domain.usecase

import android.util.Log
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.RecipesResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.domain.repository.AuthRepository
import com.cmc.recipe.domain.repository.RecipeRepository
import com.cmc.recipe.domain.repository.UserRepository
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
    ) {
    fun getRecipes() = repository.getRecipes()

    fun getRecipesDetail(id:Int) = repository.getRecipesDetail(id)

    fun postRecipesSave(id:Int) = repository.postRecipesSave(id)

    fun postRecipesNotSave(id:Int) = repository.postRecipesNotSave(id)

    fun postRecipesLike(id:Int) = repository.postRecipesLike(id)

    fun postRecipesUnLike(id:Int) = repository.postRecipesUnLike(id)

    fun getRecipesTheme(themeName:String)  = repository.getRecipesTheme(themeName)

    fun postRecipeReport(id:Int)  = repository.postRecipeReport(id)

    // 리뷰
    fun getRecipesReview(id:Int) = repository.getRecipesReview(id)

    fun getRecipesReviewPhotos(id:Int) = repository.getRecipesReviewPhotos(id)

    fun getRecipesReviewScores(id:Int) = repository.getRecipesReviewScores(id)

    fun postRecipesReview(id:Int,request:ReviewRequest) = repository.postRecipesReview(id,request)

    fun updateReviewLike(id:Int) = repository.updateReviewLike(id)

    fun updateReviewUnLike(id:Int) = repository.updateReviewUnLike(id)

    fun postReviewReport(id:Int) = repository.postReviewReport(id)

    //최근 본 레시피
    suspend fun insertRecentRecipe(item:RecipeItem) = repository.insertRecentRecipe(item)

    fun loadRecentRecipes() = repository.loadRecentRecipes()

}