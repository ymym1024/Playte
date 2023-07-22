package com.cmc.recipe.presentation.ui.recipe

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.FragmentRecipeRegisterBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener

class RecipeRegisterFragment : BaseFragment<FragmentRecipeRegisterBinding>(FragmentRecipeRegisterBinding::inflate) {

    override fun initFragment() {
        val itemList = arrayListOf<String>()

        initRecipeRv(itemList)
    }

    private fun initRecipeRv(itemList:ArrayList<String>){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
                findNavController().navigate(R.id.action_recipeMainFragment_to_recipeDetailFragment)
            }
        }

        val adapter = RecipeRegisterAdapter(clickListener)
        binding.rvStep.adapter = adapter
        binding.rvStep.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
       // adapter.replaceData(itemList)

        binding.etRecipe.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                val recipeStep = binding.etRecipe.text.toString()
                if (recipeStep.isNotEmpty()) {
                    adapter.addItem(recipeStep)
                    binding.etRecipe.text.clear() // EditText를 초기화합니다.
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

}