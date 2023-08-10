package com.cmc.recipe.presentation.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.RecipeReview
import com.cmc.recipe.databinding.ItemMypageReviewBinding
import com.cmc.recipe.databinding.ItemRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.common.ImageAdapter
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.loadImagesWithGlide

class RecipeReviewAdapter:
    BaseAdapter<RecipeReview, ItemMypageReviewBinding, RecipeReviewItemHolder>() {

    private lateinit var listener : RecipeReviewItemHolder.onActionListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeReviewItemHolder {
        return RecipeReviewItemHolder(
            ItemMypageReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )
    }

    fun setListener(listener:RecipeReviewItemHolder.onActionListener){
        this.listener = listener
    }

    fun removeItem(item:RecipeReview){
        val itemList = getData()
        itemList.remove(item)
        notifyDataSetChanged()
    }
}

class RecipeReviewItemHolder(viewBinding: ItemMypageReviewBinding,val clickListener: onActionListener):
    BaseHolder<RecipeReview, ItemMypageReviewBinding>(viewBinding){
    override fun bind(binding: ItemMypageReviewBinding, item: RecipeReview?) {

        binding.let {
            item?.let { review ->
                it.tvDate.text = review.date
                it.tvFoodName.text = "음식이름"
                it.ratingBar.rating = review.stars.toFloat()
                it.tvReviewText.text = review.content

                val adapter = ImageAdapter(80)
                it.rvReviewImage.adapter = adapter
                it.rvReviewImage.layoutManager = LinearLayoutManager(binding.root.context ,LinearLayoutManager.HORIZONTAL, false)
                adapter.replaceData(review.image_list)
            }
        }

        binding.imageButton7.setOnClickListener {
            clickListener.action(item!!)
        }
    }

    interface onActionListener{
        fun action(item:RecipeReview)
    }
}