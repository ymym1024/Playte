package com.cmc.recipe.presentation.ui.upload

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.data.model.response.Ingredients
import com.cmc.recipe.data.source.remote.request.Ingredient
import com.cmc.recipe.data.source.remote.request.UploadRecipeRequest
import com.cmc.recipe.databinding.FragmentUploadRecipeBinding
import com.cmc.recipe.presentation.ui.MainActivity
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.common.RecipeSnackBar
import com.cmc.recipe.presentation.viewmodel.UploadViewModel
import com.cmc.recipe.utils.CommonTextWatcher
import com.cmc.recipe.utils.NetworkState
import com.cmc.recipe.utils.getRealPathFromURI
import com.cmc.recipe.utils.loadImagesWithGlideRound
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@AndroidEntryPoint
class UploadRecipeFragment : BaseFragment<FragmentUploadRecipeBinding>(FragmentUploadRecipeBinding::inflate) {

    private var imageString : String? = ""
    private lateinit var thumbnailUri : String
    private lateinit var ingredientAdapter : IngredientAdapter
    private var count = 1

    private lateinit var stageAdapter : RecipeStepAdapter

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
            validate()
            uploadImageURI(thumbnailUri)
        }
    }

    private fun requestUpload(){
        // 레시피 업로드 요청
        val cook_time = Integer.parseInt(binding.etRecipeTime.text.toString())//조리시간
        val recipe_description = binding.etRecipeDescription.getText().toString()// 레시피설명
        val recipe_name = binding.etRecipeName.getText().toString() // 레시피이름
        val recipe_stage = stageAdapter.getData().toList()
        val recipe_ingredient = ingredientAdapter.getData().map {
            Ingredient(ingredient_id =it.ingredient_id, ingredient_size = it.ingredient_count)
        }

        val request = UploadRecipeRequest(cook_time = cook_time, recipe_description = recipe_description, recipe_name = recipe_name,
            recipe_thumbnail_img = thumbnailUri, serving_size = count, recipe_stages = recipe_stage, ingredients = recipe_ingredient)

        launchWithLifecycle(lifecycle) {
            uploadViewModel.uploadRecipe(request)
            uploadViewModel.uploadRecipeResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data.let { data ->
                            if(data.code == "SUCCESS"){
                                requireActivity().finish()
                                val activity = MainActivity.mainActivity as MainActivity
                                val rootView: View = activity.window.decorView.rootView // a 액티비티의 레이아웃 최상단 뷰를 가져옴
                                // 스낵바 생성 및 표시
                                RecipeSnackBar(rootView,"레시피가 등록됐습니다!").setAnchorView(rootView.findViewById(R.id.bottom_nav)).show()
                            }else{
                                Log.d("data","${data.data}")
                                RecipeSnackBar(binding.btnSave,"${data.data}").show()
                            }
                        }
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                    }
                    is NetworkState.Loading -> {
                        // progress 로딩
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initRecipeRv(){
        //레시피 이름
        binding.etRecipeName.editText.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->
                validate()
            }
        ))
        //레시피 설명
        binding.etRecipeDescription.editText.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->
                validate()
            }
        ))

        stageAdapter = RecipeStepAdapter()
        stageAdapter.setListener(object : RecipeStepAdapter.onChangeListener{
            override fun change() {
                //binding.tvRecipeStepCount.text = "${stageAdapter.itemCount}"
            }

        })
        binding.rvStep.adapter = stageAdapter
        binding.rvStep.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val swipeController = ItemTouchCallback(stageAdapter)
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
                val recipeItem = RecipeStep(image_url = recipeImg!!, stage_description = recipeStep)
                if (recipeStep.isNotEmpty()) {
                    stageAdapter.addItem(recipeItem)
                    binding.etRecipe.text.clear() // EditText를 초기화
                    imageString = ""
                    binding.ibImage.setImageResource(R.drawable.ic_image)
                    binding.tvRecipeStepCount.text = "${stageAdapter.itemCount}"
                    validate()
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun validate(){
        // 이름, 재료, 양념, 레시피
        binding.btnSave.isEnabled =
            binding.ivThumbnail.drawable !==null &&
            binding.etRecipeName.editText.text.isNotEmpty() && binding.etRecipeDescription.editText.text.isNotEmpty() &&
                    Integer.parseInt(binding.etRecipeTime.text.toString()) >= 1 && stageAdapter.itemCount > 0 && ingredientAdapter.itemCount > 0
    }

    private fun requestIngredient(){
        launchWithLifecycle(lifecycle) {
            uploadViewModel.getIngredients()
            uploadViewModel.ingredientsResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data.let { data ->
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

    private fun uploadImageURI(imageFile:String){
        launchWithLifecycle(lifecycle) {
            val imgfile = File(imageFile)
            val file = MultipartBody.Part.createFormData(name = "multipartFile", filename = imgfile.name, body = imgfile.asRequestBody("image/jpg".toMediaType()))
            uploadViewModel.uploadImage(file)
            uploadViewModel.uploadImageResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        if(it.data.code == "SUCCESS"){
                            requestUpload()
                        }
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun moveMainPage(){
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra("source", "uploadRecipe")
        startActivity(intent)

        activity?.finish()
    }

    private fun responseStepImage(origin:String,url:String){
        if(url.isEmpty()){
            showToastMessage("이미지 업로드에 실패했습니다")
        }else{
            thumbnailUri = url
            binding.ivThumbnail.loadImagesWithGlideRound(origin,10)
        }
    }

    private fun initAdapter(dataList:List<Ingredients>){
        ingredientAdapter = IngredientAdapter()
        ingredientAdapter.setActionListener(object :IngredientItemHolder.actionListener{
            override fun remove(name: Ingredients) {
                ingredientAdapter.removeItem(name)
            }
        })

        binding.rvCompleteIngredient.adapter = ingredientAdapter
        binding.rvCompleteIngredient.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)

        val adapter = IngredientListAdapter(requireContext(),dataList)
        binding.etRecipeIngredient.setAdapter(adapter)
        binding.etRecipeIngredient.setOnItemClickListener { _, v, position, _ ->
            val data = adapter.getItem(position)
            viewDialog(data)
        }
    }

    private fun viewDialog(item:Ingredients){
        if(item.ingredient_unit == "개") {
            val dialog = IngredientCountDialog(item.ingredient_name)
            dialog.setListener(object : IngredientCountDialog.onCountListener{
                override fun getCount(count: Int) {
                    item.ingredient_count = count
                    dialogBinding(item)
                }

            })
            dialog.show(parentFragmentManager,"IngredientCountDialog")
        }else{
            val dialog = IngredientEtcDialog(item.ingredient_name,item.ingredient_unit)
            dialog.setListener(object :IngredientEtcDialog.onCountListener{
                override fun getCount(count: Int) {
                    item.ingredient_count = count
                    dialogBinding(item)
                }
            })
            dialog.show(parentFragmentManager,"IngredientEtcDialog")
        }
    }

    private fun dialogBinding(item:Ingredients){
        ingredientAdapter.addItem(item)
        binding.etRecipeIngredient.setText("")
    }


    private val imageResultSingle = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            result?.data?.let { it ->
                imageString= requireActivity().getRealPathFromURI(it.data!!)
                // thumbnailFile = File(imageString)
                binding.ibImage.loadImagesWithGlideRound(imageString!!,10)
                // 이미지 업로드
                Log.d("thumbnail","${thumbnailUri}")
            }
        }
    }

    private val imageThumbnailResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            result?.data?.let { it ->
                val image = requireActivity().getRealPathFromURI(it.data!!)
                binding.ivThumbnail.setPadding(0, 0, 0, 0)
                // 이미지 업로드
                thumbnailUri = image
                binding.ivThumbnail.loadImagesWithGlideRound(image,10)
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