package com.cmc.recipe.presentation.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cmc.recipe.R
import com.cmc.recipe.databinding.FragmentSignupBinding
import com.cmc.recipe.databinding.FragmentSignupCompleteBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment

class SignupCompleteFragment : BaseFragment<FragmentSignupCompleteBinding>(FragmentSignupCompleteBinding::inflate) {

    override fun initFragment() {
        binding.btnComplete.setOnClickListener {
            findNavController().navigate(R.id.action_signupCompleteFragment_to_recipeMainFragment)
        }
    }


}