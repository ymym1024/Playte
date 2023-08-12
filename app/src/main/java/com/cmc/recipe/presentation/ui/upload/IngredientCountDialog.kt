package com.cmc.recipe.presentation.ui.upload

import com.cmc.recipe.R
import com.cmc.recipe.databinding.DialogIngredientCountBinding
import com.cmc.recipe.presentation.ui.base.BaseDialog

class IngredientCountDialog(val ingredient_name: String) : BaseDialog<DialogIngredientCountBinding>(DialogIngredientCountBinding::inflate) {

    private lateinit var clickListener: onCountListener
    private var count = 1;

    fun setListener(clickListener:onCountListener){
        this.clickListener = clickListener
    }

    override fun initDialog() {
        initView()

        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnCheck.setOnClickListener {
            val count = Integer.parseInt(binding.tvCount.text.toString())
            clickListener.getCount(count)
            dismiss()
        }

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

    interface onCountListener{
        fun getCount(count:Int)
    }
}