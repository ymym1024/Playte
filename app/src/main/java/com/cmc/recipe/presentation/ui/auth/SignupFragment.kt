package com.cmc.recipe.presentation.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cmc.recipe.R
import com.cmc.recipe.databinding.FragmentSignupBinding
import com.cmc.recipe.presentation.MainActivity
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.viewmodel.AuthViewModel
import com.cmc.recipe.presentation.viewmodel.UserViewModel
import com.cmc.recipe.utils.CommonTextWatcher
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    private val userViewModel : UserViewModel by viewModels()
    private val authViewModel : AuthViewModel by viewModels()

    private val args by navArgs<SignupFragmentArgs>()

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)
        mainActivity.hideBottomNavigation(true)
    }

    override fun initFragment() {
        onActiveButton()
        onClickSignup()
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
            binding.llValidateMsg.visibility = View.VISIBLE
            binding.ivEditIcon.visibility = View.VISIBLE
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    userViewModel.verifyNickname(nickname)
                    userViewModel.verifyResult.collect{
                        when(it){
                            is NetworkState.Success -> {
                                it.data?.let {data ->
                                    Log.d("SUCCESS","${it.data}")
                                    if(data.code == "SUCCESS"){
                                        binding.llValidateMsg.text = "사용 가능한 닉네임입니다"
                                        binding.ivEditIcon.setImageResource(R.drawable.ic_check)
                                        binding.btnNext.isEnabled = true
                                    }else{
                                        binding.llValidateMsg.text = "중복된 닉네임입니다"
                                        binding.ivEditIcon.setImageResource(R.drawable.ic_check)
                                        binding.btnNext.isEnabled = false
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

    // 회원가입 요청
    private fun requestSignup(nickname: String){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                authViewModel.signup(args.accessToken,nickname)
                authViewModel.signupResult.collect{
                    when(it){
                        is NetworkState.Success -> {
                            it.data?.let {data ->
                                if(data.code == "SUCCESS"){ // TODO : 변경
                                    Log.d("accesstoken","${data.data.accessToken}")
                                    Log.d("refreshToken","${data.data.refreshToken}")
                                    moveNextPage()
                                }else{
                                    Log.d("data","${data.data}")
                                }
                            }
                            authViewModel._signupResult.value = NetworkState.Loading
                        }
                        is NetworkState.Error ->{
                            showToastMessage(it.message.toString())
                            authViewModel._signupResult.value = NetworkState.Loading
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun onClickSignup(){
        binding.btnNext.setOnClickListener {
            val nickname = binding.etNickName.text.toString()
            requestSignup(nickname)
        }
    }

    private fun moveNextPage(){
        findNavController().navigate(R.id.action_signupFragment_to_signupCompleteFragment)
    }
}