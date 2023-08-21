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
import com.cmc.recipe.presentation.ui.common.RecipeSnackBar
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
    private lateinit var itemList:List<RecipeItem>

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

    private fun recipeRecyclerview(){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                movePage(R.id.action_recipeMainFragment_to_recipeActivity)
            }
        }

        val adapter = RecipeListAdapter(clickListener)
        adapter.setListener(object :RecipeItemHolder.onActionListener{
            override fun action(item: RecipeItem) {
                requestRecipeSave()
            }

        })
        binding.rvRecipe.adapter = adapter
        binding.rvRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)


        binding.btnNewest.setOnClickListener {
            itemList.sortedByDescending { it.created_date }
            adapter.replaceData(itemList)
        }

        binding.btnPopular.setOnClickListener {
            itemList.sortedByDescending { it.rating }
            adapter.replaceData(itemList)
        }

        binding.btnMiniumTime.setOnClickListener {
            // TODO : 조리시간 컬럼 추가 후 수정예정
          //  itemList.sortedBy {  }
          //  adapter.replaceData(itemList)
        }
    }

    private fun requestRecipeSave(){
        launchWithLifecycle(lifecycle) {
            val accessToken = MainApplication.tokenManager.getAccessToken()
            recipeViewModel.postRecipesSave(accessToken,1)
            recipeViewModel._recipeSaveResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                RecipeSnackBar(binding.rvRecipe,"레시피가 저장되었습니다!").show()
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

}

