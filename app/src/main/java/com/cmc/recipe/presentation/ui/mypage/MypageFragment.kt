package com.cmc.recipe.presentation.ui.mypage


import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.SearchShorts
import com.cmc.recipe.databinding.FragmentMypageBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.recipe.RecipeRecommendAdapter
import com.cmc.recipe.presentation.ui.search.SearchShortsAdapter


class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate) {

    override fun initFragment() {

        initMenu()

        initView()

        recipeRecyclerview()

        shortsRecyclerview()

    }

    private fun initView() {
        binding.let {
            it.btnSaveRecipe.setOnClickListener {
                movePage(R.id.action_mypageFragment_to_saveRecipeFragment)
            }
            it.btnWriteRecipe.setOnClickListener {
                movePage(R.id.action_mypageFragment_to_writeRecipeFragment)
            }
            it.btnMyReview.setOnClickListener {
                movePage(R.id.action_mypageFragment_to_myReviewFragment)
            }
        }
    }

    private fun initMenu(){
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_mypage, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_edit_button -> {
                        movePage(R.id.action_mypageFragment_to_settingFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun recipeRecyclerview(){
//        val itemList = arrayListOf(
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = false),
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = false),
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
//        )
//
//        val adapter = RecipeRecommendAdapter(requireContext())
//        binding.rvViewRecipe.adapter = adapter
//        binding.rvViewRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
//        adapter.replaceData(itemList)
    }

    private fun shortsRecyclerview(){
//        val itemList = arrayListOf(
//            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
//            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
//            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
//            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
//        )
//
//        val clickListener = object : OnClickListener {
//            override fun onMovePage(id: Int) {
//
//            }
//        }
//
//        val adapter = SearchShortsAdapter(clickListener)
//        binding.rvViewShorts.adapter = adapter
//        binding.rvViewShorts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        adapter.replaceData(itemList)
    }

}