package com.cmc.recipe.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.cmc.recipe.base.BaseFragment
import com.cmc.recipe.databinding.FragmentHomeBinding
import com.cmc.recipe.ui.MainActivity

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)
    }
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
        binding.indicator.attachTo(binding.vpRefrigerator)

        // 냉장, 실온 클릭 시 viewpager 이동
        binding.tvRefrigerator.setOnClickListener {
            viewpager.currentItem = 0
        }
        binding.tvAmbient.setOnClickListener {
            viewpager.currentItem = 1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)
    }
}