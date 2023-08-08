package com.cmc.recipe.presentation.ui.recipe

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.FragmentRecipeMainBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener

class RecipeMainFragment : BaseFragment<FragmentRecipeMainBinding>(FragmentRecipeMainBinding::inflate) {

    override fun initFragment() {
        //TODO : 네트워크 연결 후 삭제
        val itemList = arrayListOf(
            RecipeItem(image_url = "", name = "토마토 계란 볶음밥", time = 10, ingredient1 = "토마토", ingredient2 = "계란", ingredient3 = "밥"),
            RecipeItem(image_url = "", name = "토마토 계란 볶음밥", time = 10, ingredient1 = "토마토", ingredient2 = "계란", ingredient3 = "밥"),
            RecipeItem(image_url = "", name = "토마토 계란 볶음밥", time = 10, ingredient1 = "토마토", ingredient2 = "계란", ingredient3 = "밥"),
            RecipeItem(image_url = "", name = "토마토 계란 볶음밥", time = 10, ingredient1 = "토마토", ingredient2 = "계란", ingredient3 = "밥"),
        )
        recipeRecyclerview(itemList)
    }

    private fun recipeRecyclerview(itemList:ArrayList<RecipeItem>){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                findNavController().navigate(R.id.action_recipeMainFragment_to_recipeDetailFragment)
            }
        }

        val adapter = RecipeListAdapter(clickListener)
        binding.rvRecipe.adapter = adapter
        binding.rvRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }
}