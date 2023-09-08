package com.cmc.recipe.presentation.ui.common

interface OnCommentListener {
    fun onFavorite(id:Int)
    fun onReport(id:Int)
    fun writeReply(id:Int)
}