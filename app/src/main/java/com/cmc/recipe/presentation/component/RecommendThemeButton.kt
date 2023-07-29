package com.cmc.recipe.presentation.component

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.cmc.recipe.R
import com.google.android.material.card.MaterialCardView

class RecommendThemeButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet
) : MaterialCardView(context, attrs) {

    private lateinit var bg:MaterialCardView
    private lateinit var bgTitle:TextView
    private lateinit var bgImage:ImageView

    init {
        initView()
        getAttrs(attrs)
    }

    private fun initView(){
        val v = View.inflate(context, R.layout.custom_theme_button, this)
        bg = v.findViewById(R.id.bg)
        bgTitle = v.findViewById(R.id.bgTitle)
        bgImage = v.findViewById(R.id.bgImg)
    }

    @SuppressLint("ResourceAsColor")
    private fun getAttrs(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(attrs,
            R.styleable.RecommendThemeButton,
            0,0
        ).apply {
            try {
                setText(getString(R.styleable.RecommendThemeButton_bgTitle))
                setImage(getResourceId(R.styleable.RecommendThemeButton_bgImg,R.drawable.img_recommend_theme))
                setBgColor(getColor(R.styleable.RecommendThemeButton_backgroundColor,R.color.white))
                setBorder(getColor(R.styleable.RecommendThemeButton_borderColor,R.color.black))
            } finally {
                recycle()
            }

        }
    }

    fun setBorder(color:Int){
        bg.setStrokeColor(color)
    }
    fun setBgColor(color:Int){
        bg.setBackgroundColor(color)
    }
    fun setImage(resourceId: Int) {
        bgImage.setImageResource(resourceId)
    }
    fun setText(text:String?){
        bgTitle.text = text
        onRefresh()
    }

    private fun onRefresh() {
        invalidate()
        requestLayout()
    }


}