package com.cmc.recipe.presentation.ui.mypage

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.cmc.recipe.R
import com.cmc.recipe.databinding.FragmentMypageNicknameBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.utils.CommonTextWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MypageNicknameFragment : BaseFragment<FragmentMypageNicknameBinding>(FragmentMypageNicknameBinding::inflate) {
    override fun initFragment() {
        initView()

        onActiveButton()

    }

    private fun initView(){
        val text = "닉네임은 1일 1회까지만 변경할 수 있습니다."
        val highlightText = "1일 1회"
        val color = ContextCompat.getColor(requireContext(), R.color.primary_color)

        val spannableString = SpannableString(text)
        val startIndex = text.indexOf(highlightText)

        if (startIndex != -1) {
            val redColorSpan = ForegroundColorSpan(color)
            spannableString.setSpan(redColorSpan, startIndex, startIndex + highlightText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        binding.tvInfo.text = spannableString
    }

    private fun onActiveButton(){
        binding.etNickName.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->
                requestChangeNickName()
            }
        ))
    }

    private fun requestChangeNickName(){
        binding.llValidateMsg.visibility = View.VISIBLE
        val flag = "SUCCESS"
        if(flag == "SUCCESS"){
            binding.llValidateMsg.text = "사용 가능한 닉네임입니다"
            binding.ivEditIcon.setImageResource(R.drawable.ic_check)
            binding.btnNext.isEnabled = true
        }else{
            binding.llValidateMsg.text = "중복된 닉네임입니다"
            binding.ivEditIcon.setImageResource(R.drawable.ic_validate_error)
            binding.btnNext.isEnabled = false
        }
    }

}