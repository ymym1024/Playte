package com.cmc.recipe.presentation.ui.recipe

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.databinding.FragmentRecipeReviewBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.presentation.viewmodel.UploadViewModel
import com.cmc.recipe.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class RecipeReviewFragment : BaseFragment<FragmentRecipeReviewBinding>(FragmentRecipeReviewBinding::inflate) {

    private val recipeViewModel : RecipeViewModel by viewModels()
    private val uploadViewModel : UploadViewModel by viewModels()

    private lateinit var adapter: ReviewImageAdapter
    private var imageCount:Int = 0

    private var imageUploadList :MutableList<String> = emptyList<String>().toMutableList()
    private var recipeId: Int = 0

    override fun initFragment() {
        var recipeImg = ""

        arguments?.let {
            recipeId = it.getInt("recipeId", -1)
            recipeImg = it.getString("recipeImg", "")
        }

        initView(recipeImg)
        getRatingScore()
        initRV()

        addImageButton()
        saveRegisterButton()
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
        binding.btnRegister.isEnabled = false
        binding.etReview.editText.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->
                text?.let {
                    binding.btnRegister.isEnabled = !text.isEmpty()
                }
            }
        ))
        binding.btnImageAdd.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            imageResultSingle.launch(intent)
        }
    }

    private fun saveRegisterButton(){
        binding.btnRegister.setOnClickListener {
            if(adapter.getData().size >= 1) uploadImages(adapter.getData())
            else requestReviewRegister(recipeId)
        }
    }

    private fun uploadImages(imageFileList: List<String>) {
        val imageFileIterator = imageFileList.iterator()

        viewLifecycleOwner.lifecycleScope.launch {
            while (imageFileIterator.hasNext()) {
                val imageFile = imageFileIterator.next()
                val imgfile = File(imageFile)
                val file = MultipartBody.Part.createFormData(name = "multipartFile", filename = imgfile.name, body = imgfile.asRequestBody("image/jpg".toMediaType()))

                uploadViewModel.uploadImage(file)
                uploadViewModel.uploadImageResult.collect {
                    when (it) {
                        is NetworkState.Success -> {
                            if (it.data.code == "SUCCESS") {
                                imageUploadList.add(it.data.data.toString())
                                if(!imageFileIterator.hasNext()){
                                    requestReviewRegister(recipeId)
                                }
                            }
                        }
                        is NetworkState.Error -> {
                            showToastMessage(it.message.toString())
                        }
                        else -> {}
                    }
                }
            }
        }
    }


    private fun requestReviewRegister(id:Int){
        val request = ReviewRequest(review_content = binding.etReview.getText().toString(), review_imgs = imageUploadList, review_rating = binding.ratingBar.rating.toInt())
        recipeViewModel.postRecipesReview(id,request)
        lifecycleScope.launch {
            recipeViewModel._reviewAddResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data.let { data ->
                            if(data.code == "SUCCESS"){
                                Toast.makeText(requireContext(),"리뷰가 등록되었습니다!",Toast.LENGTH_SHORT).show()
                                requireActivity().finish()
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        recipeViewModel._recipeResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                        recipeViewModel._recipeResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

}