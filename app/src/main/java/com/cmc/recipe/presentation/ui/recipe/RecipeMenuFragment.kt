package com.cmc.recipe.presentation.ui.recipe

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cmc.recipe.databinding.FragmentRecipeMenuBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator


class RecipeMenuFragment : BaseFragment<FragmentRecipeMenuBinding>(FragmentRecipeMenuBinding::inflate) {

    override fun initFragment() {

        val viewPager2Adapter = RecipeViewPager2Adapter(parentFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPager2Adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if(position == 0){
                tab.text = "리뷰"
            }else{
                tab.text = "댓글"
            }
        }.attach()
    }

}

class RecipeViewPager2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> RecipeMenuReviewFragment()
            else -> RecipeMenuCommentFragment()
        }
    }
}