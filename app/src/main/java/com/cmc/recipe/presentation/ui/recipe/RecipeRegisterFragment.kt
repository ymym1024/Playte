package com.cmc.recipe.presentation.ui.recipe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.databinding.FragmentRecipeRegisterBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.loadImagesWithGlide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RecipeRegisterFragment : BaseFragment<FragmentRecipeRegisterBinding>(FragmentRecipeRegisterBinding::inflate) {

    private lateinit var imageUri : Uri
    private var imageString : String? = ""

    override fun initFragment() {
        initRecipeRv()
        initEvent()
    }

    private fun initEvent(){
        //TODO : viewmodel 로 구조 변경
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
                findNavController().navigate(R.id.action_recipeMainFragment_to_recipeDetailFragment)
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
                imageString= getRealPathFromURI(it.data!!)
                binding.ibImage.loadImagesWithGlide(imageString!!)
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String{
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity?.contentResolver?.query(uri, proj, null, null, null)
        if(cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }

    private fun selectGallery(){
        val readPermission = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)

        if(readPermission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ),
                Constant.REQ_GALLERY
            )
        }else{
            var intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            imageResultSingle.launch(intent)
        }
    }
}