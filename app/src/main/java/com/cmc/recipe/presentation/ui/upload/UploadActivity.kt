package com.cmc.recipe.presentation.ui.upload

import android.graphics.drawable.Drawable
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.cmc.recipe.R
import com.cmc.recipe.databinding.ActivityUploadBinding
import com.cmc.recipe.presentation.ui.base.BaseActivity

class UploadActivity : BaseActivity<ActivityUploadBinding>({ ActivityUploadBinding.inflate(it)}){
    private lateinit var navController: NavController

    override fun initView() {
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

        val graph = navController.navInflater.inflate(R.navigation.nav_upload_graph)
        val startDestination = intent.getStringExtra("startDestination")
        if(startDestination == "recipe"){
            graph.setStartDestination(R.id.uploadRecipeFragment)
        }else{
            graph.setStartDestination(R.id.uploadShortsFragment)
        }
        navController.graph = graph
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    fun setToolbarAndIcon(icon : Drawable, string: String, padding:Int){
        binding.toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
        binding.toolbarTitle.text = string
        binding.toolbarTitle.compoundDrawablePadding = padding
    }

    fun clearToolbarAndIcon(){
        binding.toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
    }
}