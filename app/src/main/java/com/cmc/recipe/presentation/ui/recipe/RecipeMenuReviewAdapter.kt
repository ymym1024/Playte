package com.cmc.recipe.presentation.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.RecipeReview
import com.cmc.recipe.data.model.response.ReviewContent
import com.cmc.recipe.databinding.ItemRecipeReviewBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.common.ImageAdapter
import com.cmc.recipe.utils.parseAndFormatDate

class RecipeMenuReviewAdapter(private val upEventListener: OnClickListener,private val downEventListener: OnClickListener):
    BaseAdapter<ReviewContent, ItemRecipeReviewBinding, RecipeMenuReviewItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeMenuReviewItemHolder {
        return RecipeMenuReviewItemHolder(
            ItemRecipeReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            upEventListener,
            downEventListener
        )
    }
}

class RecipeMenuReviewItemHolder(viewBinding: ItemRecipeReviewBinding,private val upEventListener: OnClickListener,private val downEventListener: OnClickListener):
    BaseHolder<ReviewContent, ItemRecipeReviewBinding>(viewBinding){
    override fun bind(binding: ItemRecipeReviewBinding, item: ReviewContent?) {
        binding.let { view ->
            item?.let { review ->
                view.tvReviewDate.text = review.modified_at.parseAndFormatDate()
                view.tvReviewNick.text = review.writtenby
                view.tvReviewThumb.text = review.review_title
                view.tvReview.text = review.review_content
                view.tvRatingbar.numStars = review.rating
                view.tvUpcount.text = "${review.liked}"

                val adapter = ImageAdapter(96)
                view.rvReviewImage.adapter = adapter
                view.rvReviewImage.layoutManager = LinearLayoutManager(binding.root.context ,
                    LinearLayoutManager.HORIZONTAL, false)
                adapter.replaceData(review.review_images)
            }
        }

    }
}