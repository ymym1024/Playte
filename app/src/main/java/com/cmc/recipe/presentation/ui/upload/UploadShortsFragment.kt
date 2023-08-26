package com.cmc.recipe.presentation.ui.upload

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.Ingredient
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.data.model.response.Ingredients
import com.cmc.recipe.databinding.FragmentUploadShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.viewmodel.UploadViewModel
import com.cmc.recipe.utils.Constant.PICK_VIDEO_REQUEST
import com.cmc.recipe.utils.NetworkState
import com.cmc.recipe.utils.bitmapImagesWithGlideRound
import com.cmc.recipe.utils.convertLongToTime
import com.cmc.recipe.utils.loadImagesWithGlideRound
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class UploadShortsFragment : BaseFragment<FragmentUploadShortsBinding>(FragmentUploadShortsBinding::inflate) {

    private val uploadViewModel : UploadViewModel by viewModels()
    private lateinit var videoUploadUri: String

    override fun onDestroyView() {
        super.onDestroyView()
        val activity = activity as UploadActivity
        activity.clearToolbarAndIcon()
    }

    override fun initFragment() {
        initMenu()
        editScroll()
        uploadShorts()
        requestIngredient()
    }


    private fun requestIngredient(){
        viewLifecycleOwner.lifecycleScope.launch {
            uploadViewModel.getIngredients()
            uploadViewModel.ingredientsResult
                .take(1)
                .onEach{
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
                        }
                        is NetworkState.Error -> {
                            showToastMessage(it.message.toString())
                        }
                        else -> {}
                    }
                }
                .launchIn(this)
        }
    }

    private fun initAdapter(dataList:List<Ingredients>){
        val ingredientAdapter = IngredientAdapter()
        ingredientAdapter.setActionListener(object :IngredientItemHolder.actionListener{
            override fun remove(name: Ingredients) {
                ingredientAdapter.removeItem(name)
                binding.tvIngredientCnt.text = "${ingredientAdapter.itemCount}"
                if(ingredientAdapter.itemCount <= 4) binding.etRecipeIngredient.isEnabled = true
            }
        })
        binding.rvCompleteIngredient.adapter = ingredientAdapter
        binding.rvCompleteIngredient.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)

        val adapter = IngredientListAdapter(requireContext(),dataList)
        binding.etRecipeIngredient.setAdapter(adapter)
        binding.etRecipeIngredient.setOnItemClickListener { _, v, position, _ ->
            val selectedData = adapter.getItem(position)
            ingredientAdapter.addItem(selectedData)
            binding.tvIngredientCnt.text = "${ingredientAdapter.itemCount}"
            binding.etRecipeIngredient.setText("")
            if(ingredientAdapter.itemCount == 4) binding.etRecipeIngredient.isEnabled = false
            hideKeyboard(v)
        }
    }

    private fun editScroll(){
        binding.etRecipeIngredient.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.scrollView.post {
                    binding.scrollView.scrollTo(0, binding.etRecipeIngredient.bottom)
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openVideoPicker()
        }
    }

    private val requestMediaPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.READ_MEDIA_IMAGES] == true &&
            permissions[Manifest.permission.READ_MEDIA_VIDEO] == true &&
            permissions[Manifest.permission.READ_MEDIA_AUDIO] == true
        ) {
            openVideoPicker()
        }
    }

    private fun checkPermissionsAndPickVideo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMediaPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
                )
            )
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openVideoPicker()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openVideoPicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/mp4"
        startActivityForResult(intent, PICK_VIDEO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { videoUri ->
                videoUploadUri = getFileName(videoUri,requireContext())
                val time = getVideoDuration(videoUri)

                if(time>= 60000L){ // 영상 1분 이내가 아닐 경우 업로드 불가능
                    showToastMessage("1분 이내의 영상만 업로드 가능합니다")
                }else{
                    val bitmap = getFirstFrameFromVideo(videoUri)
                    binding.ivThumbnail.setPadding(0, 0, 0, 0)
                    binding.ivThumbnail.bitmapImagesWithGlideRound(bitmap,10)
                    binding.btnUploadShorts.visibility = View.VISIBLE
                    settingToolbar(time)
                }
            }
        }
    }

    private fun getFileName(uri: Uri, context: Context): String {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        return ""
    }

    fun settingToolbar(time : Long){
        val activity = activity as UploadActivity
        val iconDrawable: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.ic_time)
        val drawablePadding = 6.dp

        activity.setToolbarAndIcon(iconDrawable!!,time.convertLongToTime(time),drawablePadding)
        activity.setToolbarColor()
    }

    val Int.dp: Int
        get() = (this * resources.displayMetrics.density + 0.5f).toInt()

    private fun getFirstFrameFromVideo(videoUri: Uri): Bitmap? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(requireContext(), videoUri)
            return retriever.getFrameAtTime(0)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return null
    }

    private fun getVideoDuration(videoUri: Uri): Long {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, videoUri)
            val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            return durationStr?.toLongOrNull() ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return 0L
    }

    private fun initMenu(){
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_shorts_register, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_edit_button -> {
                        requestUploadVideo()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun requestUploadVideo(){
        viewLifecycleOwner.lifecycleScope.launch {
            Log.d("videoUpload","${videoUploadUri}")
            val img_file = File(videoUploadUri)
            val file = MultipartBody.Part.createFormData(name = "multipartFile", filename = img_file.name, body = img_file.asRequestBody("video/mp4".toMediaType()))
            uploadViewModel.uploadVideo(file)
            uploadViewModel.uploadVideoResult.take(1).onEach{
                when(it) {
                    is NetworkState.Success -> {
                        if (it.data.code == "SUCCESS") {
                            Log.d("여기 호출", "${it.data}")

                            if (it.data.data.toString().isEmpty()) {
                                showToastMessage("이미지 업로드에 실패했습니다")
                            } else {
                                requestShorts(it.data.data.toString())

                            }
                        }
                    }
                    is NetworkState.Error -> {
                        Log.d("error","${it.message}")
                        showToastMessage(it.message.toString())
                    }
                    else -> {
                        // progress bar 띄우기
                    }
                }
            }
            .launchIn(this)
        }
    }

    private fun requestShorts(shorts_url:String){

    }

    private fun uploadShorts(){
        binding.ivThumbnail.setOnClickListener {
            checkPermissionsAndPickVideo()
        }

        binding.btnUploadShorts.setOnClickListener {
            checkPermissionsAndPickVideo()
        }
    }

}