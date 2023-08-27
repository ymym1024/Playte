package com.cmc.recipe.presentation.ui.mypage

import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.FragmentWriteRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.common.CustomBottomSheetFragment
import com.cmc.recipe.presentation.ui.recipe.RecipeItemHolder
import com.cmc.recipe.presentation.ui.recipe.RecipeListAdapter
import com.cmc.recipe.utils.Constant


class WriteRecipeFragment : BaseFragment<FragmentWriteRecipeBinding>(FragmentWriteRecipeBinding::inflate) {
    override fun initFragment() {
        //TODO : 네트워크 연결 후 삭제
//        val itemList = arrayListOf(
//            RecipeItem(image_url = "", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
//            RecipeItem(image_url = "", name = "토마토 계란 볶음밥2", time = 10, nickName = "구땡뿡야",star=30, flag = false),
//            RecipeItem(image_url = "", name = "토마토 계란 볶음밥3", time = 10, nickName = "구땡뿡야",star=30, flag = false),
//            RecipeItem(image_url = "", name = "토마토 계란 볶음밥4", time = 10, nickName = "구땡뿡야",star=30, flag = true),
//        )
//        recipeRecyclerview(itemList)
    }

    private fun recipeRecyclerview(itemList:ArrayList<RecipeItem>){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                // findNavController().navigate(R.id.action_recipeMainFragment_to_recipeDetailFragment)
            }
        }

        val adapter = RecipeListAdapter(clickListener)
        adapter.setType(Constant.WRITE)
        adapter.setListener(object : RecipeItemHolder.onActionListener{
            override fun action(item: RecipeItem) {
                // bottom sheet show
                CustomBottomSheetFragment().show(fragmentManager!!, "RemoveBottomSheetFragment")
            }
        })

        binding.rvRecipe.adapter = adapter
        binding.rvRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }

}