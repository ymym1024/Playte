package com.cmc.recipe.presentation.ui.upload

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.databinding.ItemRecipeStepBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.loadImagesWithGlide

class RecipeRegisterAdapter(val clickListener: OnClickListener):
    BaseAdapter<RecipeStep, ItemRecipeStepBinding, RecipeRegisterItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeRegisterItemHolder {
        return RecipeRegisterItemHolder(
            ItemRecipeStepBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }

    fun addItem(item: RecipeStep) {
        getData().add(item)
        notifyItemInserted(getData().size - 1)
    }
}

class RecipeRegisterItemHolder(viewBinding: ItemRecipeStepBinding, val clickListener: OnClickListener):
    BaseHolder<RecipeStep, ItemRecipeStepBinding>(viewBinding){
    override fun bind(binding: ItemRecipeStepBinding, item: RecipeStep?) {
        binding.let { view->
            view.tvStepTitle.text = item?.recipeDesc
            view.ibImage.loadImagesWithGlide(item?.recipeImage!!)
        }
    }
}
