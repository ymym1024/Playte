package com.cmc.recipe.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.data.model.SearchShorts
import com.cmc.recipe.databinding.ItemRecipeStepBinding
import com.cmc.recipe.databinding.ItemSearchShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.loadImagesWithGlide
import com.cmc.recipe.utils.loadImagesWithGlideRound

class SearchShortsAdapter(val clickListener: OnClickListener):
    BaseAdapter<SearchShorts, ItemSearchShortsBinding, SearchShortsItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchShortsItemHolder {
        return SearchShortsItemHolder(
            ItemSearchShortsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }
}

class SearchShortsItemHolder(viewBinding: ItemSearchShortsBinding, val clickListener: OnClickListener):
    BaseHolder<SearchShorts, ItemSearchShortsBinding>(viewBinding){
    override fun bind(binding: ItemSearchShortsBinding, item: SearchShorts?) {
        binding.let { view->
            view.ivShortsThumbnail.loadImagesWithGlideRound(item?.shorts_thumb!!,20)
            view.tvShortsTime.text = item?.shorts_time
            view.tvShortsNick.text = item?.shorts_nick
            view.tvShortsTitle.text = item?.shorts_title
        }
    }
}
