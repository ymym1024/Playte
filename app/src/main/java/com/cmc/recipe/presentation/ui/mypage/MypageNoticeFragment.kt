package com.cmc.recipe.presentation.ui.mypage

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.Notice
import com.cmc.recipe.databinding.FragmentMypageNoticeBinding
import com.cmc.recipe.databinding.FragmentMypageTermsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.viewmodel.MyPageViewModel
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageNoticeFragment : BaseFragment<FragmentMypageNoticeBinding>(FragmentMypageNoticeBinding::inflate) {

    private val myPageViewModel : MyPageViewModel by viewModels()

    override fun initFragment() {

        requestNotice()
    }

    private fun initView(itemList:List<Notice>){
        val adapter = AccordionAdapter()
        binding.rvNotice.adapter = adapter
        binding.rvNotice.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        adapter.replaceData(itemList)
    }

    private fun requestNotice(){
        myPageViewModel.getNotice()
        launchWithLifecycle(lifecycle) {

            myPageViewModel.noticeResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                initView(data.data)
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        myPageViewModel._noticeResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        myPageViewModel._noticeResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

}