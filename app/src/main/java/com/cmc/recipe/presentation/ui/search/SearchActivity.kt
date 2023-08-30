package com.cmc.recipe.presentation.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.navigation.NavController
import androidx.navigation.NavOptions
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


        if(keyword?.isNullOrBlank()==null){
            graph.setStartDestination(R.id.searchFragment)
            navController.graph = graph

        } else{
            val bundle = Bundle()
            bundle.putString("keyword", keyword)

            if(startDestination == Constant.RECIPE){
                graph.setStartDestination(R.id.searchRecipeFragment)
                navController.graph = graph
                val navOptions = NavOptions.Builder()
                    .setLaunchSingleTop(true)  // 이미 스택에 해당 프래그먼트가 있다면 재사용
                    .setPopUpTo(R.id.searchRecipeFragment, false) // 백스택에서 해당 프래그먼트 위로 모두 제거
                    .build()
                navController.navigate(R.id.searchRecipeFragment, bundle,navOptions)
            }else if(startDestination == Constant.SHORTS){
                graph.setStartDestination(R.id.searchShortsFragment)
                navController.graph = graph
                val navOptions = NavOptions.Builder()
                    .setLaunchSingleTop(true)  // 이미 스택에 해당 프래그먼트가 있다면 재사용
                    .setPopUpTo(R.id.searchShortsFragment, false) // 백스택에서 해당 프래그먼트 위로 모두 제거
                    .build()
                navController.navigate(R.id.searchShortsFragment, bundle,navOptions)
            }
        }
    }
}