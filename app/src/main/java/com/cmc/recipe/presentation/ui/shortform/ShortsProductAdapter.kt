package com.cmc.recipe.presentation.ui.shortform


import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.Product
import com.cmc.recipe.databinding.ItemShortsProductBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlideRound

class ShortsProductAdapter(private val clickListener: ShortsProductItemHolder.OnClickListener):
    BaseAdapter<Product, ItemShortsProductBinding, ShortsProductItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsProductItemHolder {
        return ShortsProductItemHolder(
            ItemShortsProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }
}

class ShortsProductItemHolder(viewBinding: ItemShortsProductBinding,val clickListener: OnClickListener):
    BaseHolder<Product, ItemShortsProductBinding>(viewBinding){

    override fun bind(binding: ItemShortsProductBinding, item: Product?) {
        binding.ivProductMain.loadImagesWithGlideRound(item?.image,8)
        binding.tvProductName.text = item?.name
        binding.tvProductPrice.text = item?.price.toString()

        binding.product.setOnClickListener {
            clickListener.onMoveSite(item?.url.toString())
        }
    }

    interface OnClickListener{
        fun onMoveSite(url:String)
    }
}