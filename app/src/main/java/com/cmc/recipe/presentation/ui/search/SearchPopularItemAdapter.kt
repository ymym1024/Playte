package com.cmc.recipe.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.databinding.ItemSearchPopularBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder

class SearchPopularItemAdapter:
    BaseAdapter<String, ItemSearchPopularBinding, SearchPopularHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPopularHolder {
        return SearchPopularHolder(
            ItemSearchPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }
}

class SearchPopularHolder(viewBinding: ItemSearchPopularBinding):
    BaseHolder<String, ItemSearchPopularBinding>(viewBinding){
    override fun bind(binding: ItemSearchPopularBinding, item: String?) {
        binding.tvSequence.text = "${position+1}"
        binding.tvKeyword.text = item
    }
}
