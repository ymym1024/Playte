package com.cmc.recipe.presentation.ui.upload

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.data.model.response.Ingredients
import com.cmc.recipe.databinding.FragmentUploadRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.common.RecipeSnackBar
import com.cmc.recipe.presentation.viewmodel.UploadViewModel
import com.cmc.recipe.utils.NetworkState
import com.cmc.recipe.utils.getRealPathFromURI
import com.cmc.recipe.utils.loadImagesWithGlideRound
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class UploadRecipeFragment : BaseFragment<FragmentUploadRecipeBinding>(FragmentUploadRecipeBinding::inflate) {

    private var imageString : String? = ""
    private lateinit var thumbnailFile : File
    private lateinit var ingredientAdapter : IngredientAdapter
    private var count = 1;

    private val uploadViewModel : UploadViewModel by viewModels()

    override fun initFragment() {
        selectGallery()
        getThumbnail()
        initRecipeRv()
        initEvent()
        requestIngredient()
    }

    private fun initEvent(){
        binding.ibPlus.setOnClickListener {
            binding.tvRecipeCount.text = "${++count}"
            if(count > 1){
                binding.ibMinus.isEnabled = true
            }
            binding.ibMinus.setBackgroundResource(R.drawable.ic_minus_activate)
        }

        binding.ibMinus.setOnClickListener {
            binding.tvRecipeCount.text  = "${--count}"
            if(count == 1){
                binding.ibMinus.setBackgroundResource(R.drawable.ic_minus_deactivate)
                binding.ibMinus.isEnabled = false
            }
        }

        binding.btnSave.setOnClickListener {
            requestUpload()

        }
    }

    private fun requestUpload(){

        RecipeSnackBar(binding.btnSave,"레시피가 등록됐습니다!").show()
    }

    private fun initRecipeRv(){

        val adapter = RecipeStepAdapter()
        adapter.setListener(object : RecipeStepAdapter.onChangeListener{
            override fun change() {
                binding.tvRecipeStepCount.text = "${adapter.itemCount}"
            }

        })
        binding.rvStep.adapter = adapter
        binding.rvStep.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val swipeController = ItemTouchCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(binding.rvStep)
        binding.rvStep.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })


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
                    binding.btnSave.isEnabled =true
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun requestIngredient(){
        launchWithLifecycle(lifecycle) {
            uploadViewModel.getIngredients()
            uploadViewModel.ingredientsResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                Log.d("data",data.data.toString())
                                initAdapter(data.data)
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        uploadViewModel._ingredientsResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        uploadViewModel._ingredientsResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }
    private fun initAdapter(dataList:List<Ingredients>){
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
            viewDialog(data.ingredient_unit,data.ingredient_name)
        }
    }

    private fun viewDialog(unit:String,name:String){
        if(unit == "개") {
            val dialog = IngredientCountDialog(name)
            dialog.setListener(object : IngredientCountDialog.onCountListener{
                override fun getCount(count: Int) {
                    dialogBinding(name,count,unit)
                }

            })
            dialog.show(parentFragmentManager,"IngredientCountDialog")
        }else{
            val dialog = IngredientEtcDialog(name,unit)
            dialog.setListener(object :IngredientEtcDialog.onCountListener{
                override fun getCount(count: Int) {
                    dialogBinding(name,count,unit)
                }
            })
            dialog.show(parentFragmentManager,"IngredientEtcDialog")
        }
    }

    private fun dialogBinding(name:String,count:Int,unit:String){
        val nameAndCount = "${name} ${count}${unit}"
        ingredientAdapter.addItem(nameAndCount)
        binding.etRecipeIngredient.setText("")
    }


    private val imageResultSingle = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            result?.data?.let { it ->
                imageString= requireActivity().getRealPathFromURI(it.data!!)
                thumbnailFile = File(imageString)
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