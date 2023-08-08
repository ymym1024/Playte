package com.cmc.recipe.presentation.ui.search

import androidx.recyclerview.widget.GridLayoutManager
import com.cmc.recipe.data.model.SearchShorts
import com.cmc.recipe.databinding.FragmentSearchShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener


class SearchShortsFragment : BaseFragment<FragmentSearchShortsBinding>(FragmentSearchShortsBinding::inflate) {
    override fun initFragment() {
        //TODO : 네트워크 연결 후 삭제
        val itemList = arrayListOf(
            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
        )
        recipeRecyclerview(itemList)
    }

    private fun recipeRecyclerview(itemList:ArrayList<SearchShorts>){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                // findNavController().navigate(R.id.action_recipeMainFragment_to_recipeDetailFragment)
            }
        }

        val adapter = SearchShortsAdapter(clickListener)
        binding.rvShorts.adapter = adapter
        binding.rvShorts.layoutManager = GridLayoutManager(context,2)
        adapter.replaceData(itemList)
    }

}