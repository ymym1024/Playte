package com.cmc.recipe.presentation.ui.mypage

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.RecipeReview
import com.cmc.recipe.data.model.response.ReviewContent
import com.cmc.recipe.data.model.response.ReviewMyData
import com.cmc.recipe.data.model.response.ReviewMyResponse
import com.cmc.recipe.databinding.FragmentMyReviewBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.common.CustomBottomSheetFragment
import com.cmc.recipe.presentation.viewmodel.MyPageViewModel
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyReviewFragment : BaseFragment<FragmentMyReviewBinding>(FragmentMyReviewBinding::inflate) {

    private val myPageViewModel : MyPageViewModel by viewModels()
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

    private fun recipeRecyclerview(itemList: List<ReviewMyData>){

        val bottomSheetFragment = CustomBottomSheetFragment()
        bottomSheetFragment.setTitle("삭제하시겠습니까?")
        bottomSheetFragment.setListener {
            // 삭제이벤트 호출
        }

        val adapter = RecipeReviewAdapter()
        adapter.setListener(object : RecipeReviewItemHolder.onActionListener{
            override fun action(item: ReviewMyData) {
                bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
            }

        })

        binding.rvReview.adapter = adapter
        binding.rvReview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }

}