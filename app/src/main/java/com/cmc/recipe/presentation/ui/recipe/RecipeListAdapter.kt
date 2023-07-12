package com.cmc.recipe.presentation.ui.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.ItemRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlide

class RecipeListAdapter(private val context:Context):
    BaseAdapter<RecipeItem, ItemRecipeBinding, RecipeItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeItemHolder {
        return RecipeItemHolder(
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class RecipeItemHolder(viewBinding: ItemRecipeBinding):
    BaseHolder<RecipeItem, ItemRecipeBinding>(viewBinding){
    override fun bind(binding: ItemRecipeBinding, item: RecipeItem?) {
        item?.let { recipe ->
            binding.ivRecipeMain.loadImagesWithGlide(recipe.image_url)
            binding.tvRecipeName.text = recipe.name
            binding.tvRecipeTime.text = "${recipe.time}분"
            binding.tvRecipeIngrdient1.text = recipe.ingredient1
            binding.tvRecipeIngrdient2.text = recipe.ingredient2
            binding.tvRecipeIngrdient3.text = recipe.ingredient3
        }
    }
}