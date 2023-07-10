package com.cmc.recipe.presentation.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.databinding.ItemIngredientDetailBinding
import com.cmc.recipe.data.model.TrendsItem
import com.cmc.recipe.utils.loadImagesWithGlide

class TrendsDetailAdapter(private val context: Context):
    BaseAdapter<TrendsItem, ItemIngredientDetailBinding, TrendsDetailItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendsDetailItemHolder {
        return TrendsDetailItemHolder(
            ItemIngredientDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class TrendsDetailItemHolder(viewBinding: ItemIngredientDetailBinding):
    BaseHolder<TrendsItem, ItemIngredientDetailBinding>(viewBinding){
    override fun bind(binding: ItemIngredientDetailBinding, item: TrendsItem?) {
        item?.let { trends ->
            binding.ivTrends.loadImagesWithGlide("https://github.com/ymym1024/Travalue-Android/assets/41943129/30588945-e2dc-4e65-85e4-77a6a628f148")
            binding.tvType.text = trends.type
            binding.tvName.text = trends.name
            binding.tvTrends.text = trends.trends
            binding.tvDate.text = trends.date
            binding.tvCount.text = trends.count
            binding.tvPrice.text = trends.price
        }
    }
}