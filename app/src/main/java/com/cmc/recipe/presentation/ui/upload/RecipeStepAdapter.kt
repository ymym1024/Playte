package com.cmc.recipe.presentation.ui.upload

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.databinding.ItemRecipeStepBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlide


class RecipeStepAdapter:
    BaseAdapter<RecipeStep, ItemRecipeStepBinding, RecipeStepItemHolder>(),
    ItemTouchCallback.ItemTouchHelperListener {

    private lateinit var listener:onChangeListener
    interface onChangeListener{
        fun change()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeStepItemHolder {
        return RecipeStepItemHolder(
            ItemRecipeStepBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    fun setListener(listener:onChangeListener){
        this.listener = listener
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

    override fun onItemRemove(position: Int) {
        getData().removeAt(position)
        notifyItemRemoved(position)
        listener.change()
    }

    override fun onItemSwipe(position: Int) {
        //
    }

}

class RecipeStepItemHolder(viewBinding: ItemRecipeStepBinding):
    BaseHolder<RecipeStep, ItemRecipeStepBinding>(viewBinding){
    override fun bind(binding: ItemRecipeStepBinding, item: RecipeStep?) {
        binding.let { view->
            view.tvStepTitle.text = item?.recipeDesc
            view.ibImage.loadImagesWithGlide(item?.recipeImage!!)

        }
    }
}

enum class ButtonsState {
    GONE, RIGHT_VISIBLE
}
class ItemTouchCallback(val listener: ItemTouchHelperListener): ItemTouchHelper.Callback() {
    private var swipeBack = false
    private var buttonShowedState = ButtonsState.GONE
    private val buttonWidth = 100f
    private var buttonInstance: RectF? = null
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null

    /** 드래그 방향과 드래그 이동을 정의하는 함수 */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // 드래그 방향
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        // 스와이프 방향
        val swipeFlags = ItemTouchHelper.LEFT

        // 드래그 이동을 만드는 함수
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        var dX = dX
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState !== ButtonsState.GONE) {
                if (buttonShowedState === ButtonsState.RIGHT_VISIBLE) //왼쪽으로 스와이프 했을 때
                    dX = dX.coerceAtMost(-buttonWidth)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            } else {
                setTouchListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            if (buttonShowedState === ButtonsState.GONE) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        currentItemViewHolder = viewHolder
    //    drawButtons(c, currentItemViewHolder!!)
    }

    fun onDraw(c: Canvas?) {
        if (currentItemViewHolder != null) {
            drawButtons(c!!, currentItemViewHolder!!)
        }
    }

    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
                if (swipeBack) {
                    if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE

                    if (buttonShowedState !== ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder,
                            dX, dY, actionState, isCurrentlyActive)
                        setItemsClickable(recyclerView, false)
                    }
                }
                return false
            }
        })
    }

    private fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            false
        }
    }

    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                super@ItemTouchCallback.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    0f,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                recyclerView.setOnTouchListener { v, event -> false }
                setItemsClickable(recyclerView, true)
                swipeBack = false
                if (listener != null && buttonInstance != null && buttonInstance!!.contains(event.getX(), event.getY())) {
                    if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                        listener.onItemRemove(viewHolder.getAdapterPosition());
                    }
                }
                buttonShowedState = ButtonsState.GONE
            }
            false
        }
    }

    private fun setItemsClickable(
        recyclerView: RecyclerView,
        isClickable: Boolean
    ) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val buttonWidthWithoutPadding = buttonWidth - 20
        val corners = 16f
        val itemView = viewHolder.itemView
        val p = Paint()

        val rightButton = RectF(
            itemView.right - buttonWidthWithoutPadding,
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat()
        )
        p.setColor(Color.WHITE)
        c.drawRoundRect(rightButton, corners, corners, p)
        val drawable = ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.ic_trash)
        drawImage(drawable!!,c,rightButton)
        buttonInstance = null
        if (buttonShowedState === ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = rightButton
        }
    }

    private fun drawImage(drawable: Drawable, c: Canvas, button: RectF) {
        if (drawable != null) {
            val iconLeft = (button.centerX() - drawable.intrinsicWidth / 2).toInt()
            val iconTop = (button.centerY() - drawable.intrinsicHeight / 2).toInt()
            drawable.setBounds(
                iconLeft,
                iconTop,
                iconLeft + drawable.intrinsicWidth,
                iconTop + drawable.intrinsicHeight
            )
            drawable.draw(c)
        }
    }

    /** 아이템이 움직일떼 호출되는 함수 */
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return false
    }

    /** 아이템이 스와이프 될때 호출되는 함수 */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onItemSwipe(viewHolder.adapterPosition)
    }

    interface ItemTouchHelperListener {
        fun onItemMove(from: Int, to: Int)
        fun onItemRemove(position: Int)
        fun onItemSwipe(position: Int)
    }
}

