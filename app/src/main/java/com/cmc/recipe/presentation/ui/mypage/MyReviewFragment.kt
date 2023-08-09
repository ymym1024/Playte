package com.cmc.recipe.presentation.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.RecipeReview
import com.cmc.recipe.databinding.FragmentMyReviewBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.common.RemoveBottomSheetFragment
import com.cmc.recipe.presentation.ui.recipe.RecipeItemHolder
import com.cmc.recipe.presentation.ui.recipe.RecipeListAdapter
import com.cmc.recipe.utils.Constant


class MyReviewFragment : BaseFragment<FragmentMyReviewBinding>(FragmentMyReviewBinding::inflate) {

    val url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg"
    override fun initFragment() {
        //TODO : 네트워크 연결 후 삭제
        val itemList = arrayListOf(
            RecipeReview(nick = "냉파 CMC", date = "2023.05.11", content = "기름이 너무 많아서 망했습니다. 맛은 있는데 약간 짜고 느끼한\n" +
                    "감이 좀 있어요. 그래도 다들 한번씩 해보길", stars = 3, thumb_up = 2, thumb_down = 2,image_list = arrayListOf(url,url,url)
            ),
            RecipeReview(nick = "냉파 CMC", date = "2023.05.11", content = "기름이 너무 많아서 망했습니다. 맛은 있는데 약간 짜고 느끼한\n" +
                    "감이 좀 있어요. 그래도 다들 한번씩 해보길", stars = 3, thumb_up = 2, thumb_down = 2,image_list = arrayListOf(url)),
            RecipeReview(nick = "냉파 CMC", date = "2023.05.11", content = "기름이 너무 많아서 망했습니다. 맛은 있는데 약간 짜고 느끼한\n" +
                    "감이 좀 있어요. 그래도 다들 한번씩 해보길", stars = 3, thumb_up = 2, thumb_down = 2,image_list = arrayListOf(url,url,url,url)),
            RecipeReview(nick = "냉파 CMC", date = "2023.05.11", content = "기름이 너무 많아서 망했습니다. 맛은 있는데 약간 짜고 느끼한\n" +
                    "감이 좀 있어요. 그래도 다들 한번씩 해보길", stars = 3, thumb_up = 2, thumb_down = 2,image_list = arrayListOf(url))
        )
        recipeRecyclerview(itemList)
    }

    private fun recipeRecyclerview(itemList:ArrayList<RecipeReview>){

        val adapter = RecipeReviewAdapter()

        adapter.setListener(object : RecipeReviewItemHolder.onActionListener{
            override fun action(item: RecipeReview) {

            }
        })

        binding.rvReview.adapter = adapter
        binding.rvReview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }

}