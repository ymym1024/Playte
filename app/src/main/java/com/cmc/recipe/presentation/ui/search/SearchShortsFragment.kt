package com.cmc.recipe.presentation.ui.search

import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.SearchShorts
import com.cmc.recipe.data.model.response.ShortsContent
import com.cmc.recipe.data.model.response.ShortsData
import com.cmc.recipe.databinding.FragmentSearchShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.shortform.ShortsDetailActivity
import com.cmc.recipe.presentation.viewmodel.SearchViewModel
import com.cmc.recipe.presentation.viewmodel.ShortsViewModel
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class SearchShortsFragment : BaseFragment<FragmentSearchShortsBinding>(FragmentSearchShortsBinding::inflate) {

    private val searchViewModel : SearchViewModel by viewModels()
    private val shortsViewModel : ShortsViewModel by viewModels()
    private lateinit var itemList:List<ShortsContent>

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

    private fun moveShortsPage(position:Int){
        shortsViewModel.insertRecentShorts(itemList[position]!!)
        val intent = Intent(requireContext(), ShortsDetailActivity::class.java)
        intent.putExtra("detailId",position)
        startActivity(intent)
    }

    private fun requestRecipeList(keyword:String){
        launchWithLifecycle(lifecycle) {
            searchViewModel.getSearchShortform(keyword)
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

    private fun recipeRecyclerview(){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                moveShortsPage(id)
            }
        }

        val adapter = SearchShortsAdapter(clickListener)
        binding.rvShorts.adapter = adapter
        binding.rvShorts.layoutManager = GridLayoutManager(context,2)
        adapter.replaceData(itemList)

        binding.chipRecipe.setOnCheckedStateChangeListener { group, checkedIds ->
            when (checkedIds[0]) {
                R.id.btn_recipe_newest -> {
                    val newList = itemList.sortedByDescending { it.writtenBy }
                    adapter.replaceData(newList)
                    binding.btnRecipeNewest.isCheckable = true
                }
                R.id.btn_recipe_popular -> {
                    val newList = itemList.sortedBy { it.likes_count }
                    adapter.replaceData(newList)
                    binding.btnRecipePopular.isCheckable = true
                }
                R.id.btn_recipe_minium_time -> {
                    val newList = itemList.sortedBy { it.video_time }
                    adapter.replaceData(newList)
                    binding.btnRecipeMiniumTime.isCheckable = true
                }
            }
        }
    }

}