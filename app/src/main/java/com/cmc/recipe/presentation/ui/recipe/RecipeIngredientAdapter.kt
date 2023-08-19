package com.cmc.recipe.presentation.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.IngredientItem
import com.cmc.recipe.data.model.response.Ingredient
import com.cmc.recipe.databinding.ItemTabIngredientBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener

class RecipeIngredientAdapter:
    BaseAdapter<Ingredient, ItemTabIngredientBinding, RecipeIngredientItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeIngredientItemHolder {
        return RecipeIngredientItemHolder(
            ItemTabIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }
}

class RecipeIngredientItemHolder(viewBinding: ItemTabIngredientBinding):
    BaseHolder<Ingredient, ItemTabIngredientBinding>(viewBinding){
    override fun bind(binding: ItemTabIngredientBinding, item: Ingredient?) {
        binding.let { view->
            view.tvIngredientName.text = item!!.ingredient_name
            view.tvIngredientCnt.text = "${item!!.ingredient_size}"
        }
    }
}
