package com.cmc.recipe.data.source.remote.request

data class UploadRecipeRequest(
    val cook_time: Int,
    val ingredients: List<Ingredient>,
    val recipe_description: String,
    val recipe_name: String,
    val recipe_stages: List<RecipeStage>,
    val recipe_thumbnail_img: String,
    val serving_size: Int
)

data class Ingredient(
    val ingredient_id: Int,
    val ingredient_size: Int
)

data class RecipeStage(
    val image_url: String,
    val stage_description: String
)

