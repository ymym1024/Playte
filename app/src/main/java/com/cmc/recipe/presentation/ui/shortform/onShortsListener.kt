package com.cmc.recipe.presentation.ui.shortform

interface onShortsListener {
    fun onFavorite()
    fun onSave()
    fun onComment(id:Int)
    fun requestDetail(id:Int)
}