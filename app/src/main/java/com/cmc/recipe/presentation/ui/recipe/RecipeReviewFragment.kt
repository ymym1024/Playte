package com.cmc.recipe.presentation.ui.recipe

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cmc.recipe.databinding.FragmentRecipeReviewBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.common.ImageAdapter
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.dpToPx
import com.cmc.recipe.utils.getRealPathFromURI
import com.cmc.recipe.utils.loadImagesWithGlideRound

class RecipeReviewFragment : BaseFragment<FragmentRecipeReviewBinding>(FragmentRecipeReviewBinding::inflate) {

    private lateinit var adapter: ReviewImageAdapter

    override fun initFragment() {
        initView()

        initRV()

    }

    private fun initView(){
        val url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg" // TODO : 삭제예정
        val pixel = dpToPx(requireContext(),100.toFloat())
        binding.ivRecipeThumbnail.layoutParams.width = pixel
        binding.ivRecipeThumbnail.layoutParams.height = pixel
        binding.ivRecipeThumbnail.loadImagesWithGlideRound(url,10)
    }

    private fun initRV(){
        val url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg";
        val image_list = arrayListOf(url,url,url)
        val adapter = ReviewImageAdapter()
        adapter.setListener(object :ReviewImageItemHolder.onActionListener{
            override fun delete(item: String) {
                adapter.removeItem(item)
            }
        })
        binding.rvReviewImage.adapter = adapter
        binding.rvReviewImage.layoutManager = LinearLayoutManager(binding.root.context ,
            LinearLayoutManager.HORIZONTAL, false)
        adapter.replaceData(image_list)
    }


}