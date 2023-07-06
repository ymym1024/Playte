package com.cmc.recipe.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseHolder<DataType, BindingType : ViewBinding> constructor(internal val viewDataBinding: BindingType) :
    RecyclerView.ViewHolder(viewDataBinding.root) {

    var item: DataType? = null

    abstract fun bind(binding: BindingType, item: DataType?)
}