package com.cmc.recipe.presentation.ui.mypage

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cmc.recipe.R
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.databinding.FragmentMypageNicknameBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.common.RecipeSnackBar
import com.cmc.recipe.presentation.viewmodel.AuthViewModel
import com.cmc.recipe.presentation.viewmodel.UserViewModel
import com.cmc.recipe.utils.CommonTextWatcher
import com.cmc.recipe.utils.NetworkState
import com.cmc.recipe.utils.highlightText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take

@AndroidEntryPoint
class MypageNicknameFragment : BaseFragment<FragmentMypageNicknameBinding>(FragmentMypageNicknameBinding::inflate) {

    private val userViewModel: UserViewModel by viewModels()
    private var searchJob: Job? = null

    override fun initFragment() {
        initView()

        onActiveButton()
        saveButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
    }

    private fun initView() {
        val text = "닉네임은 1일 1회까지만 변경할 수 있습니다."
        val highlightText = "1일 1회"

        binding.tvInfo.text = requireContext().highlightText(text,highlightText)
    }

    private fun onActiveButton() {
        binding.etNickName.addTextChangedListener(CommonTextWatcher(
            onChanged = { text, _, _, _ ->
                searchJob?.cancel()
                if (text!!.contains(" ")) { // 공백포함
                    showImage(false,"띄어쓰기를 제외한 한글 닉네임으로 작성해주세요")
                } else {
                    // 공백이 포함되지 않은 경우에만 기존 로직 실행
                    searchJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(500L)
                        requestVerifyNickname(text.toString())
                    }
                }
            }
        ))
    }

    private fun saveButton() {
        binding.btnNext.setOnClickListener {
            chnageNickname(binding.etNickName.text.toString())
        }
    }

    private fun showImage(flag:Boolean,msg:String){
        if(flag){
            binding.llValidateMsg.text = "사용 가능한 닉네임입니다"
            binding.ivEditIcon.setImageResource(R.drawable.ic_check)
            binding.btnNext.isEnabled = true
        }else{
            binding.llValidateMsg.text = msg
            binding.ivEditIcon.setImageResource(R.drawable.ic_validate_error)
            binding.btnNext.isEnabled = false
        }
    }

    private fun requestVerifyNickname(nickname: String) {
        if (nickname.isNotBlank()) {
            binding.llValidateMsg.visibility = View.VISIBLE
            binding.ivEditIcon.visibility = View.VISIBLE
            launchWithLifecycle(lifecycle) {
                userViewModel.verifyNickname(nickname)
                userViewModel.verifyResult.collect {
                    when (it) {
                        is NetworkState.Success -> {
                            it.data?.let { data ->
                                if (data.code == "SUCCESS") {
                                    if(!data.data.isDuplicated){ // 중복 x
                                        showImage(true, "사용 가능한 닉네임입니다")
                                    }else{
                                        showImage(false, "중복된 닉네임입니다")
                                    }
                                } else {
                                    showImage(false,data.message)
                                }
                            }
                            userViewModel._verifyResult.value = NetworkState.Loading
                        }
                        is NetworkState.Error -> {
                            showImage(false,"${it.message}")

                            userViewModel._verifyResult.value = NetworkState.Loading
                        }
                        else -> {}
                    }

                }
            }
        }
    }

    private fun chnageNickname(nickname: String) {
        userViewModel.changeNickname(RequestNickname(nickname))
        launchWithLifecycle(lifecycle) {
            userViewModel.changeResult.take(1).onEach{
                when(it) {
                    is NetworkState.Success -> {
                        if (it.data.code == "SUCCESS") {
                            RecipeSnackBar(binding.root,"닉네임이 변경되었습니다.").show()
                        }
                    }
                    is NetworkState.Error -> {
                        showToastMessage("닉네임 변경에 실패했습니다. ${it.message.toString()}")
                    }
                    else -> {
                        showToastMessage("${it}")
                    }
                }
            }.launchIn(this)
        }
    }
}