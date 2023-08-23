package com.cmc.recipe.presentation.ui.search

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.SearchShorts
import com.cmc.recipe.data.model.response.ShortsContent
import com.cmc.recipe.data.model.response.ShortsData
import com.cmc.recipe.databinding.FragmentSearchShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.viewmodel.SearchViewModel
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class SearchShortsFragment : BaseFragment<FragmentSearchShortsBinding>(FragmentSearchShortsBinding::inflate) {

    private val searchViewModel : SearchViewModel by viewModels()
    private var searchJob: Job? = null
    private lateinit var itemList:List<ShortsContent>

    override fun initFragment() {
        //TODO : 네트워크 연결 후 삭제
        val keyword = arguments?.getString("keyword")
        binding.searchView.setText(keyword)
        requestRecipeList(keyword!!)


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

    private fun requestRecipeList(keyword:String){
        launchWithLifecycle(lifecycle) {
            searchViewModel.getSearchRecipe(keyword)
            searchViewModel.shortsResult.collect{
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

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
    }

    private fun recipeRecyclerview(){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                // findNavController().navigate(R.id.action_recipeMainFragment_to_recipeDetailFragment)
            }
        }

        val adapter = SearchShortsAdapter(clickListener)
        binding.rvShorts.adapter = adapter
        binding.rvShorts.layoutManager = GridLayoutManager(context,2)
        adapter.replaceData(itemList)

        binding.chipRecipe.setOnCheckedStateChangeListener { group, checkedIds ->
            when (checkedIds) {
                binding.btnRecipeNewest -> {
                    itemList.sortedByDescending { it.writtenBy }
                    adapter.replaceData(itemList)
                    binding.btnRecipeNewest.isCheckable = true
                }
                binding.btnRecipePopular -> {
                    itemList.sortedByDescending { it.likes_count }
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