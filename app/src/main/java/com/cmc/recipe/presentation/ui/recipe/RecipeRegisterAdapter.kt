package com.cmc.recipe.presentation.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.ItemRecipeStepBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener

class RecipeRegisterAdapter(val clickListener: OnClickListener):
    BaseAdapter<String, ItemRecipeStepBinding, RecipeRegisterItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeRegisterItemHolder {
        return RecipeRegisterItemHolder(
            ItemRecipeStepBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }

    fun addItem(item: String) {
        getData().add(item)
        notifyItemInserted(getData().size - 1)
    }
}

class RecipeRegisterItemHolder(viewBinding: ItemRecipeStepBinding, val clickListener: OnClickListener):
    BaseHolder<String, ItemRecipeStepBinding>(viewBinding){
    override fun bind(binding: ItemRecipeStepBinding, item: String?) {
        binding.let { view->
            view.tvStepTitle.text = item
        }
    }
}
