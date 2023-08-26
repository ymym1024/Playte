package com.cmc.recipe.presentation.ui.recipe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.cmc.recipe.R
import com.cmc.recipe.databinding.ActivityRecipeBinding
import com.cmc.recipe.presentation.ui.common.RecipeSnackBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    private lateinit var navController: NavController
    private var id:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id",0)
        }

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

        val graph = navController.navInflater.inflate(R.navigation.nav_recipe_graph)
        val args: RecipeActivityArgs = RecipeActivityArgs.fromBundle(intent.extras!!)

        if (args.theme?.isNotBlank() == true) {
            val bundle = Bundle()
            bundle.putString("theme", args.theme)

            graph.setStartDestination(R.id.recipeThemeFragment)
            navController.graph = graph
            navController.navigate(R.id.recipeThemeFragment,bundle)
        } else {
            graph.setStartDestination(R.id.recipeDetailFragment)
            navController.graph = graph

            val bundle = Bundle()
            bundle.putInt("id", id)

            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)  // 이미 스택에 해당 프래그먼트가 있다면 재사용
                .setPopUpTo(R.id.recipeDetailFragment, false) // 백스택에서 해당 프래그먼트 위로 모두 제거
                .build()

            navController.navigate(R.id.recipeDetailFragment, bundle, navOptions)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun hideToolbar(state:Boolean){
        if(state){
            binding.toolbar.visibility = View.GONE
        }else{
            binding.toolbar.visibility = View.VISIBLE
        }
    }

    fun setToolbarTitle(title:String){
        binding.toolbarTitle.text = title
    }
}