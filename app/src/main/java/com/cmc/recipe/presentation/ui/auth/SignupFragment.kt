package com.cmc.recipe.presentation.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cmc.recipe.databinding.FragmentSignupBinding
import com.cmc.recipe.presentation.MainActivity
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.viewmodel.UserViewModel
import com.cmc.recipe.utils.CommonTextWatcher
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    private val userViewModel : UserViewModel by viewModels()

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)
        mainActivity.hideBottomNavigation(true)
    }

    override fun initFragment() {
        onActiveButton()
    }

    private fun onActiveButton(){
        binding.etNickName.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->

                searchJob?.cancel()

                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(500L)
                    requestVerifyNickname(text.toString())
                }
            }
        ))
    }

    private fun requestVerifyNickname(nickname:String){
        if(nickname.isNotBlank()){
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    userViewModel.verifyNickname(nickname)
                    userViewModel.verifyResult.collect{
                        when(it){
                            is NetworkState.Success -> {
                                it.data?.let {data ->
                                    Log.d("SUCCESS","${it.data}")
                                    if(data.code == "SUCCESS"){ // TODO : 변경
                                        binding.llValidateMsg.visibility = View.VISIBLE
                                        binding.btnNext.isEnabled = true
                                    }else{
                                        binding.llValidateMsg.visibility = View.INVISIBLE
                                        binding.btnNext.isEnabled = false
                                        showToastMessage("닉네임을 다시 입력해 주세요") //TODO : 추후 수정예정
                                    }
                                }
                                userViewModel._verifyResult.value = NetworkState.Loading
                            }
                            is NetworkState.Error ->{
                                showToastMessage(it.message.toString())
                                userViewModel._verifyResult.value = NetworkState.Loading
                            }
                            else -> {}
                        }
                    }
                }
            }
        }

    }
}