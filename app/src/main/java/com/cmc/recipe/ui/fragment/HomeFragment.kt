package com.cmc.recipe.ui.fragment

import com.cmc.recipe.base.BaseFragment
import com.cmc.recipe.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun initFragment() {
        // TODO : 수정예정
        binding.btnAdd.setOnClickListener {
            val ingredientBottomSheet = IngredientBottomSheetFragment(requireContext())
            ingredientBottomSheet.show(parentFragmentManager,ingredientBottomSheet.tag)
        }
    }


}