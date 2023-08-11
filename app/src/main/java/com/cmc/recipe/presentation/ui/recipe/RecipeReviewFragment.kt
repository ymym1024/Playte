package com.cmc.recipe.presentation.ui.recipe

import com.cmc.recipe.databinding.FragmentRecipeReviewBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.utils.dpToPx
import com.cmc.recipe.utils.loadImagesWithGlideRound

class RecipeReviewFragment : BaseFragment<FragmentRecipeReviewBinding>(FragmentRecipeReviewBinding::inflate) {
    override fun initFragment() {
        initView()
    }

    private fun initView(){
        val url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg" // TODO : 삭제예정
        val pixel = dpToPx(requireContext(),100.toFloat())
        binding.ivRecipeThumbnail.layoutParams.width = pixel
        binding.ivRecipeThumbnail.layoutParams.height = pixel
        binding.ivRecipeThumbnail.loadImagesWithGlideRound(url,10)
    }

}