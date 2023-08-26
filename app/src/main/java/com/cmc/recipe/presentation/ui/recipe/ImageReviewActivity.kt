package com.cmc.recipe.presentation.ui.recipe

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmc.recipe.R
import com.cmc.recipe.databinding.ActivityImageReviewBinding
import com.cmc.recipe.databinding.ActivityRecipeBinding
import com.cmc.recipe.presentation.ui.common.ImageAdapter
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImageReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageReviewBinding
    private val recipeViewModel : RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImageReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // intent
        val id = intent.getIntExtra("recipeId",0)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        requestReviewImage(id)

    }
    private fun requestReviewImage(id:Int){
        recipeViewModel.getRecipesReviewPhotos(id)
        lifecycleScope.launch {
            recipeViewModel.reviewPhotosResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                imageRV(data.data)
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        recipeViewModel._recipeResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        Toast.makeText(this@ImageReviewActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                        recipeViewModel._recipeResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }

    }

    private fun imageRV(imageList: List<String>){
        val imageAdapter = ImageAdapter(null)
        binding.rvGridImage.run {
            adapter = imageAdapter
            addItemDecoration(GridSpaceItemDecoration(3, 5))
        }
        imageAdapter.replaceData(imageList)
    }

    class GridSpaceItemDecoration(private val spanCount: Int, private val space: Int): RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount + 1      // 1부터 시작

            if (position >= spanCount){
                outRect.top = space
            }

            if (column != 1){
                outRect.left = space
            }

        }

    }
}