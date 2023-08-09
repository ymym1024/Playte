package com.cmc.recipe.presentation.ui

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cmc.recipe.R
import com.cmc.recipe.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navSetting()
    }

    private fun navSetting(){
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener {
                _, destination, _ ->

            binding.toolbarTitle.text = ""
            when(destination.id){
                R.id.recipeMainFragment -> {
                    binding.toolbarLogo.visibility = View.VISIBLE
                    binding.toolbarLogo.setImageResource(R.drawable.img_recipe_logo)
                }
                R.id.shortsFragment -> {
                    binding.toolbarLogo.visibility = View.VISIBLE
                    binding.toolbarLogo.setImageResource(R.drawable.img_shorts_logo)
                }
                R.id.mypageFragment -> {
                    binding.toolbarLogo.visibility = View.VISIBLE
                    binding.toolbarLogo.setImageResource(R.drawable.img_mypage_logo)
                }
                else -> {
                    binding.toolbarLogo.visibility = View.GONE
                    binding.toolbarTitle.text = navController.currentDestination?.label.toString()
                }
            }
        }

        val graph = navController.navInflater.inflate(R.navigation.nav_graph)

        navController.graph = graph

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.recipeMainFragment,R.id.shortsFragment,R.id.uploadFragment,R.id.mypageFragment))
        setupActionBarWithNavController(navController,appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.itemIconTintList = null
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // toolbar show flag
    fun hideToolbar(state:Boolean){
        if(state){
            binding.toolbar.visibility = View.GONE
        }else{
            binding.toolbar.visibility = View.VISIBLE
        }
    }

    fun setToolbarAndIcon(icon : Drawable, string: String,padding:Int){
        binding.toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
        binding.toolbarTitle.text = string
        binding.toolbarTitle.compoundDrawablePadding = padding
    }

    fun clearToolbarAndIcon(){
        binding.toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
    }

    // bottomnavigation show flag
    fun hideBottomNavigation(state:Boolean){
        if(state){
            binding.bottomNav.visibility = View.GONE
        }else{
            binding.bottomNav.visibility = View.VISIBLE
        }
    }
}