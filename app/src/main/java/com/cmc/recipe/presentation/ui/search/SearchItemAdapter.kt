package com.cmc.recipe.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.SearchShorts
import com.cmc.recipe.databinding.ItemSearchBinding
import com.cmc.recipe.databinding.ItemSearchShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.loadImagesWithGlideRound

class SearchItemAdapter:
    BaseAdapter<String, ItemSearchBinding, SearchItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemHolder {
        return SearchItemHolder(
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }
}

class SearchItemHolder(viewBinding: ItemSearchBinding):
    BaseHolder<String, ItemSearchBinding>(viewBinding){
    override fun bind(binding: ItemSearchBinding, item: String?) {
        binding.root.text = item!!
    }
}
