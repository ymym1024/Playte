package com.cmc.recipe.data.model.response
import com.cmc.recipe.data.model.RecipeItem

data class SaveWriteRecipeResponse(
    val code: String,
    val data: List<RecipeItem>,
    val message: String
)