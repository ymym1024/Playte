package com.cmc.recipe.presentation.ui.mypage

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeReview
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.data.model.response.ReviewContent
import com.cmc.recipe.data.model.response.ReviewMyData
import com.cmc.recipe.data.model.response.ReviewMyResponse
import com.cmc.recipe.databinding.FragmentMyReviewBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.common.CustomBottomSheetFragment
import com.cmc.recipe.presentation.viewmodel.MyPageViewModel
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class MyReviewFragment : BaseFragment<FragmentMyReviewBinding>(FragmentMyReviewBinding::inflate) {

    private val myPageViewModel : MyPageViewModel by viewModels()
    private lateinit var adapter : RecipeReviewAdapter

    override fun initFragment() {

        requestMyPageList()
    }

    private fun requestMyPageList(){
        myPageViewModel.getMyReview()
        launchWithLifecycle(lifecycle) {
            myPageViewModel.myReviewResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                recipeRecyclerview(it.data.data)
                            }else{
                                Log.d("data-err","${data.data}")
                            }
                        }
                        myPageViewModel._myReviewResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        Log.d("data-err","${it.message}")
                        showToastMessage(it.message.toString())
                        myPageViewModel._myReviewResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    private fun requestDeleteReview(id:Int){
        myPageViewModel.deleteReview(id)
        viewLifecycleOwner.lifecycleScope.launch {
            myPageViewModel.reviewDeleteResult.take(1).onEach{
                    when(it) {
                        is NetworkState.Success -> {
                            if (it.data.code == "SUCCESS") {
                                adapter.removeItem(id)
                                showToastMessage("리뷰가 삭제되었습니다")
                            }
                            showToastMessage("${it.data}")
                        }
                        is NetworkState.Error -> {
                            showToastMessage("리뷰 삭제에 실패했습니다. ${it.message.toString()}")
                        }
                        is NetworkState.Loading -> {
                            // 프로그레스바 띄우기
                        }
                        else -> {
                            showToastMessage("${it}")
                        }
                    }
                }
                .launchIn(this)
        }
    }

    private fun recipeRecyclerview(itemList: List<ReviewMyData>){
        val id = 0
        val bottomSheetFragment = CustomBottomSheetFragment()
        bottomSheetFragment.setTitle("삭제하시겠습니까?")

        adapter = RecipeReviewAdapter()
        adapter.setListener(object : RecipeReviewItemHolder.onActionListener{
            override fun action(item: ReviewMyData) {
                bottomSheetFragment.setItemToDelete(item.review_id)
                bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
            }

        })

        binding.rvReview.adapter = adapter
        binding.rvReview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }

    fun CustomBottomSheetFragment.setItemToDelete(id: Int) {
        setListener {
            Log.d("review-id","${id}")
            requestDeleteReview(id) // 선택된 아이템으로 삭제 요청 보냄
        }
    }
}