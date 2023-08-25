package com.cmc.recipe.data.model.response

data class IngredientsResponse(
    val code: String,
    val `data`: List<Ingredients>,
    val hasNext: Boolean,
    val message: String,
    val offsetId: Int,
    val pageNumber: Int,
    val pageSize: Int
)

data class Ingredients(
    val ingredient_id: Int,
    val ingredient_name: String,
    val ingredient_type: String,
    val ingredient_unit: String
)
