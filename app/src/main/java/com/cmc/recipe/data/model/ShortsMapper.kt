package com.cmc.recipe.data.model

import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.entity.ShortsEntity
import com.cmc.recipe.data.model.response.RecommendationRecipe
import com.cmc.recipe.data.model.response.ShortsContent
import com.cmc.recipe.data.model.response.ShortsDetailData
import java.util.*

object ShortsMapper {
    fun ShortsContent.toEntity() = ShortsEntity(
        shorts_id = shortform_id?:0,
        shortform_name = shortform_name,
        recipe_thumbnail_img = video_url,
        createdDate = Date()
    )

    fun ShortsDetailData.toContent() = ShortsContent(
        comments_count = comments_count,
        created_date = created_date,
        ingredients = ingredients,
        is_liked = is_liked,
        is_saved = is_saved,
        likes_count = liked_count,
        saved_count = saved_count,
        shortform_description = shortform_recipe_description,
        shortform_id = shortform_recipe_id,
        shortform_name = shortform_recipe_name,
        video_time = "",
        video_url = video_url,
        writtenBy = writtenby
    )
}