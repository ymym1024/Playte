package com.cmc.recipe.presentation.ui.common

import com.cmc.recipe.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
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
            eventListener.onFavorite(position)
        }

        binding.ibReport.setOnClickListener {
            eventListener.onReport(position)
        }

        binding.tvReply.setOnClickListener {
            eventListener.writeReply(position)
        }

        val adapter = ReplyAdapter(object : OnCommentListener{
            override fun onFavorite(id: Int) {

            }

            override fun onReport(id: Int) {

            }

            override fun writeReply(id: Int) {

            }
        })

        binding.rvReply.adapter = adapter
        binding.rvReply.layoutManager = LinearLayoutManager(binding.root.context ,LinearLayoutManager.VERTICAL, false)
        val itemList = arrayListOf(
            Comment(comment = "좋은레시피입니다", nickname = "자칭 얼리어답터", comment_time = "2022.03.04", is_like = false, is_reply = false),
            Comment(comment = "좋은레시피입니다", nickname = "자칭 얼리어답터", comment_time = "2022.03.04", is_like = false, is_reply = false),
            Comment(comment = "좋은레시피입니다", nickname = "자칭 얼리어답터", comment_time = "2022.03.04", is_like = false, is_reply = false),
        )

        adapter.replaceData(itemList)

        //답글
        if (itemList.size < 2) {
            binding.itemReply.visibility = View.GONE
        } else {
            binding.tvReplyCnt.text = "답글 ${itemList.size}개 보기"
        }

        binding.itemReply.setOnClickListener { v ->
            if (binding.rvReply.visibility == View.VISIBLE) {
                binding.tvReplyCnt.text = "답글 ${itemList.size}개 보기"
                binding.rvReply.visibility = View.GONE
            } else {
                binding.rvReply.visibility = View.VISIBLE
                binding.tvReplyCnt.text = "답글 숨기기"
            }
        }

        val layoutParams: ConstraintLayout.LayoutParams =
            binding.itemReply.layoutParams as ConstraintLayout.LayoutParams

        val newMarginStart: Int =
            viewDataBinding.root.resources.getDimensionPixelSize(R.dimen.comment_start) // replace with your desired margin value

        layoutParams.marginStart = newMarginStart

        binding.itemReply.layoutParams = layoutParams
    }
}

