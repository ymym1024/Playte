package com.cmc.recipe.data.model.response

import com.cmc.recipe.data.model.Notice

data class NoticeResponse(
    val code: String,
    val message: String,
    val data : List<Notice>,
)