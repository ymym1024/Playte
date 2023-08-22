package com.cmc.recipe.presentation.ui.search

import android.util.Log
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
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRecipeFragment : BaseFragment<FragmentSearchRecipeBinding>(FragmentSearchRecipeBinding::inflate) {

    private val searchViewModel : SearchViewModel by viewModels()
    private lateinit var itemList:List<RecipeItem>

    override fun initFragment() {
        initSearch()
        requestRecipeList("test")

        //뒤로가기 시 activity 삭제
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        )
    }

    private fun initSearch(){


    }

    private fun requestRecipeList(keyword:String){
        launchWithLifecycle(lifecycle) {
            searchViewModel.getSearchRecipe(keyword)
            searchViewModel.recipeResult.collect{
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