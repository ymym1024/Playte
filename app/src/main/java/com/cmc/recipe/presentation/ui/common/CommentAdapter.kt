package com.cmc.recipe.presentation.ui.common

import android.annotation.SuppressLint
import com.cmc.recipe.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.Comment
import com.cmc.recipe.data.model.response.CommentContent
import com.cmc.recipe.data.model.response.CommentData
import com.cmc.recipe.databinding.ItemCommentBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.shortform.onShortsListener
import com.cmc.recipe.utils.formatDateRelativeToNow
import com.cmc.recipe.utils.parseDateTime


class CommentAdapter:
    BaseAdapter<CommentContent, ItemCommentBinding, CommentItemHolder>() {

    private lateinit var commentListener:OnCommentListener

    fun setCommentListener(commentListener: OnCommentListener){
        this.commentListener = commentListener
    }

    fun removeItem(id:Int){
        val itemList = getData()
        val itemToRemove = itemList.find { it.comment_id == id }
        itemList.remove(itemToRemove)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentItemHolder {
        return CommentItemHolder(
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            commentListener
        )
    }
}

class CommentItemHolder(viewBinding: ItemCommentBinding,val eventListener: OnCommentListener):
    BaseHolder<CommentContent, ItemCommentBinding>(viewBinding){
    @SuppressLint("ResourceAsColor")
    override fun bind(binding: ItemCommentBinding, item: CommentContent?) {
        binding.let { view ->
            view.tvNick.text = item?.comment_writtenby
            view.tvComment.text = item?.comment_content
            view.tvTime.text = item?.created_at?.parseDateTime()?.formatDateRelativeToNow()
            view.tvThumbCnt.text = "${item?.comment_likes}"
        }
        val textColor1 = ContextCompat.getColor(binding.root.context, R.color.gray_8)
        val textColor = ContextCompat.getColor(binding.root.context, R.color.primary_color)
        if(item?.is_liked!!) {
            binding.ibThumbUp.setImageResource(R.drawable.ic_heart_full)
            binding.tvThumbCnt.setTextColor(textColor)
        }
        else {
            binding.ibThumbUp.setImageResource(R.drawable.ic_heart_gray)
            binding.tvThumbCnt.setTextColor(textColor1)
        }

        binding.ibThumbUp.setOnClickListener {
            eventListener.onFavorite(item?.comment_id!!)
        }

        binding.ibReport.setOnClickListener {
            eventListener.onReport(item?.comment_id!!)
        }

        binding.tvReply.setOnClickListener {
            eventListener.writeReply(item?.comment_id!!)
        }

//        val adapter = ReplyAdapter(object : OnCommentListener{
//            override fun onFavorite(id: Int) {
//
//            }
//
//            override fun onReport(id: Int) {
//
//            }
//
//            override fun writeReply(id: Int) {
//
//            }
//        })

//        binding.rvReply.adapter = adapter
//        binding.rvReply.layoutManager = LinearLayoutManager(binding.root.context ,LinearLayoutManager.VERTICAL, false)
//        val itemList = arrayListOf(
//            Comment(comment = "좋은레시피입니다", nickname = "자칭 얼리어답터", comment_time = "2022.03.04", is_like = false, is_reply = false),
//            Comment(comment = "좋은레시피입니다", nickname = "자칭 얼리어답터", comment_time = "2022.03.04", is_like = false, is_reply = false),
//            Comment(comment = "좋은레시피입니다", nickname = "자칭 얼리어답터", comment_time = "2022.03.04", is_like = false, is_reply = false),
//        )
//
//        adapter.replaceData(itemList)

        //답글
//        if (itemList.size < 2) {
//            binding.itemReply.visibility = View.GONE
//        } else {
//            binding.tvReplyCnt.text = "답글 ${itemList.size}개 보기"
//        }
//
//        binding.itemReply.setOnClickListener { v ->
//            if (binding.rvReply.visibility == View.VISIBLE) {
//                binding.tvReplyCnt.text = "답글 ${itemList.size}개 보기"
//                binding.rvReply.visibility = View.GONE
//            } else {
//                binding.rvReply.visibility = View.VISIBLE
//                binding.tvReplyCnt.text = "답글 숨기기"
//            }
//        }

        val layoutParams: ConstraintLayout.LayoutParams =
            binding.itemReply.layoutParams as ConstraintLayout.LayoutParams

        val newMarginStart: Int =
            viewDataBinding.root.resources.getDimensionPixelSize(R.dimen.comment_start) // replace with your desired margin value

        layoutParams.marginStart = newMarginStart

        binding.itemReply.layoutParams = layoutParams
    }
}

