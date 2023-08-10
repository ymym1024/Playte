package com.cmc.recipe.presentation.ui.shortform


import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.Product
import com.cmc.recipe.databinding.ItemShortsProductBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlide
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
        item?.let { it ->
            binding.ivProductMain.loadImagesWithGlideRound(it.image,8)
            binding.tvProductName.text = it.name
            binding.tvProductPrice.text = it.price.toString()

            binding.product.setOnClickListener {
                clickListener.onMoveSite("it")
            }
        }
    }

    interface OnClickListener{
        fun onMoveSite(url:String)
    }
}