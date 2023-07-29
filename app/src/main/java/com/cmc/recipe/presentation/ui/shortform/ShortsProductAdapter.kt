package com.cmc.recipe.presentation.ui.shortform


import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.databinding.ItemShortsProductBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlide

class ShortsProductAdapter(private val clickListener: ShortsProductItemHolder.OnClickListener):
    BaseAdapter<String, ItemShortsProductBinding, ShortsProductItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsProductItemHolder {
        return ShortsProductItemHolder(
            ItemShortsProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }
}

class ShortsProductItemHolder(viewBinding: ItemShortsProductBinding,val clickListener: OnClickListener):
    BaseHolder<String, ItemShortsProductBinding>(viewBinding){

    override fun bind(binding: ItemShortsProductBinding, item: String?) {
        item?.let {string ->
            binding.ivProductMain.loadImagesWithGlide(string)
            binding.tvProductName.text = string
            binding.tvProductPrice.text = string

            binding.product.setOnClickListener {
                clickListener.onMoveSite(string)
            }
        }
    }

    interface OnClickListener{
        fun onMoveSite(url:String)
    }
}