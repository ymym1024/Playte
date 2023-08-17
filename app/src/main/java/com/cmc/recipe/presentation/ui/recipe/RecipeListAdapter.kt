package com.cmc.recipe.presentation.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.ItemRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.loadImagesWithGlide

class RecipeListAdapter(val clickListener: OnClickListener):
    BaseAdapter<RecipeItem, ItemRecipeBinding, RecipeItemHolder>() {

    private var type : String = ""
    private lateinit var listener : RecipeItemHolder.onActionListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeItemHolder {
        return RecipeItemHolder(
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            type,
            clickListener,
            listener
        )
    }
    fun setType(type: String){
        this.type = type
    }

    fun setListener(listener:RecipeItemHolder.onActionListener){
        this.listener = listener
    }
    fun removeItem(item:RecipeItem){
        val itemList = getData()
        itemList.remove(item)
        notifyDataSetChanged()
    }
}

class RecipeItemHolder(viewBinding: ItemRecipeBinding, val type:String,val clickListener: OnClickListener,val actionListener: onActionListener):
    BaseHolder<RecipeItem, ItemRecipeBinding>(viewBinding){
    override fun bind(binding: ItemRecipeBinding, item: RecipeItem?) {
        binding.recipeItem.setOnClickListener {
            clickListener.onMovePage(0)
        }

        if(type == Constant.WRITE){
            binding.btnStar.setBackgroundResource(R.drawable.ic_trash)
        }

        binding.btnStar.setOnClickListener {
            actionListener.action(item!!)
        }

        binding.let {
            item?.let { recipe ->
                it.ivRecipeMain.loadImagesWithGlide(recipe.recipe_thumbnail_img)
                it.tvRecipeName.text = recipe.recipe_name
                it.tvRecipeTime.text = "" //"${recipe.time}분"
                it.tvTimeNickname.text = "${recipe.created_date} | ${recipe.nickname}"
                it.tvStarCnt.text = "${recipe.rating}(${recipe.comment_count})"
                if(!recipe.is_saved) it.btnStar.setBackgroundResource(R.drawable.ic_bookmark_deactive)
                else it.btnStar.setBackgroundResource(R.drawable.ic_bookmark_activate)
            }
        }
    }

    interface onActionListener{
        fun action(item:RecipeItem)
    }
}