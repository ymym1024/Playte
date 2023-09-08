package com.cmc.recipe.data.source.remote.request

data class UploadShortsRequest(
    val description: String,
    val ingredients_ids: List<Int>,
    val shortform_name: String,
    val video_time:String,
    val video_url: String
)
