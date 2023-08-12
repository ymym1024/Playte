package com.cmc.recipe.presentation.ui.upload

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.Ingredient
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.databinding.FragmentUploadRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.getRealPathFromURI
import com.cmc.recipe.utils.loadImagesWithGlideRound

class UploadRecipeFragment : BaseFragment<FragmentUploadRecipeBinding>(FragmentUploadRecipeBinding::inflate) {

    private lateinit var imageUri : Uri
    private var imageString : String? = ""
    private lateinit var ingredientAdapter : IngredientAdapter

    override fun initFragment() {
        selectGallery()
        getThumbnail()
        initRecipeRv()
        initEvent()
        initAdapter()
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

        val adapter = RecipeStepAdapter(clickListener)
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
    }

    private fun initAdapter(){
        val dataList = arrayListOf(
            Ingredient("토마토","재료"),
            Ingredient("토마토 소스","양념"),
            Ingredient("토마토","양념"),
            Ingredient("토마토 소스","양념"),
            Ingredient("토마토","양념"),
            Ingredient("토마토 소스","양념")
        )

        ingredientAdapter = IngredientAdapter()
        ingredientAdapter.setActionListener(object :IngredientItemHolder.actionListener{
            override fun remove(name: String) {
                ingredientAdapter.removeItem(name)
            }
        })

        binding.rvCompleteIngredient.adapter = ingredientAdapter
        binding.rvCompleteIngredient.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)

        val adapter = IngredientListAdapter(requireContext(),dataList)
        binding.etRecipeIngredient.setAdapter(adapter)
        binding.etRecipeIngredient.setOnItemClickListener { _, v, position, _ ->
            val data = adapter.getItem(position)
            viewDialog("개수",data.name)
        }
    }

    private fun viewDialog(type:String,name:String){
        if(type == "개수") {
            val dialog = IngredientCountDialog(name)
            dialog.setListener(object : IngredientCountDialog.onCountListener{
                override fun getCount(count: Int) {
                    val nameAndCount = "${name} ${count}개"
                    ingredientAdapter.addItem(nameAndCount)
                    binding.etRecipeIngredient.setText("")
                }

            })
            dialog.show(parentFragmentManager,"IngredientCountDialog")
        }
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
        binding.ibImage.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            imageResultSingle.launch(intent)
        }
    }
}