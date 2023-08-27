package com.cmc.recipe.presentation.ui.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.RecipeMapper.toRecipe
import com.cmc.recipe.data.model.response.RecommendationRecipe
import com.cmc.recipe.databinding.FragmentSearchBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.recipe.RecipeActivity
import com.cmc.recipe.presentation.ui.recipe.RecipeItemHolder
import com.cmc.recipe.presentation.ui.recipe.RecipeListAdapter
import com.cmc.recipe.presentation.viewmodel.SearchViewModel
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val searchViewModel : SearchViewModel by viewModels()
    private lateinit var destation : String

    override fun initFragment() {
        val activity = activity as SearchActivity

        destation = activity.getPrevDestation()

        initRV()
    }

    private fun initRV(){
        val adapter = SearchItemAdapter()
        //최신 검색어
        launchWithLifecycle(lifecycle){
            searchViewModel.loadRecentSearch()
            var itemList = mutableListOf<String>()
            searchViewModel.recentKeywordResult.collect{ list ->
                for(item in list) itemList.add(item.keyword)
                Log.d("recentRecipeResult--2","${itemList}")
                binding.rvRecent.adapter = adapter
                binding.rvRecent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                adapter.replaceData(itemList)
            }
        }

        requestKeywordList()

        moveSearch()
    }

    //인기 검색어
    private fun rvPopularBinding(item:List<String>){
        val popAdapter = SearchPopularItemAdapter()
        binding.rvPopular.adapter = popAdapter
        binding.rvPopular.layoutManager = GridLayoutManager(context,2)
        popAdapter.replaceData(item)
    }

    private fun requestKeywordList(){
        launchWithLifecycle(lifecycle) {
            searchViewModel.getSearchKeywords()
            searchViewModel.keywordResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data.let { data ->
                            if(data.code == "SUCCESS"){
                                rvPopularBinding(it.data.data)
                            }else{
                                Log.d("data","${data.data}")
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

    private fun moveSearch(){
        binding.searchView.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                val bundle = Bundle()
                bundle.putString("keyword", "${binding.searchView.text}")

                if(destation==Constant.SHORTS){
                    findNavController().navigate(R.id.action_searchFragment_to_searchShortsFragment,bundle)
                }else{
                    findNavController().navigate(R.id.action_searchFragment_to_searchRecipeFragment,bundle)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

}