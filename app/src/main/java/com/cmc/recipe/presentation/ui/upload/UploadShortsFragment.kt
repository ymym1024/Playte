package com.cmc.recipe.presentation.ui.upload

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.Ingredient
import com.cmc.recipe.databinding.FragmentUploadShortsBinding
import com.cmc.recipe.presentation.MainActivity
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.utils.Constant.PICK_VIDEO_REQUEST
import com.cmc.recipe.utils.Constant.READ_EXTERNAL_STORAGE_REQUEST
import com.cmc.recipe.utils.convertLongToTime

class UploadShortsFragment : BaseFragment<FragmentUploadShortsBinding>(FragmentUploadShortsBinding::inflate) {

    override fun onDestroyView() {
        super.onDestroyView()
        val mainActivity = activity as MainActivity
        mainActivity.clearToolbarAndIcon()
    }

    override fun initFragment() {
        initAdapter()
        editScroll()
        uploadShorts()
    }

    private fun initAdapter(){
        //TODO : mockup data => 네트워크 연결 후 삭제
        val dataList = arrayListOf(
            Ingredient("토마토","재료"),
            Ingredient("토마토 소스","양념"),
            Ingredient("토마토","양념"),
            Ingredient("토마토 소스","양념"),
            Ingredient("토마토","양념"),
            Ingredient("토마토 소스","양념")
        )

        val ingredientAdapter = IngredientAdapter()
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
            val selectedData = adapter.getItem(position)
            ingredientAdapter.addItem(selectedData.name)
            binding.etRecipeIngredient.setText("")
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
                val bitmap = getFirstFrameFromVideo(videoUri)
                binding.ivThumbnail.setImageBitmap(bitmap)
                val time = getVideoDuration(videoUri)
                settingToolbar(time)
            }
        }
    }

    fun settingToolbar(time : Long){
        val mainActivity = activity as MainActivity
        val iconDrawable: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.ic_time)
        val drawablePadding = 6.dp

        mainActivity.setToolbarAndIcon(iconDrawable!!,time.convertLongToTime(time),drawablePadding)
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

    private fun uploadShorts(){
        binding.btnUploadShorts.setOnClickListener {
            checkPermissionsAndPickVideo()
        }
    }

}