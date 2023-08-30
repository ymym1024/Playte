package com.cmc.recipe.presentation.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.ItemRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.*

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
        notifyDataSetChanged()
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
            clickListener.onMovePage(item?.recipe_id!!)
        }

        if(type == Constant.WRITE){
            binding.btnStar.setImageResource(R.drawable.ic_trash)
        }

        binding.btnStar.setOnClickListener {
            actionListener.action(item!!)
        }

        binding.let {
            item?.let { recipe ->
                val create_date = recipe.created_date.parseDateTime()?.formatDateRelativeToNow()

                it.ivRecipeMain.loadImagesWithGlideRound(recipe.recipe_thumbnail_img,10)
                it.tvRecipeName.text = recipe.recipe_name
                it.tvRecipeTime.text = "${recipe.cook_time}분"
                it.tvTimeNickname.text = "${create_date} | ${recipe.nickname}"
                it.tvStarCnt.text = "${String.format("%.2f",recipe.rating)}(${recipe.comment_count})"
                if(type.isEmpty()){ // 작성레시피가 아닌 경우
                    if(!recipe.is_saved) it.btnStar.setBackgroundResource(R.drawable.ic_bookmark_deactive)
                    else it.btnStar.setBackgroundResource(R.drawable.ic_bookmark_activate)
                }
            }
        }
    }

    interface onActionListener{
        fun action(item:RecipeItem)
    }
}