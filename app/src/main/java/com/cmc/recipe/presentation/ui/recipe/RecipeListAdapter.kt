package com.cmc.recipe.presentation.ui.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.ItemRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.loadImagesWithGlide

class RecipeListAdapter(private val context:Context,val clickListener: OnClickListener):
    BaseAdapter<RecipeItem, ItemRecipeBinding, RecipeItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeItemHolder {
        return RecipeItemHolder(
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }
}

class RecipeItemHolder(viewBinding: ItemRecipeBinding, val clickListener: OnClickListener):
    BaseHolder<RecipeItem, ItemRecipeBinding>(viewBinding){
    override fun bind(binding: ItemRecipeBinding, item: RecipeItem?) {
        binding.recipeItem.setOnClickListener {
            clickListener.onMovePage(0)
        }

        binding.let {
            item?.let { recipe ->
                it.ivRecipeMain.loadImagesWithGlide("https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg")
                it.tvRecipeName.text = recipe.name
                it.tvRecipeTime.text = "${recipe.time}분"
                it.tvRecipeIngrdient1.text = recipe.ingredient1
                it.tvRecipeIngrdient2.text = recipe.ingredient2
                it.tvRecipeIngrdient3.text = recipe.ingredient3
            }
        }
    }
}