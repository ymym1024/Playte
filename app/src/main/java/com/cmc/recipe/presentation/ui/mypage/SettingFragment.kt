package com.cmc.recipe.presentation.ui.mypage

import androidx.navigation.fragment.findNavController
import com.cmc.recipe.R
import com.cmc.recipe.databinding.FragmentSettingBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment


class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    override fun initFragment() {
        binding.menuNick.setOnClickListener {
            movePage(R.id.action_settingFragment_to_mypageNicknameFragment)
        }
    }

    private fun movePage(naviRes:Int){
        findNavController().navigate(naviRes)
    }
}