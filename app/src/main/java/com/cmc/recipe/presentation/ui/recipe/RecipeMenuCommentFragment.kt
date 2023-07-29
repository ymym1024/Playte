package com.cmc.recipe.presentation.ui.recipe

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.Comment
import com.cmc.recipe.databinding.FragmentRecipeMenuCommentBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.common.CommentAdapter
import com.cmc.recipe.presentation.ui.common.OnCommentListener

class RecipeMenuCommentFragment : BaseFragment<FragmentRecipeMenuCommentBinding>(FragmentRecipeMenuCommentBinding::inflate) {

    override fun initFragment() {
        val itemList = arrayListOf(
            Comment(comment = "좋은레시피입니다", nickname = "자칭 얼리어답터", comment_time = "2022.03.04", is_like = false, is_reply = false),
            Comment(comment = "좋은레시피입니다", nickname = "자칭 얼리어답터", comment_time = "2022.03.04", is_like = false, is_reply = false),
        )

        binding.tvCommentCnt.text = itemList.size.toString()
        initRV(itemList)
    }

    private fun initRV(itemList:ArrayList<Comment>){

        val adapter = CommentAdapter(object :OnCommentListener{
            override fun onFavorite(id: Int) {
                Log.d("onFavorite",id.toString())
            }

            override fun onReport(id: Int) {
                Log.d("onReport",id.toString())
            }

        })
        binding.rvComment.adapter = adapter
        binding.rvComment.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }

}