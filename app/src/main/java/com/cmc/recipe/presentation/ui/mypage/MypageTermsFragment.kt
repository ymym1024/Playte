package com.cmc.recipe.presentation.ui.mypage

import com.cmc.recipe.databinding.FragmentMypageTermsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment


class MypageTermsFragment : BaseFragment<FragmentMypageTermsBinding>(FragmentMypageTermsBinding::inflate) {
    override fun initFragment() {

        // 이용약관 다운로드
        binding.tvDown.setOnClickListener {

        }
    }

}