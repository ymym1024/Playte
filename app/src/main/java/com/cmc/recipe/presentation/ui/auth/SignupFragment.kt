package com.cmc.recipe.presentation.ui.auth

import android.os.Bundle
import com.cmc.recipe.databinding.FragmentSignupBinding
import com.cmc.recipe.presentation.MainActivity
import com.cmc.recipe.presentation.ui.base.BaseFragment

class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)
        mainActivity.hideBottomNavigation(true)
    }

    override fun initFragment() {

    }

}