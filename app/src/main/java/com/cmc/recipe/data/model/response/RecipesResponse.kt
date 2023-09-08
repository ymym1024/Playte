package com.cmc.recipe.data.model.response

import com.cmc.recipe.data.model.RecipeItem

data class RecipesResponse(
    val code: String,
    val data: RecipeData,
    val hasNext: Boolean,
    val message: String,
    val offsetId: Int,
    val pageNumber: Int,
    val pageSize: Int
)

data class RecipeData(
    val content: List<RecipeItem>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: Sort
)
data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: Sort,
    val unpaged: Boolean
)

data class Sort(
    val empty:Boolean,
    val sorted:Boolean,
    val unsorted:Boolean
)
