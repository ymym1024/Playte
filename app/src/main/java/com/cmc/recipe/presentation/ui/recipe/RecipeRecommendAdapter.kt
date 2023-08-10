package com.cmc.recipe.presentation.ui.recipe

import android.content.Context
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.RecipeOrder
import com.cmc.recipe.databinding.ItemRecipeOrderBinding
import com.cmc.recipe.databinding.ItemRecipeRecommendBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlide
import com.cmc.recipe.utils.loadImagesWithGlideRound

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
                view.ivThumbnail.outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View, outline: Outline) {

                        val cornerRadius = view.resources.getDimensionPixelSize(R.dimen.corner_radius) // 둥근 정도 조절
                        outline.setRoundRect(0, 0, view.width, view.height+200, cornerRadius.toFloat())
                    }
                }

                view.ivThumbnail.clipToOutline = true
                view.tvRecommendTitle.text = recipe.name
                view.tvRecommendTime.text = "조리시간 ${recipe.time}분"
            }
        }

    }
}