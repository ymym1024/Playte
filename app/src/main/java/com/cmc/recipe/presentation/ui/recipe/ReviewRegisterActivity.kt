package com.cmc.recipe.presentation.ui.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.cmc.recipe.R
import com.cmc.recipe.databinding.ActivityRecipeBinding
import com.cmc.recipe.databinding.ActivityReviewRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewRegisterBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding.toolbarTitle.text = "리뷰 작성"

        // id 전달받기
        val args = ReviewRegisterActivityArgs.fromBundle(intent.extras!!)

        // 초기 화면에서 리뷰 화면 추가
        if (savedInstanceState == null) {
            val initialFragment = RecipeReviewFragment()
            initialFragment?.arguments = Bundle().apply {
                putInt("recipeId", args.recipeId)
                putString("recipeImg", args.recipeImg)
            }
            supportFragmentManager.commit {
                replace(R.id.fragmentContainerView, initialFragment)
            }
        }


    }

}