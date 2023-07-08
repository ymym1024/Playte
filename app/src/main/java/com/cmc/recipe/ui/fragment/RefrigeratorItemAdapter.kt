package com.cmc.recipe.ui.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.base.BaseAdapter
import com.cmc.recipe.base.BaseHolder
import com.cmc.recipe.databinding.ItemRefrigeratorBinding

class RefrigeratorItemAdapter(private val context:Context):
    BaseAdapter<String, ItemRefrigeratorBinding, RefrigeratorItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefrigeratorItemHolder {
        return RefrigeratorItemHolder(
            ItemRefrigeratorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class RefrigeratorItemHolder(viewBinding: ItemRefrigeratorBinding):BaseHolder<String,ItemRefrigeratorBinding>(viewBinding){
    override fun bind(binding: ItemRefrigeratorBinding, item: String?) {
        item?.let {
            binding.btnTitle.text = it
            binding.btnTitle.setOnClickListener {
                println(it) // TODO : 수정예정
            }
        }
    }
}