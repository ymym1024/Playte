package com.cmc.recipe.presentation.ui.search

import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.FragmentSearchRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.recipe.RecipeActivity
import com.cmc.recipe.presentation.ui.recipe.RecipeItemHolder
import com.cmc.recipe.presentation.ui.recipe.RecipeListAdapter
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.presentation.viewmodel.SearchViewModel
import com.cmc.recipe.utils.CommonTextWatcher
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SearchRecipeFragment : BaseFragment<FragmentSearchRecipeBinding>(FragmentSearchRecipeBinding::inflate) {

    private val searchViewModel : SearchViewModel by viewModels()
    private lateinit var itemList:List<RecipeItem>

    override fun initFragment() {
        val keyword = arguments?.getString("keyword")
        searchViewModel.insertRecentRecipe(keyword!!) //검색어 저장

        binding.searchView.setText(keyword)
        requestRecipeList(keyword!!)

        binding.searchView.setOnEditorActionListener{ text, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                if(binding.searchView.text.toString().isNotEmpty()){
                    requestRecipeList("${binding.searchView.text}")
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        //뒤로가기 시 activity 삭제
//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    requireActivity().finish()
//                }
//            }
//        )

        //버튼 눌렀을 때 뒤로가기
        binding.btnBack.setOnClickListener {
            requireActivity().finish()
        }
    }


    private fun requestRecipeList(keyword:String){
        searchViewModel.getSearchRecipe(keyword)
        launchWithLifecycle(lifecycle) {
            searchViewModel.recipeResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data.let { data ->
                            if(data.code == "SUCCESS"){
                                itemList = it.data.data.content
                                recipeRecyclerview()
                            }
                        }
                        searchViewModel._recipeResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        searchViewModel._recipeResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    private fun recipeRecyclerview(){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
               // findNavController().navigate(R.id.action_recipeMainFragment_to_recipeDetailFragment)
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

        binding.chipRecipe.setOnCheckedStateChangeListener { group, checkedIds ->
            when (checkedIds[0]) {
                R.id.btn_recipe_newest -> {
                    val newList = itemList.sortedByDescending { it.created_date }
                    adapter.replaceData(newList)
                    binding.btnRecipeNewest.isCheckable = true
                }
                R.id.btn_recipe_popular -> {
                    val newList = itemList.sortedBy { it.rating }
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