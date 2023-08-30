package com.cmc.recipe.data.model

import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.entity.ShortsEntity
import com.cmc.recipe.data.model.response.RecommendationRecipe
import com.cmc.recipe.data.model.response.ShortsContent
import java.util.*

object ShortsMapper {
    fun ShortsContent.toEntity() = ShortsEntity(
        shorts_id = shortform_id?:0,
        shortform_name = shortform_name,
        recipe_thumbnail_img = video_url,
        createdDate = Date()
    )
}