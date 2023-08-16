package com.cmc.recipe.presentation.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.Comment
import com.cmc.recipe.databinding.ItemCommentBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder

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

        val adapter = ReplyAdapter(object : OnCommentListener{
            override fun onFavorite(id: Int) {

            }

            override fun onReport(id: Int) {

            }

        })

        binding.rvReply.adapter = adapter
        binding.rvReply.layoutManager = LinearLayoutManager(binding.root.context ,LinearLayoutManager.VERTICAL, false)
        val itemList = arrayListOf(
            Comment(comment = "좋은레시피입니다", nickname = "자칭 얼리어답터", comment_time = "2022.03.04", is_like = false, is_reply = false),
        )

        adapter.replaceData(itemList)

        if (itemList.size < 2) {
            binding.itemReply.visibility = View.GONE
            binding.rvReply.visibility = View.GONE
        } else {
            binding.tvReplyCnt.text = "답글 ${itemList.size}개 보기"
            binding.rvReply.visibility = View.VISIBLE
        }

        binding.itemReply.setOnClickListener { v ->
            if (binding.rvReply.visibility == View.VISIBLE) {
                binding.rvReply.visibility = View.GONE
            } else {
                binding.rvReply.visibility = View.VISIBLE
            }
        }
    }
}

