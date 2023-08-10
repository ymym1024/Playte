package com.cmc.recipe.presentation.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.IngredientItem
import com.cmc.recipe.databinding.ItemTabIngredientBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener

class RecipeIngredientAdapter:
    BaseAdapter<IngredientItem, ItemTabIngredientBinding, RecipeIngredientItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeIngredientItemHolder {
        return RecipeIngredientItemHolder(
            ItemTabIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }
}

class RecipeIngredientItemHolder(viewBinding: ItemTabIngredientBinding):
    BaseHolder<IngredientItem, ItemTabIngredientBinding>(viewBinding){
    override fun bind(binding: ItemTabIngredientBinding, item: IngredientItem?) {
        binding.let { view->
            view.tvIngredientName.text = item!!.name
            view.tvIngredientCnt.text = item!!.cnt
        }
    }
}
