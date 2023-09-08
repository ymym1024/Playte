package com.cmc.recipe.presentation.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.response.RecipeIngredient
import com.cmc.recipe.databinding.ItemTabIngredientBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder

class RecipeIngredientAdapter:
    BaseAdapter<RecipeIngredient, ItemTabIngredientBinding, RecipeIngredientItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeIngredientItemHolder {
        return RecipeIngredientItemHolder(
            ItemTabIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }
}

class RecipeIngredientItemHolder(viewBinding: ItemTabIngredientBinding):
    BaseHolder<RecipeIngredient, ItemTabIngredientBinding>(viewBinding){
    override fun bind(binding: ItemTabIngredientBinding, item: RecipeIngredient?) {
        binding.let { view->
            view.tvIngredientName.text = item!!.ingredient_name
            view.tvIngredientCnt.text = "${item!!.ingredient_size}"
        }
    }
}
