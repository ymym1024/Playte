package com.cmc.recipe.presentation.ui.common

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.cmc.recipe.databinding.SnackbarRecipeBinding
import com.google.android.material.snackbar.Snackbar

class RecipeSnackBar(view: View, private val message: String) {

    companion object {

        fun make(view: View, message: String) = RecipeSnackBar(view, message)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 3000)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val snackbarBinding: SnackbarRecipeBinding = SnackbarRecipeBinding.inflate(inflater)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackbarBinding.root, 0)
        }
    }

    private fun initData() {
        snackbarBinding.tvSample.text = message
    }

    fun setAnchorView(anchorView: View): RecipeSnackBar {
        snackbar.anchorView = anchorView
        return this
    }

    fun show() {
        snackbar.show()
    }
}