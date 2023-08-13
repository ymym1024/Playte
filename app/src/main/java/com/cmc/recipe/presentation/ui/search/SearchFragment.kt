package com.cmc.recipe.presentation.ui.search

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.FragmentSearchBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.recipe.RecipeActivity
import com.cmc.recipe.presentation.ui.recipe.RecipeItemHolder
import com.cmc.recipe.presentation.ui.recipe.RecipeListAdapter
import com.cmc.recipe.utils.Constant


class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private lateinit var destation : String

    override fun initFragment() {
        val activity = activity as SearchActivity
        destation = activity.getPrevDestation()

        initRV()
    }

    private fun initRV(){
        val itemList = arrayListOf("토마토 계란 볶음밥","토마토 계란 볶음밥","토마토 계란 볶음밥")

        val adapter = SearchItemAdapter()
        binding.rvRecent.adapter = adapter
        binding.rvRecent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.replaceData(itemList)

        moveSearch()
    }

    private fun moveSearch(){
        binding.searchView.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                if(destation==Constant.SHORTS){
                    movePage(R.id.action_searchFragment_to_searchShortsFragment)
                }else{
                    movePage(R.id.action_searchFragment_to_searchRecipeFragment)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

}