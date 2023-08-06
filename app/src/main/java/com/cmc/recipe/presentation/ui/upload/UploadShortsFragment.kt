package com.cmc.recipe.presentation.ui.upload

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.Ingredient
import com.cmc.recipe.databinding.FragmentUploadShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment

class UploadShortsFragment : BaseFragment<FragmentUploadShortsBinding>(FragmentUploadShortsBinding::inflate) {

    override fun initFragment() {
        initAdapter()
        editScroll()
        settingToolbar()
        uploadShorts()
    }

    private fun initAdapter(){
        //TODO : mockup data => 네트워크 연결 후 삭제
        val dataList = arrayListOf(
            Ingredient("토마토","재료"),
            Ingredient("토마토 소스","양념"),
            Ingredient("토마토","양념"),
            Ingredient("토마토 소스","양념"),
            Ingredient("토마토","양념"),
            Ingredient("토마토 소스","양념")
        )

        val ingredientAdapter = IngredientAdapter()
        ingredientAdapter.setActionListener(object :IngredientItemHolder.actionListener{
            override fun remove(name: String) {
                ingredientAdapter.removeItem(name)
            }
        })
        binding.rvCompleteIngredient.adapter = ingredientAdapter
        binding.rvCompleteIngredient.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)

        val adapter = IngredientListAdapter(requireContext(),dataList)
        binding.etRecipeIngredient.setAdapter(adapter)
        binding.etRecipeIngredient.setOnItemClickListener { _, v, position, _ ->
            val selectedData = adapter.getItem(position)
            ingredientAdapter.addItem(selectedData.name)
            binding.etRecipeIngredient.setText("")
            hideKeyboard(v)
        }
    }

    private fun editScroll(){
        binding.etRecipeIngredient.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.scrollView.post {
                    binding.scrollView.scrollTo(0, binding.etRecipeIngredient.bottom)
                }
            }
        }
    }

    private fun settingToolbar(){

    }

    private fun uploadShorts(){
        binding.btnUploadShorts.setOnClickListener {

        }
    }

}