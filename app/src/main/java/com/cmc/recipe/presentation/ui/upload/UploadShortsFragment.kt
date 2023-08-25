package com.cmc.recipe.presentation.ui.upload

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.Ingredient
import com.cmc.recipe.data.model.response.Ingredients
import com.cmc.recipe.databinding.FragmentUploadShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.viewmodel.UploadViewModel
import com.cmc.recipe.utils.Constant.PICK_VIDEO_REQUEST
import com.cmc.recipe.utils.NetworkState
import com.cmc.recipe.utils.convertLongToTime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadShortsFragment : BaseFragment<FragmentUploadShortsBinding>(FragmentUploadShortsBinding::inflate) {

    private val uploadViewModel : UploadViewModel by viewModels()

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
            ingredientAdapter.addItem(selectedData.ingredient_name)
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
        val activity = activity as UploadActivity
        val iconDrawable: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.ic_time)
        val drawablePadding = 6.dp

        activity.setToolbarAndIcon(iconDrawable!!,time.convertLongToTime(time),drawablePadding)
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
                        Log.d("완료","test")
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun uploadShorts(){
        binding.btnUploadShorts.setOnClickListener {
            checkPermissionsAndPickVideo()
        }
    }

}