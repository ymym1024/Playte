package com.cmc.recipe.presentation.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.Comment
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.ItemCommentBinding
import com.cmc.recipe.databinding.ItemReplyBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener

class ReplyAdapter(private val commentListener: OnCommentListener):
    BaseAdapter<Comment, ItemReplyBinding, ReplyItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyItemHolder {
        return ReplyItemHolder(
            ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            commentListener
        )
    }
}

class ReplyItemHolder(viewBinding: ItemReplyBinding,val eventListener: OnCommentListener):
    BaseHolder<Comment, ItemReplyBinding>(viewBinding){
    override fun bind(binding: ItemReplyBinding, item: Comment?) {
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

