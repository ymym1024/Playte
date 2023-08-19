package com.cmc.recipe.presentation.ui.recipe

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.MainApplication
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.FragmentRecipeMainBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.search.SearchActivity
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class RecipeMainFragment : BaseFragment<FragmentRecipeMainBinding>(FragmentRecipeMainBinding::inflate) {

    private val recipeViewModel : RecipeViewModel by viewModels()

    override fun initFragment() {

        requestRecipeList()
        searchRecipe()
    }

    private fun requestRecipeList(){
        launchWithLifecycle(lifecycle) {
            val accessToken = MainApplication.tokenManager.getAccessToken()
            recipeViewModel.getRecipes(accessToken)
            recipeViewModel.recipeResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                val itemList = it.data.data.content
                                recipeRecyclerview(itemList)
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

    private fun searchRecipe(){
        binding.searchView.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                val searchText = binding.searchView.text.toString()

                if(searchText.isEmpty()){
                    movePage(Constant.RECIPE, Constant.SEARCH)
                }else{
                    movePage(Constant.RECIPE, Constant.RECIPE)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun movePage(current:String,destination:String){
        val intent = Intent(requireContext(), SearchActivity::class.java)
        intent.putExtra("startDestination", destination)
        intent.putExtra("currentDestination", current)
        startActivity(intent)
    }

    private fun recipeRecyclerview(itemList:List<RecipeItem>){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                movePage(R.id.action_recipeMainFragment_to_recipeActivity)
            }
        }

        val adapter = RecipeListAdapter(clickListener)
        adapter.setListener(object :RecipeItemHolder.onActionListener{
            override fun action(item: RecipeItem) {

            }

        })
        binding.rvRecipe.adapter = adapter
        binding.rvRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }

}

