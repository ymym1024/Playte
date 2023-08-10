package com.cmc.recipe.presentation.ui.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.RecipeOrder
import com.cmc.recipe.databinding.ItemRecipeOrderBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlide
import com.cmc.recipe.utils.loadImagesWithGlideRound

class RecipeOrderAdapter(private val context:Context):
    BaseAdapter<RecipeOrder, ItemRecipeOrderBinding, RecipeOrderItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeOrderItemHolder {
        return RecipeOrderItemHolder(
            ItemRecipeOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class RecipeOrderItemHolder(viewBinding: ItemRecipeOrderBinding):
    BaseHolder<RecipeOrder, ItemRecipeOrderBinding>(viewBinding){
    override fun bind(binding: ItemRecipeOrderBinding, item: RecipeOrder?) {
        binding.let { view ->
            item?.let { order ->
                view.ivThumbnail.loadImagesWithGlideRound(order.recipeImage,8)
                view.tvOrder.text = "0${order.recipeOrder}"
                view.tvOrderTitle.text = order.recipeInfo
            }
        }

    }
}