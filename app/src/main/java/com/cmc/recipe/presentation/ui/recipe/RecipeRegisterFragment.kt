package com.cmc.recipe.presentation.ui.recipe

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.databinding.FragmentRecipeRegisterBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.getRealPathFromURI
import com.cmc.recipe.utils.loadImagesWithGlideRound

class RecipeRegisterFragment : BaseFragment<FragmentRecipeRegisterBinding>(FragmentRecipeRegisterBinding::inflate) {

    private lateinit var imageUri : Uri
    private var imageString : String? = ""

    override fun initFragment() {
        getThumbnail()
        initRecipeRv()
        initEvent()
    }

    private fun initEvent(){

        binding.ibPlus.setOnClickListener {
            binding.tvRecipeCount.text = "${Integer.parseInt(binding.tvRecipeCount.text as String)+1}"
        }

        binding.ibMinus.setOnClickListener {
            binding.tvRecipeCount.text = "${Integer.parseInt(binding.tvRecipeCount.text as String)-1}"
            if(binding.tvRecipeCount.text == "1") binding.ibMinus.isEnabled = false
        }
    }
    private fun initRecipeRv(){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
               // findNavController().navigate(R.id.action_recipeDetailFragment_to_recipeReviewFragment)
            }
        }

        val adapter = RecipeRegisterAdapter(clickListener)
        binding.rvStep.adapter = adapter
        binding.rvStep.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.etRecipe.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                val recipeStep = binding.etRecipe.text.toString()
                val recipeImg = imageString
                val recipeItem = RecipeStep(recipeImage = recipeImg!!, recipeDesc = recipeStep)
                if (recipeStep.isNotEmpty()) {
                    adapter.addItem(recipeItem)
                    binding.etRecipe.text.clear() // EditText를 초기화
                    imageString = ""
                    binding.ibImage.setImageResource(R.drawable.ic_image)
                    binding.tvRecipeStepCount.text = "${adapter.itemCount}"
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.ibImage.setOnClickListener { selectGallery() }
    }


    private val imageResultSingle = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            result?.data?.let { it ->
                imageUri = it.data!!
                imageString= requireActivity().getRealPathFromURI(it.data!!)
                binding.ibImage.loadImagesWithGlideRound(imageString!!,10)
            }
        }
    }

    private val imageThumbnailResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            result?.data?.let { it ->
                val image = requireActivity().getRealPathFromURI(it.data!!)
                binding.ivThumbnail.setPadding(0, 0, 0, 0)
                binding.ivThumbnail.loadImagesWithGlideRound(image!!,10)
            }
        }
    }

    private fun getThumbnail(){
        binding.ivThumbnail.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            imageThumbnailResult.launch(intent)
        }
    }

    private fun selectGallery(){
        var intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
        imageResultSingle.launch(intent)
    }
}