package com.cmc.recipe.presentation.ui.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.RecipeOrder
import com.cmc.recipe.databinding.ItemRecipeOrderBinding
import com.cmc.recipe.databinding.ItemRecipeRecommendBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlide

class RecipeRecommendAdapter(private val context:Context):
    BaseAdapter<RecipeItem, ItemRecipeRecommendBinding, RecipeRecommendItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeRecommendItemHolder {
        return RecipeRecommendItemHolder(
            ItemRecipeRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class RecipeRecommendItemHolder(viewBinding: ItemRecipeRecommendBinding):
    BaseHolder<RecipeItem, ItemRecipeRecommendBinding>(viewBinding){
    override fun bind(binding: ItemRecipeRecommendBinding, item: RecipeItem?) {
        binding.let { view ->
            item?.let { recipe ->
                view.ivThumbnail.loadImagesWithGlide(recipe.image_url)
                view.tvRecommendTitle.text = recipe.name
                view.tvRecommendTime.text = recipe.time.toString()
            }
        }

    }
}