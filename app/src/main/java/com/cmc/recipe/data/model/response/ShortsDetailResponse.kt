package com.cmc.recipe.data.model.response

data class ShortsDetailResponse(
    val code: String,
    val data: ShortsDetailData,
    val message: String
)

data class ShortsDetailData(
    val comments_count: Int,
    val created_date: String,
    val ingredients: List<Product>,
    val is_liked: Boolean,
    val is_saved: Boolean,
    val liked_count: Int,
    val saved_count: Int,
    val shortform_recipe_description: String,
    val shortform_recipe_id: Int,
    val shortform_recipe_name: String,
    val video_url: String,
    val writtenby: String
)

data class Product(
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