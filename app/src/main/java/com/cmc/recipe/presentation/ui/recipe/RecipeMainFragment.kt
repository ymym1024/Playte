package com.cmc.recipe.presentation.ui.recipe

import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager.ActionListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.FragmentRecipeMainBinding
import com.cmc.recipe.presentation.ui.MainActivity
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener

class RecipeMainFragment : BaseFragment<FragmentRecipeMainBinding>(FragmentRecipeMainBinding::inflate) {

    override fun initFragment() {
        //TODO : 네트워크 연결 후 삭제
        val itemList = arrayListOf(
            RecipeItem(image_url = "", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
            RecipeItem(image_url = "", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = false),
            RecipeItem(image_url = "", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = false),
            RecipeItem(image_url = "", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
        )
        recipeRecyclerview(itemList)
    }

    private fun recipeRecyclerview(itemList:ArrayList<RecipeItem>){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                movePage(R.id.action_recipeMainFragment_to_recipeActivity)
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
    }

}