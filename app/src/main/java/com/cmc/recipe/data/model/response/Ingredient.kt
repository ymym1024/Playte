package com.cmc.recipe.data.model.response

data class Ingredient(
    val coupang_product_image: String,
    val coupang_product_name: String,
    val coupang_product_price: Int,
    val coupang_product_url: String,
    val ingredient_id: Int,
    val ingredient_name: String,
    val ingredient_size: Any,
    val ingredient_type: String,
    val ingredient_unit: String,
    val is_rocket_delivery: Boolean
)