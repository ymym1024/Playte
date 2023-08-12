package com.cmc.recipe.presentation.ui.upload

import com.cmc.recipe.R
import com.cmc.recipe.databinding.DialogIngredientCountBinding
import com.cmc.recipe.presentation.ui.base.BaseDialog

class IngredientCountDialog(val ingredient_name: String) : BaseDialog<DialogIngredientCountBinding>(DialogIngredientCountBinding::inflate) {

    private lateinit var clickListener: onActionListener
    private var count = 1;

    fun setListener(clickListener:onActionListener){
        this.clickListener = clickListener
    }

    override fun initDialog() {
        initView()

        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnCheck.setOnClickListener {  }

    }

    private fun initView(){
        binding.tvIngredientName.text = ingredient_name

        binding.btnPlus.setOnClickListener {
            binding.tvCount.text = "${++count}"
        }

        binding.btnMinus.setOnClickListener {
            if(count > 1){
                binding.btnMinus.setImageResource(R.drawable.ic_minus_activate)
            }else{
                binding.btnMinus.setImageResource(R.drawable.ic_minus_deactivate)
            }
            binding.tvCount.text = "${--count}"
        }
    }
}