package com.cmc.recipe.presentation.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.databinding.ItemImageDeleteBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.mypage.RecipeReviewItemHolder
import com.cmc.recipe.utils.loadImagesWithGlideRound
import com.cmc.recipe.utils.resizeBitmapToSquare

class ReviewImageAdapter:
    BaseAdapter<String, ItemImageDeleteBinding, ReviewImageItemHolder>() {

    private lateinit var listener : ReviewImageItemHolder.onActionListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewImageItemHolder {
        return ReviewImageItemHolder(
            ItemImageDeleteBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )
    }

    fun setListener(listener: ReviewImageItemHolder.onActionListener){
        this.listener = listener
    }

    fun addItem(item: String) {
        val itemList = getData()
        itemList.add(item)
        notifyItemInserted(getData().size - 1)
    }

    fun removeItem(item: String) {
        val itemList = getData()
        itemList.remove(item)
        notifyDataSetChanged()
    }
}

class ReviewImageItemHolder(viewBinding: ItemImageDeleteBinding, val actionListener: onActionListener):
    BaseHolder<String, ItemImageDeleteBinding>(viewBinding){
    override fun bind(binding: ItemImageDeleteBinding, item: String?) {
        binding.let { view->
            view.ivImage.resizeBitmapToSquare(100)
            view.ivImage.loadImagesWithGlideRound(item!!,10)
            view.btnDelete.setOnClickListener {
                actionListener.delete(item)
            }
        }
    }

    interface onActionListener{
        fun delete(item:String)
    }
}
