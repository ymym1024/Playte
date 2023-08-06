package com.cmc.recipe.presentation.ui.upload

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.ItemIngredientBinding
import com.cmc.recipe.databinding.ItemRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.loadImagesWithGlide

class IngredientAdapter:
    BaseAdapter<String, ItemIngredientBinding, IngredientItemHolder>() {

    lateinit var listener : IngredientItemHolder.actionListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientItemHolder {
        return IngredientItemHolder(
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )
    }

    fun addItem(item:String){
        val itemList = getData()
        itemList.addAll(listOf(item))
        notifyDataSetChanged()
    }

    fun removeItem(item:String){
        val itemList = getData()
        itemList.remove(item)
        notifyDataSetChanged()
    }

    fun setActionListener(listener: IngredientItemHolder.actionListener){
        this.listener = listener
    }

}

class IngredientItemHolder(viewBinding: ItemIngredientBinding, val clickListener: actionListener):
    BaseHolder<String, ItemIngredientBinding>(viewBinding){

    override fun bind(binding: ItemIngredientBinding, item: String?) {
        binding.let {
            it.tvIngredientName.text = item
            it.btnRemove.setOnClickListener {
                clickListener.remove(item.toString())
            }
        }
    }

    interface actionListener{
        fun remove(name:String)
    }
}