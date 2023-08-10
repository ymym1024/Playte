package com.cmc.recipe.presentation.ui.recipe

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmc.recipe.R
import com.cmc.recipe.databinding.ActivityImageReviewBinding
import com.cmc.recipe.databinding.ActivityRecipeBinding
import com.cmc.recipe.presentation.ui.common.ImageAdapter

class ImageReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImageReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        imageRV()
    }

    private fun imageRV(){
        val imageList = arrayListOf(
            "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",
            "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",
            "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",
            "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",
            "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",
        )

        val imageAdapter = ImageAdapter(null)
        binding.rvGridImage.run {
            adapter = imageAdapter
            addItemDecoration(GridSpaceItemDecoration(3, 5))
          //  layoutManager = GridLayoutManager(context,3)
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