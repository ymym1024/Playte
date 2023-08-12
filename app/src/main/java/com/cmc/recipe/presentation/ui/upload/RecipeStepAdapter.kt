package com.cmc.recipe.presentation.ui.upload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.databinding.ItemRecipeStepBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.loadImagesWithGlide

class RecipeStepAdapter:
    BaseAdapter<RecipeStep, ItemRecipeStepBinding, RecipeStepItemHolder>() ,
    ItemTouchCallback.ItemTouchHelperListener {

    private lateinit var clickListener: RecipeStepItemHolder.onItemListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeStepItemHolder {
        return RecipeStepItemHolder(
            ItemRecipeStepBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener,
            itemPosition
        )
    }

    fun setListener(clickListener:RecipeStepItemHolder.onItemListener){
        this.clickListener = clickListener
    }

    fun addItem(item: RecipeStep) {
        getData().add(item)
        notifyItemInserted(getData().size - 1)
    }

    override fun onItemMove(from: Int, to: Int) {
        val items = getData()
        val movedItem = items.removeAt(from)
        items.add(to, movedItem)
        notifyItemMoved(from, to)
    }
}

class RecipeStepItemHolder(viewBinding: ItemRecipeStepBinding, val listener: onItemListener,val currentPosition: Int):
    BaseHolder<RecipeStep, ItemRecipeStepBinding>(viewBinding){
    override fun bind(binding: ItemRecipeStepBinding, item: RecipeStep?) {
        binding.let { view->
            view.tvStepTitle.text = item?.recipeDesc
            view.ibImage.loadImagesWithGlide(item?.recipeImage!!)

        }
    }

    interface onItemListener{
        fun delete(position: Int)
    }
}

class ItemTouchCallback(private val listener: ItemTouchHelperListener): ItemTouchHelper.Callback() {

    /** 드래그 방향과 드래그 이동을 정의하는 함수 */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // 드래그 방향
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        // 스와이프 방향
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        // 드래그 이동을 만드는 함수
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /** 아이템이 움직일떼 호출되는 함수 */
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return false
    }

    /** 아이템이 스와이프 될때 호출되는 함수 */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        TODO("Not yet implemented")
    }

    interface ItemTouchHelperListener {
        fun onItemMove(from: Int, to: Int)
    }
}
