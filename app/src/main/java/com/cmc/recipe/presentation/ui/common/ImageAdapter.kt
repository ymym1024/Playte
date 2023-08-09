package com.cmc.recipe.presentation.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.databinding.ItemImageBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlideRound

class ImageAdapter:
    BaseAdapter<String, ItemImageBinding, ImageItemHolder>() {

    private var type : String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemHolder {
        return ImageItemHolder(
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class ImageItemHolder(viewBinding: ItemImageBinding):
    BaseHolder<String, ItemImageBinding>(viewBinding){
    override fun bind(binding: ItemImageBinding, item: String?) {
        binding.imageView11.loadImagesWithGlideRound(item!!,10)
    }
}