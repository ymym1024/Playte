package com.cmc.recipe.presentation.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.Comment
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.ItemCommentBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener

class CommentAdapter(private val commentListener: OnCommentListener):
    BaseAdapter<Comment, ItemCommentBinding, CommentItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentItemHolder {
        return CommentItemHolder(
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            commentListener
        )
    }
}

class CommentItemHolder(viewBinding: ItemCommentBinding,val eventListener: OnCommentListener):
    BaseHolder<Comment, ItemCommentBinding>(viewBinding){
    override fun bind(binding: ItemCommentBinding, item: Comment?) {
        binding.let { view ->
            view.tvNick.text = item?.nickname
            view.tvComment.text = item?.comment
            view.tvTime.text = item?.comment_time
        }
        binding.ibThumbUp.setOnClickListener {
            eventListener.onFavorite(0)
        }

        binding.ibReport.setOnClickListener {
            eventListener.onReport(0)
        }
    }
}