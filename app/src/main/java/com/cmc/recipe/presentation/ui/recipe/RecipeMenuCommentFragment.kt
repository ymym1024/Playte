package com.cmc.recipe.presentation.ui.recipe

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.Comment
import com.cmc.recipe.databinding.FragmentRecipeMenuCommentBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.common.CommentAdapter
import com.cmc.recipe.presentation.ui.common.OnCommentListener
import com.cmc.recipe.utils.CommonTextWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecipeMenuCommentFragment : BaseFragment<FragmentRecipeMenuCommentBinding>(FragmentRecipeMenuCommentBinding::inflate) {

    override fun initFragment() {
        val itemList = arrayListOf(
            Comment(comment = "좋은레시피입니다", nickname = "자칭 얼리어답터", comment_time = "2022.03.04", is_like = false, is_reply = false),
            Comment(comment = "좋은레시피입니다", nickname = "자칭 얼리어답터", comment_time = "2022.03.04", is_like = false, is_reply = false),
        )

        binding.tvCommentCnt.text = itemList.size.toString()
        initRV(itemList)

        // 댓글 입력 시 이벤트 처리
        binding.etComment.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->
                text?.let {
                    if (text.isEmpty()) {
                        binding.tvEditIng.visibility = View.INVISIBLE
                    } else {
                        binding.tvEditIng.visibility = View.VISIBLE
                        binding.tvEditIng.text = "답글 남기는 중...";
                    }
                }
            }
        ))
    }

    private fun initRV(itemList:ArrayList<Comment>){

        val adapter = CommentAdapter(object :OnCommentListener{
            override fun onFavorite(id: Int) {
                Log.d("onFavorite",id.toString())
            }

            override fun onReport(id: Int) {
                Log.d("onReport",id.toString())
            }

            override fun writeReply(id: Int) {
                val nick = itemList.get(id).nickname
                writeReply(nick)
            }

        })
        binding.rvComment.adapter = adapter
        binding.rvComment.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }

    private fun writeReply(nick:String){
        binding.etComment.setText("@$nick ")
    }

}