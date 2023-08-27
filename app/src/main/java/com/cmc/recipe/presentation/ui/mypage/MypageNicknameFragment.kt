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
import com.cmc.recipe.databinding.FragmentMypageNicknameBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.viewmodel.AuthViewModel
import com.cmc.recipe.presentation.viewmodel.UserViewModel
import com.cmc.recipe.utils.CommonTextWatcher
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MypageNicknameFragment : BaseFragment<FragmentMypageNicknameBinding>(FragmentMypageNicknameBinding::inflate) {

    private val userViewModel: UserViewModel by viewModels()
    private var searchJob: Job? = null

    override fun initFragment() {
        initView()

        onActiveButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
    }

    private fun initView() {
        val text = "닉네임은 1일 1회까지만 변경할 수 있습니다."
        val highlightText = "1일 1회"
        val color = ContextCompat.getColor(requireContext(), R.color.primary_color)

        val spannableString = SpannableString(text)
        val startIndex = text.indexOf(highlightText)

        if (startIndex != -1) {
            val redColorSpan = ForegroundColorSpan(color)
            spannableString.setSpan(
                redColorSpan,
                startIndex,
                startIndex + highlightText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        binding.tvInfo.text = spannableString
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
                                    showImage(true,data.message)
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
}