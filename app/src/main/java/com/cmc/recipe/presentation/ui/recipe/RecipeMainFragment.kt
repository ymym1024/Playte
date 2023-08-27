package com.cmc.recipe.presentation.ui.recipe

import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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


@AndroidEntryPoint
class RecipeMainFragment : BaseFragment<FragmentRecipeMainBinding>(FragmentRecipeMainBinding::inflate) {

    private val recipeViewModel : RecipeViewModel by viewModels()
    private lateinit var itemList:List<RecipeItem>


    override fun initFragment() {
        initEventBinding()
        requestRecipeList()
        searchRecipe()
    }

    private fun initEventBinding(){
        binding.apply {
            btnTheme1.setOnClickListener {
                moveThemePage(getString(R.string.recipe_theme_1))
            }
            btnTheme2.setOnClickListener {
                moveThemePage(getString(R.string.recipe_theme_2))
            }
            btnTheme3.setOnClickListener {
                moveThemePage(getString(R.string.recipe_theme_3))
            }
            btnTheme4.setOnClickListener {
                moveThemePage(getString(R.string.recipe_theme_4))
            }
        }
    }

    private fun moveThemePage(theme:String){
        val action = RecipeMainFragmentDirections.actionRecipeMainFragmentToRecipeActivity(theme = theme)
        findNavController().navigate(action)
    }

    private fun requestRecipeList(){
        launchWithLifecycle(lifecycle) {
            recipeViewModel.getRecipes()
            recipeViewModel.recipeResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                itemList = it.data.data.content
                                Log.d("data","${data.data}")
                                recipeRecyclerview()
                            }else{
                                Log.d("data-err","${data.data}")
                            }
                        }
                        recipeViewModel._recipeResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        Log.d("data-err","${it.message}")
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
                    movePage(Constant.RECIPE, Constant.SEARCH,null)
                }else{
                    movePage(Constant.RECIPE, Constant.RECIPE,searchText)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun movePage(current:String,destination:String,keyword:String?){
        binding.searchView.setText("")
        val intent = Intent(requireContext(), SearchActivity::class.java)
        intent.putExtra("startDestination", destination)
        intent.putExtra("currentDestination", current)
        intent.putExtra("keyword", keyword)
        startActivity(intent)
    }

    private fun moveDetailPage(id:Int){
        val intent = Intent(requireContext(), RecipeActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    private fun recipeRecyclerview(){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                moveDetailPage(id)
            }
        }

        val adapter = RecipeListAdapter(clickListener)
        adapter.setListener(object :RecipeItemHolder.onActionListener{
            override fun action(item: RecipeItem) {
                requestRecipeSave(item.recipe_id)
            }

        })
        binding.rvRecipe.adapter = adapter
        binding.rvRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)

        binding.chipRecipe.setOnCheckedStateChangeListener { group, checkedIds ->
            when (checkedIds) {
                binding.btnNewest -> {
                    itemList.sortedByDescending { it.created_date }
                    adapter.replaceData(itemList)
                    binding.btnNewest.isCheckable = true
                }
                binding.btnPopular -> {
                    itemList.sortedByDescending { it.rating }
                    adapter.replaceData(itemList)
                    binding.btnPopular.isCheckable = true
                }
                binding.btnMiniumTime -> {
                    binding.btnPopular.isCheckable = true
                }
            }
        }
    }

    private fun requestRecipeSave(recipeId: Int) {
        recipeViewModel.postRecipesSave(recipeId)
        launchWithLifecycle(lifecycle) {
            recipeViewModel.recipeSaveResult.collect{
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

