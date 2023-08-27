package com.cmc.recipe.presentation.ui.mypage

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.FragmentSaveRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.common.CustomBottomSheetFragment
import com.cmc.recipe.presentation.ui.common.RecipeSnackBar
import com.cmc.recipe.presentation.ui.recipe.RecipeItemHolder
import com.cmc.recipe.presentation.ui.recipe.RecipeListAdapter
import com.cmc.recipe.presentation.viewmodel.MyPageViewModel
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveRecipeFragment : BaseFragment<FragmentSaveRecipeBinding>(FragmentSaveRecipeBinding::inflate) {

    private val myPageViewModel : MyPageViewModel by viewModels()
    private val recipeViewModel : RecipeViewModel by viewModels()

    private lateinit var adapter : RecipeListAdapter

    override fun initFragment() {

        requestSaveRecipe()
    }

    private fun requestSaveRecipe(){
        launchWithLifecycle(lifecycle) {
            myPageViewModel.getSaveRecipe()
            myPageViewModel.saveRecipeResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data.let { data ->
                            if(data.code == "SUCCESS"){
                                recipeRecyclerview(it.data.data as ArrayList<RecipeItem>)
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        myPageViewModel._saveRecipeResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        myPageViewModel._saveRecipeResult.value  = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    private fun requestUnSave(item:RecipeItem){
        recipeViewModel.postRecipesNotSave(item.recipe_id)
        launchWithLifecycle(lifecycle) {
            recipeViewModel.recipeSaveResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                adapter.removeItem(item)
                                RecipeSnackBar(binding.rvRecipe,"저장이 해제됐습니다!").show()
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

    private fun recipeRecyclerview(itemList:ArrayList<RecipeItem>){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                // 페이지 이동 구현
            }
        }

        adapter = RecipeListAdapter(clickListener)
        adapter.setListener(object :RecipeItemHolder.onActionListener{
            override fun action(item: RecipeItem) {
                requestUnSave(item)
            }
        })

        binding.rvRecipe.adapter = adapter
        binding.rvRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }
}