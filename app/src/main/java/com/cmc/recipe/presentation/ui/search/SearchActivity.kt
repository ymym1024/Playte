package com.cmc.recipe.presentation.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.cmc.recipe.R
import com.cmc.recipe.databinding.ActivitySearchBinding
import com.cmc.recipe.presentation.ui.base.BaseActivity
import com.cmc.recipe.utils.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>({ ActivitySearchBinding.inflate(it)}){
    private lateinit var navController: NavController
    private lateinit var prevDestination : String
    override fun initView() {
        navSetting()
    }

    fun getPrevDestation():String{
        return prevDestination
    }

    private fun navSetting(){
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val graph = navController.navInflater.inflate(R.navigation.nav_search)
        val startDestination = intent.getStringExtra("startDestination")
        prevDestination = intent.getStringExtra("currentDestination")!!
        val keyword = intent.getStringExtra("keyword")

        if(keyword?.isNullOrBlank() == true){
            graph.setStartDestination(R.id.searchFragment)
            navController.graph = graph

        } else{
            val bundle = Bundle()
            bundle.putString("keyword", keyword)

            if(startDestination == Constant.RECIPE){
                graph.setStartDestination(R.id.searchRecipeFragment)
                navController.graph = graph
                navController.navigate(R.id.searchRecipeFragment,bundle)
            }else if(startDestination == Constant.SHORTS){
                graph.setStartDestination(R.id.searchShortsFragment)
                navController.graph = graph
                navController.navigate(R.id.searchShortsFragment,bundle)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}