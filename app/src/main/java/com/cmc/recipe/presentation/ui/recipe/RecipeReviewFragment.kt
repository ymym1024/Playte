package com.cmc.recipe.presentation.ui.recipe

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.databinding.FragmentRecipeReviewBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.utils.dpToPx
import com.cmc.recipe.utils.getRealPathFromURI
import com.cmc.recipe.utils.loadImagesWithGlideRound

class RecipeReviewFragment : BaseFragment<FragmentRecipeReviewBinding>(FragmentRecipeReviewBinding::inflate) {

    private lateinit var adapter: ReviewImageAdapter
    private var imageCount:Int = 0
    private var recipeId: Int = 0

    override fun initFragment() {
        var recipeImg: String = ""

        arguments?.let {
            recipeId = it.getInt("recipeId", -1)
            recipeImg = it.getString("recipeImg", "")
        }

        initView(recipeImg)
        getRatingScore()
        initRV()

        addImageButton()
    }

    private fun initView(thumb: String) {
        val pixel = dpToPx(requireContext(),100.toFloat())
        binding.ivRecipeThumbnail.layoutParams.width = pixel
        binding.ivRecipeThumbnail.layoutParams.height = pixel
        binding.ivRecipeThumbnail.loadImagesWithGlideRound(thumb,10)
    }

    private fun getRatingScore(){
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            binding.tvRatingbar.text = "${rating}"
        }
    }
    private fun initRV(){
        adapter = ReviewImageAdapter()
        adapter.setListener(object :ReviewImageItemHolder.onActionListener{
            override fun delete(item: String) {
                adapter.removeItem(item)
            }
        })
        binding.rvReviewImage.adapter = adapter
        binding.rvReviewImage.layoutManager = LinearLayoutManager(binding.root.context ,
            LinearLayoutManager.HORIZONTAL, false)
    }

    private val imageResultSingle = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            result?.data?.let { it ->
                if(imageCount<5) {
                    binding.tvImageCnt.text="${++imageCount}"
                    val image = requireActivity().getRealPathFromURI(it.data!!)
                    adapter.addItem(image)
                    binding.rvReviewImage.scrollToPosition(adapter.itemCount - 1)
                } else{
                    showToastMessage("이미지는 최대 5개 까지만 추가 됩니다")
                }
            }
        }
    }

    private fun addImageButton(){
        binding.btnImageAdd.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            imageResultSingle.launch(intent)
        }
    }

}