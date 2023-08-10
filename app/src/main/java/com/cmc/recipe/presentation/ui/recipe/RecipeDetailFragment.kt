package com.cmc.recipe.presentation.ui.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.IngredientItem
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.RecipeOrder
import com.cmc.recipe.databinding.FragmentRecipeDetailBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.utils.loadImagesWithGlide


class RecipeDetailFragment : BaseFragment<FragmentRecipeDetailBinding>(FragmentRecipeDetailBinding::inflate) {
    override fun initFragment() {
        initDatabinding()
        initRecipeRV()
        initRecipeIngredientRV()
        initRecommendRV()
    }

    private fun initDatabinding(){
        binding.ivThumb.loadImagesWithGlide("https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg")
        binding.tvNickname.text = "규땡뿡야"
        binding.tvRecipeTitle.text = "토마토 계란볶음밥"
        binding.tvRecipeInfo.text = "토마토가 많아서 볶아먹고 삶아먹고 이젠 밥에도 넣어봤어요"
        binding.tvRecipeDate.text = "2023.02.12"

    }

    private fun initRecipeIngredientRV(){
        val itemList = arrayListOf(
            IngredientItem(name = "토마토", cnt = "3개"),
            IngredientItem(name = "계란", cnt = "3개"),
            IngredientItem(name = "대파", cnt = "3개"),
        )

        val itemList1 = arrayListOf(
            IngredientItem(name = "굴소스", cnt = "2T"),
            IngredientItem(name = "소금", cnt = "2T"),
        )

        val adapter = RecipeIngredientAdapter()
        binding.rvIngredient.adapter = adapter
        binding.rvIngredient.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)

        val adapter1 = RecipeIngredientAdapter()
        binding.rvSpices.adapter = adapter1
        binding.rvSpices.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter1.replaceData(itemList1)
    }

    private fun initRecipeRV(){
        val itemList = arrayListOf(
            RecipeOrder(recipeImage = "", recipeInfo = "그릇에 옮기기", recipeOrder = 1, recipeDetail = "당근이 노릇노릇하게 익으면 다 익은 당근을 그릇에 옮겨 20분 정도 냉장고에서 식혀주세요."),
            RecipeOrder(recipeImage = "", recipeInfo = "그릇에 옮기기", recipeOrder = 1, recipeDetail = "당근이 노릇노릇하게 익으면 다 익은 당근을 그릇에 옮겨 20분 정도 냉장고에서 식혀주세요."),
        )

        val adapter = RecipeOrderAdapter(requireContext())
        binding.rvRecipeList.adapter = adapter
        binding.rvRecipeList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }

    private fun initRecommendRV(){
        val itemList = arrayListOf(
            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
        )

        val adapter = RecipeRecommendAdapter(requireContext())
        binding.rvRecommendRecipe.adapter = adapter
        binding.rvRecommendRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.replaceData(itemList)
    }
}