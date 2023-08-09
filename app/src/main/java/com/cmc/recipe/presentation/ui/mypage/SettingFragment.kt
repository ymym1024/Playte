package com.cmc.recipe.presentation.ui.mypage

import androidx.navigation.fragment.findNavController
import com.cmc.recipe.R
import com.cmc.recipe.databinding.FragmentSettingBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment


class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    override fun initFragment() {

        initView()
        binding.let {
            it.menuNick.setOnClickListener {
                movePage(R.id.action_settingFragment_to_mypageNicknameFragment)
            }

            it.menuInfo.setOnClickListener {

            }

            it.menuTerms.setOnClickListener {
                movePage(R.id.action_settingFragment_to_mypageTermsFragment)
            }

            it.menuQna.setOnClickListener {
                movePage(R.id.action_settingFragment_to_mypageQnaFragment)
            }

            it.menuLogout.setOnClickListener {
                logout()
            }

            it.menuWithdrawal.setOnClickListener {
                withdrawal()
            }
        }
    }

    private fun initView() {
        binding.textView50.text = "CMC냉파"
    }

    private fun movePage(naviRes:Int){
        findNavController().navigate(naviRes)
    }

    private fun logout(){

    }

    private fun withdrawal(){

    }
}