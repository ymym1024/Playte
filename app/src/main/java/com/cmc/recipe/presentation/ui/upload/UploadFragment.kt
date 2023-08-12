package com.cmc.recipe.presentation.ui.upload


import android.content.Intent
import com.cmc.recipe.R
import com.cmc.recipe.databinding.FragmentMypageBinding
import com.cmc.recipe.databinding.FragmentUploadBinding
import com.cmc.recipe.presentation.ui.MainActivity
import com.cmc.recipe.presentation.ui.base.BaseFragment


class UploadFragment : BaseFragment<FragmentUploadBinding>(FragmentUploadBinding::inflate) {

    override fun initFragment() {
        binding.btnUploadRecipe.setOnClickListener {
            val intent = Intent(activity, UploadActivity::class.java)
            intent.putExtra("startDestination", "recipe")
            startActivity(intent)
        }
        binding.btnUploadShorts.setOnClickListener {
            val intent = Intent(activity, UploadActivity::class.java)
            intent.putExtra("startDestination", "upload")
            startActivity(intent)
        }
    }

}