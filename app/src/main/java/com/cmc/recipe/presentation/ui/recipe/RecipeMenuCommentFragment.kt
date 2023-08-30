package com.cmc.recipe.presentation.ui.recipe

import android.annotation.SuppressLint
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.Comment
import com.cmc.recipe.data.model.response.CommentContent
import com.cmc.recipe.data.source.remote.request.CommentRequest
import com.cmc.recipe.databinding.FragmentRecipeMenuCommentBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.common.CommentAdapter
import com.cmc.recipe.presentation.ui.common.OnCommentListener
import com.cmc.recipe.presentation.ui.common.RecipeSnackBar
import com.cmc.recipe.presentation.viewmodel.CommentViewModel
import com.cmc.recipe.utils.CommonTextWatcher
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeMenuCommentFragment : BaseFragment<FragmentRecipeMenuCommentBinding>(FragmentRecipeMenuCommentBinding::inflate) {
    private val commentViewModel : CommentViewModel by viewModels()
    private var recipeId: Int = 0
    private var recipeImg : String = ""

    private lateinit var commnetAdapter : CommentAdapter
    private var adapterInitial = 0
    private var reponseCommentCnt = 0

    override fun initFragment() {
        arguments?.let {
            recipeId = it.getInt("recipeId", -1)
            recipeImg = it.getString("recipeImg","")
        }
        requestCommentList(recipeId)

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

        binding.button9.setOnClickListener {
            requestCommentSave(recipeId)
            binding.etComment.setText("")
        }

        binding.etComment.setOnEditorActionListener{ _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                if(binding.etComment.text.toString().isNotEmpty()){
                    requestCommentSave(recipeId)
                    binding.etComment.setText("")
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun findCommentItemById(reviewId: Int): CommentContent? {
        return commnetAdapter.getData().find { it.comment_id == reviewId }
    }

    private fun requestCommentSave(id:Int){
        commentViewModel.postRecipeCommentSave(id, CommentRequest(content = binding.etComment.text.toString()))
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                commentViewModel.commentRecipeSaveResult.collect {
                    when (it) {
                        is NetworkState.Success -> {
                            it.data?.let { data ->
                                if (data.code == "SUCCESS") {
                                    requestCommentList(recipeId)
                                } else {
                                    Log.d("data", "${data.data}")
                                }
                            }
                        }
                        is NetworkState.Error -> {
                            Log.d("data", "${it.message}")
                            commentViewModel._commentRecipeSaveResult.emit(NetworkState.Loading)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun requestCommentLikeOrUnLike(id: Int) {
        val review = findCommentItemById(id)
        if (review != null) {
            if (review.is_liked) {
                reponseCommentCnt = 0
                requestCommentUnLike(id)
            } else {
                reponseCommentCnt = 1
                requestCommentLike(id)
            }
        }
    }

    private fun requestCommentLike(id:Int){
        commentViewModel.postRecipeCommentLike(id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                commentViewModel.commentRecipeLikeResult.collect {
                    when (it) {
                        is NetworkState.Success -> {
                            it.data?.let { data ->
                                if (data.code == "SUCCESS") {
                                    if(reponseCommentCnt == 1){
                                        val review = findCommentItemById(id)
                                        Log.d("review data", "${review}")
                                        review?.is_liked = true
                                        review?.comment_likes = review?.comment_likes!! + 1
                                        commnetAdapter.notifyDataSetChanged()
                                    }else{}
                                } else {
                                    Log.d("data", "${data.data}")
                                }
                            }
                        }
                        is NetworkState.Error -> {
                            Log.d("data", "${it.message}")

                            commentViewModel._commentRecipeLikeResult.emit(NetworkState.Loading)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun requestCommentUnLike(id:Int){
        commentViewModel.postRecipeCommentUnLike(id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                commentViewModel.commentRecipeUnLikeResult.collect {
                    when (it) {
                        is NetworkState.Success -> {
                            it.data?.let { data ->
                                if (data.code == "SUCCESS") {
                                    Log.d("data-unlike", "${data}")
                                    val review = findCommentItemById(id)
                                    review?.is_liked = false
                                    review?.comment_likes = review?.comment_likes!! - 1
                                    commnetAdapter.notifyDataSetChanged()
                                } else {
                                    Log.d("data", "${data.data}")
                                }
                            }
                            commentViewModel._commentRecipeUnLikeResult.emit(NetworkState.Loading)
                        }
                        is NetworkState.Error -> {
                            Log.d("data", "${it.message}")

                            commentViewModel._commentRecipeUnLikeResult.emit(NetworkState.Loading)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun requestCommentList(id:Int) {
        commentViewModel.getRecipeComment(id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                commentViewModel.commentRecipeResult.collect{
                    when (it) {
                        is NetworkState.Success -> {
                            if (it.data.code == "SUCCESS") {
                                val item = it.data.data.content as ArrayList<CommentContent>
                                if(adapterInitial == 0){
                                    initRV(item)
                                }else{
                                    commnetAdapter.replaceData(item)
                                    binding.tvCommentCnt.text = "${item.size}개"
                                }
                            }
                            commentViewModel._commentRecipeResult.value = NetworkState.Loading
                        }

                        is NetworkState.Error -> {
                            showToastMessage("${it.message}")
                            commentViewModel._commentRecipeResult.value = NetworkState.Loading
                        }
                        is NetworkState.Loading -> {
                            // 프로그레스바 띄우기
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun requestCommentReport(id:Int){
        commentViewModel.reportRecipeComment(id)
        launchWithLifecycle(lifecycle) {
            commentViewModel.reportRecipeResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                requestCommentList(recipeId)
                                RecipeSnackBar(binding.root,"신고가 접수되었습니다.").show()
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        commentViewModel._reportRecipeResult.emit(NetworkState.Loading)
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        commentViewModel._reportRecipeResult.emit(NetworkState.Loading)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initRV(itemList:ArrayList<CommentContent>){

        commnetAdapter = CommentAdapter()
        commnetAdapter.setCommentListener(object :OnCommentListener{
            override fun onFavorite(id: Int) {
                requestCommentLikeOrUnLike(id)
            }

            override fun onReport(id: Int) {
                Log.d("id","${id}")
                requestCommentReport(id)
            }

            override fun writeReply(id: Int) {

            }
        })
        binding.rvComment.adapter = commnetAdapter
        binding.rvComment.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        commnetAdapter.replaceData(itemList)
        binding.tvCommentCnt.text = "${itemList.size}개"
    }

}