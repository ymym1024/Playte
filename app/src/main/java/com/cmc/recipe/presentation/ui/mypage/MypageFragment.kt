package com.cmc.recipe.presentation.ui.mypage


import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.SearchShorts
import com.cmc.recipe.databinding.FragmentMypageBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.recipe.RecipeRecommendAdapter
import com.cmc.recipe.presentation.ui.search.SearchShortsAdapter


class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate) {

    override fun initFragment() {

        recipeRecyclerview()

        shortsRecyclerview()

    }

    private fun recipeRecyclerview(){
        val itemList = arrayListOf(
            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, ingredient1 = "토마토", ingredient2 = "계란", ingredient3 = "밥"),
            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, ingredient1 = "토마토", ingredient2 = "계란", ingredient3 = "밥"),
            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, ingredient1 = "토마토", ingredient2 = "계란", ingredient3 = "밥"),
            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, ingredient1 = "토마토", ingredient2 = "계란", ingredient3 = "밥"),
        )

        val adapter = RecipeRecommendAdapter(requireContext())
        binding.rvViewRecipe.adapter = adapter
        binding.rvViewRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        adapter.replaceData(itemList)
    }

    private fun shortsRecyclerview(){
        val itemList = arrayListOf(
            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
        )

        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {

            }
        }

        val adapter = SearchShortsAdapter(clickListener)
        binding.rvViewShorts.adapter = adapter
        binding.rvViewShorts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.replaceData(itemList)
    }

}