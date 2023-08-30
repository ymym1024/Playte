package com.cmc.recipe.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.data.model.SearchShorts
import com.cmc.recipe.data.model.response.ShortsContent
import com.cmc.recipe.databinding.ItemRecipeStepBinding
import com.cmc.recipe.databinding.ItemSearchShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.loadImagesWithGlide
import com.cmc.recipe.utils.loadImagesWithGlideRound

class SearchShortsAdapter(val clickListener: OnClickListener):
    BaseAdapter<ShortsContent, ItemSearchShortsBinding, SearchShortsItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchShortsItemHolder {
        return SearchShortsItemHolder(
            ItemSearchShortsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }
}

class SearchShortsItemHolder(viewBinding: ItemSearchShortsBinding, val clickListener: OnClickListener):
    BaseHolder<ShortsContent, ItemSearchShortsBinding>(viewBinding){
    override fun bind(binding: ItemSearchShortsBinding, item: ShortsContent?) {
        binding.let { view->
            view.ivShortsThumbnail.loadImagesWithGlideRound(item?.video_url!!,20)
            view.tvShortsTime.text = "${item?.video_time}"
            view.tvShortsNick.text = "${item?.writtenBy}"
            view.tvShortsTitle.text = "${item?.shortform_name}"
        }
    }
}
