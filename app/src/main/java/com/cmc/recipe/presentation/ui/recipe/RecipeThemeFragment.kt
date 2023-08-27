package com.cmc.recipe.presentation.ui.recipe

import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.MainApplication
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.FragmentRecipeThemeBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.NetworkState
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeThemeFragment : BaseFragment<FragmentRecipeThemeBinding>(FragmentRecipeThemeBinding::inflate) {

    private val recipeViewModel : RecipeViewModel by viewModels()
    private lateinit var itemList:List<RecipeItem>

    override fun initFragment() {
        initTitle()
        requestRecipeList()

    }

    private fun initTitle(){
        val theme = arguments?.getString("theme")

        val activity = activity as RecipeActivity
        activity.setToolbarTitle("$theme")
    }

    private fun requestRecipeList(){
        launchWithLifecycle(lifecycle) {
            recipeViewModel.getRecipes()
            recipeViewModel.recipeResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data.let { data ->
                            if(data.code == "SUCCESS"){
                                itemList = it.data.data.content
                                recipeRecyclerview()
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        recipeViewModel._recipeResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        recipeViewModel._recipeResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    private fun recipeRecyclerview() {
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                //movePage(R.id.action_recipeMainFragment_to_recipeActivity)
            }
        }

        val adapter = RecipeListAdapter(clickListener)
        adapter.setListener(object : RecipeItemHolder.onActionListener {
            override fun action(item: RecipeItem) {

            }

        })
        binding.rvRecipe.adapter = adapter
        binding.rvRecipe.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)

        binding.chipRecipe.setOnCheckedStateChangeListener { group, checkedIds ->
            when (checkedIds) {
                binding.btnRecipeNewest -> {
                    itemList.sortedByDescending { it.created_date }
                    adapter.replaceData(itemList)
                    binding.btnRecipeNewest.isCheckable = true
                }
                binding.btnRecipePopular -> {
                    itemList.sortedByDescending { it.rating }
                    adapter.replaceData(itemList)
                    binding.btnRecipePopular.isCheckable = true
                }
                binding.btnRecipeMiniumTime -> {
                    binding.btnRecipeMiniumTime.isCheckable = true
                }
            }
        }

    }
}