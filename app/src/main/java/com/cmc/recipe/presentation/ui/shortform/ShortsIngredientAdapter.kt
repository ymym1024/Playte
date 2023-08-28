package com.cmc.recipe.presentation.ui.shortform


import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.Product
import com.cmc.recipe.databinding.ItemShortsIngredientBinding
import com.cmc.recipe.databinding.ItemShortsProductBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlideRound

class ShortsIngredientAdapter():
    BaseAdapter<String, ItemShortsIngredientBinding, ShortsIngredientItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsIngredientItemHolder {
        return ShortsIngredientItemHolder(
            ItemShortsIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class ShortsIngredientItemHolder(viewBinding: ItemShortsIngredientBinding):
    BaseHolder<String, ItemShortsIngredientBinding>(viewBinding){

    override fun bind(binding: ItemShortsIngredientBinding, item: String?) {
        binding.tvIngredientName.text = item
    }
}