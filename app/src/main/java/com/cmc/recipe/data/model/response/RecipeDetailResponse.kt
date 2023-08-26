package com.cmc.recipe.data.model.response

data class RecipeDetailResponse(
    val code: String,
    val data: RecipeDetail,
    val hasNext: Boolean,
    val message: String,
    val offsetId: Int,
    val pageNumber: Int,
    val pageSize: Int
)

data class RecipeIngredient(
    val coupang_product_image: String,
    val coupang_product_name: String,
    val coupang_product_price: Int,
    val coupang_product_url: String,
    val ingredient_id: Int,
    val ingredient_name: String,
    val ingredient_size: Int,
    val ingredient_type: String,
    val ingredient_unit: String,
    val is_rocket_delivery: Boolean
)

data class RecipeDetail(
    val cook_time: Int,
    val created_date: String,
    val ingredients: List<RecipeIngredient>,
    val is_saved: Boolean,
    val rating: Int,
    val recipe_description: String,
    val recipe_id: Int,
    val recipe_name: String,
    val recipe_thumbnail_img: String,
    val recommendation_recipes: List<RecommendationRecipe>,
    val serving_size: Int,
    val stages: List<Stage>,
    val writtenby: String
)

data class RecommendationRecipe(
    val cooking_time: Int,
    val img_url: String,
    val recipe_name: String
)

data class Stage(
    val stage_description: String,
    val stage_image_url: String
)