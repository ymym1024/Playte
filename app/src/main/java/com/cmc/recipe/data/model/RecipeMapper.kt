package com.cmc.recipe.data.model

import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.response.RecommendationRecipe
import java.util.*

object RecipeMapper {
    fun RecipeItem.toEntity() = RecipeEntity(
        recipe_id = recipe_id?:0,
        recipe_name = recipe_name,
        recipe_thumbnail_img = recipe_thumbnail_img,
        cook_time = cook_time,
        createdDate = Date()
    )

    fun RecipeEntity.toRecipe() = RecommendationRecipe(
        recipe_id = recipe_id,
        recipe_name = recipe_name,
        img_url = recipe_thumbnail_img,
        cooking_time = cook_time
    )
}