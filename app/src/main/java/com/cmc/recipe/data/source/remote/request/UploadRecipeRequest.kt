package com.cmc.recipe.data.source.remote.request

import com.cmc.recipe.data.model.RecipeStep

data class UploadRecipeRequest(
    val cook_time: Int,
    val ingredients: List<Ingredient>,
    val recipe_description: String,
    val recipe_name: String,
    val recipe_stages: List<RecipeStep>,
    val recipe_thumbnail_img: String,
    val serving_size: Int
)

data class Ingredient(
    val ingredient_id: Int,
    val ingredient_size: Int
)

