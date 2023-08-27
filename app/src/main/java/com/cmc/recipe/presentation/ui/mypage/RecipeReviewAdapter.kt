package com.cmc.recipe.presentation.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.RecipeReview
import com.cmc.recipe.data.model.response.ReviewContent
import com.cmc.recipe.data.model.response.ReviewMyData
import com.cmc.recipe.databinding.ItemMypageReviewBinding
import com.cmc.recipe.databinding.ItemRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.common.ImageAdapter
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.loadImagesWithGlide
import com.cmc.recipe.utils.parseAndFormatDate

class RecipeReviewAdapter:
    BaseAdapter<ReviewMyData, ItemMypageReviewBinding, RecipeReviewItemHolder>() {

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

    fun removeItem(id:Int){
        val itemList = getData()
        val itemToRemove = itemList.find { it.review_id == id }
        itemList.remove(itemToRemove)
        notifyDataSetChanged()
    }
}

class RecipeReviewItemHolder(viewBinding: ItemMypageReviewBinding,val clickListener: onActionListener):
    BaseHolder<ReviewMyData, ItemMypageReviewBinding>(viewBinding){
    override fun bind(binding: ItemMypageReviewBinding, item: ReviewMyData?) {

        binding.let {
            item?.let { review ->
                it.tvDate.text = review.written_date.parseAndFormatDate()
                it.tvFoodName.text = review.recipe_name
                it.ratingBar.rating = review.review_rating.toFloat()
                it.tvReviewText.text = review.review_content

                val adapter = ImageAdapter(80)
                it.rvReviewImage.adapter = adapter
                it.rvReviewImage.layoutManager = LinearLayoutManager(binding.root.context ,LinearLayoutManager.HORIZONTAL, false)
                adapter.replaceData(review.img_list)
            }
        }

        binding.ibRemove.setOnClickListener {
            clickListener.action(item!!)
        }
    }

    interface onActionListener{
        fun action(item:ReviewMyData)
    }
}