package com.cmc.recipe.presentation.ui.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeOrder
import com.cmc.recipe.data.model.response.Stage
import com.cmc.recipe.databinding.ItemRecipeOrderBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlide
import com.cmc.recipe.utils.loadImagesWithGlideRound

class RecipeOrderAdapter:
    BaseAdapter<Stage, ItemRecipeOrderBinding, RecipeOrderItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeOrderItemHolder {
        return RecipeOrderItemHolder(
            ItemRecipeOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class RecipeOrderItemHolder(viewBinding: ItemRecipeOrderBinding):
    BaseHolder<Stage, ItemRecipeOrderBinding>(viewBinding){
    override fun bind(binding: ItemRecipeOrderBinding, item: Stage?) {
        binding.let { view ->
            item?.let { order ->
                if(order.stage_image_url.isEmpty()){
                    view.ivThumbnail.setImageResource(R.drawable.img_noimage)
                }else{
                    view.ivThumbnail.loadImagesWithGlideRound(order.stage_image_url,8)
                }

                view.tvOrder.text = if (position < 10) {
                    String.format("%02d", position)
                } else {
                    position.toString()
                }
                view.tvOrderTitle.text = order.stage_description
            }
        }

    }
}