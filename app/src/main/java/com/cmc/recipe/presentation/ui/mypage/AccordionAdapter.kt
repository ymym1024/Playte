package com.cmc.recipe.presentation.ui.mypage

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.Notice
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.RecipeReview
import com.cmc.recipe.data.model.response.ReviewContent
import com.cmc.recipe.data.model.response.ReviewMyData
import com.cmc.recipe.databinding.ItemAccordionBinding
import com.cmc.recipe.databinding.ItemMypageReviewBinding
import com.cmc.recipe.databinding.ItemRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.common.ImageAdapter
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.highlightText
import com.cmc.recipe.utils.loadImagesWithGlide
import com.cmc.recipe.utils.parseAndFormatDate

class AccordionAdapter:
    BaseAdapter<Notice, ItemAccordionBinding, AccordionItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccordionItemHolder {
        return AccordionItemHolder(
            ItemAccordionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

}

class AccordionItemHolder(viewBinding: ItemAccordionBinding):
    BaseHolder<Notice, ItemAccordionBinding>(viewBinding){
    override fun bind(binding: ItemAccordionBinding, item: Notice?) {

        binding.let { view ->
            view.titleTextView.text = binding.root.context.highlightText("${item?.title}","Q.")
            view.contentTextView.text = binding.root.context.highlightText("${item?.content}","A.")

            view.titleTextView.setOnClickListener {
                val visibility = if (view.contentLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                view.contentLayout.visibility = visibility
            }

            view.collapseButton.setOnClickListener {
                view.contentLayout.visibility = View.GONE
            }
        }

    }
}