package com.cmc.recipe.presentation.ui.recipe

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.FragmentRecipeThemeBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeThemeFragment : BaseFragment<FragmentRecipeThemeBinding>(FragmentRecipeThemeBinding::inflate) {

    private val recipeViewModel : RecipeViewModel by viewModels()
    private lateinit var itemList:List<RecipeItem>


    override fun initFragment() {
        var theme = arguments?.getString("theme")
        var theme_name = ""

        if(theme!!.equals(getString(R.string.recipe_theme_1))){
            theme_name = "BudgetHappiness"
        }else if(theme!!.equals(getString(R.string.recipe_theme_2))){
            theme_name = "ForDieting"
        }else if(theme!!.equals(getString(R.string.recipe_theme_3))){
            theme_name = "Housewarming"
        }else{
            theme_name = "LivingAlone"
        }

        initTitle(theme)
        requestRecipeList(theme_name)
    }

    private fun initTitle(theme: String?) {
        val activity = activity as RecipeActivity
        activity.setToolbarTitle("$theme")
    }

    private fun requestRecipeList(theme: String?) {
        launchWithLifecycle(lifecycle) {
            recipeViewModel.getRecipeTheme(theme!!)
            recipeViewModel.recipeThemeResult.collect{
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
            when (checkedIds[0]) {
                R.id.btn_recipe_newest -> {
                    val newList = itemList.sortedByDescending { it.created_date }
                    adapter.replaceData(newList)
                    binding.btnRecipeNewest.isCheckable = true
                }
                R.id.btn_recipe_popular -> {
                    val newList = itemList.sortedByDescending { it.rating }
                    adapter.replaceData(newList)
                    binding.btnRecipePopular.isCheckable = true
                }
                R.id.btn_recipe_minium_time -> {
                    val newList = itemList.sortedBy { it.cook_time }
                    adapter.replaceData(newList)
                    binding.btnRecipeMiniumTime.isCheckable = true
                }
            }
        }

    }
}