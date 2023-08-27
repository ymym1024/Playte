package com.cmc.recipe.presentation.ui.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.cmc.recipe.R
import com.cmc.recipe.databinding.ActivityMypageBinding
import com.cmc.recipe.databinding.ActivityRecipeBinding
import com.cmc.recipe.presentation.ui.recipe.RecipeActivityArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMypageBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navSetting()

    }

    private fun navSetting(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener {
                _, destination, _ ->
            binding.toolbarTitle.text = navController.currentDestination?.label.toString()
        }

        val graph = navController.navInflater.inflate(R.navigation.nav_mypage_graph)
        val args: MypageActivityArgs = MypageActivityArgs.fromBundle(intent.extras!!)

        Log.d("destination","${args.destination}")

        if (args.destination=="myReviewFragment") {
            graph.setStartDestination(R.id.myReviewFragment)
            //navController.navigate(R.id.recipeThemeFragment, bundle, navOptions)
        }else if(args.destination=="saveRecipeFragment"){
            graph.setStartDestination(R.id.saveRecipeFragment)
        }else if(args.destination=="settingFragment"){
            graph.setStartDestination(R.id.settingFragment)
        } else {
            graph.setStartDestination(R.id.writeRecipeFragment)
        }
        navController.graph = graph
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}