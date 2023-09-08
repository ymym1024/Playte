package com.cmc.recipe.presentation.ui.shortform

interface onShortsListener {
    fun onFavorite(id:Int)
    fun onSave(id:Int)
    fun onComment(id:Int)
}