package com.cmc.recipe.ui.fragment

import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cmc.recipe.base.BaseFragment
import com.cmc.recipe.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun initFragment() {
        // TODO : 수정예정
        binding.btnAdd.setOnClickListener {
            val ingredientBottomSheet = IngredientBottomSheetFragment(requireContext())
            ingredientBottomSheet.show(parentFragmentManager,ingredientBottomSheet.tag)
        }

        // 냉장고 UI - viewPager2 연결
        val innerFragmentList:ArrayList<Fragment> = arrayListOf(InnerViewFragment1(),InnerViewFragment2())
        val viewPager2Adapter = RefrigeratorViewAdapter(childFragmentManager,lifecycle,innerFragmentList)
        val viewpager = binding.vpRefrigerator
        viewpager.adapter = viewPager2Adapter

        // 냉장, 실온 클릭 시 viewpager 이동
        binding.tvRefrigerator.setOnClickListener {
            viewpager.currentItem = 0
        }
        binding.tvAmbient.setOnClickListener {
            viewpager.currentItem = 1
        }
    }


}