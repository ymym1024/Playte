package com.cmc.recipe.presentation.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.cmc.recipe.R
import com.cmc.recipe.utils.CommonTextWatcher

class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var editText: EditText
    private lateinit var textLength: TextView

    private var count : Int = 0

    init {
        setupViews(context,attrs)
    }

    @SuppressLint("ResourceAsColor")
    private fun setupViews(context: Context, attrs: AttributeSet?) {
        editText = EditText(context)
        textLength = TextView(context)

        setAlign()

        // Set the drawable background for the ConstraintLayout
        setBackgroundResource(R.drawable.bg_gray_radius)
        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.textSize = 14.0F
        editText.setTextColor(R.color.black_4)

        editText.gravity = android.view.Gravity.TOP
        textLength.gravity = android.view.Gravity.BOTTOM

        this.layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT)
        addView(editText)
        addView(textLength)

        setAttrs(attrs)

        setupTextChangedListener()
    }

    private fun setAttrs(attrs: AttributeSet?){
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText)
            val hint = typedArray.getString(R.styleable.CustomEditText_hint)
            val maxLength = typedArray.getString(R.styleable.CustomEditText_maxLength)
            typedArray.recycle()

            setHint(hint!!)
            setMaxLength(Integer.parseInt(maxLength))
        }
    }

    private fun setAlign(){
        val editTextParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.MATCH_CONSTRAINT)
        editTextParams.setMargins(14, 0, 14, 0)
        editText.layoutParams = editTextParams

        editTextParams.startToStart = LayoutParams.PARENT_ID
        editTextParams.endToEnd = LayoutParams.PARENT_ID
        editTextParams.topToTop = LayoutParams.PARENT_ID
        editTextParams.bottomToBottom = LayoutParams.PARENT_ID

        val maxTextViewParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_CONSTRAINT)
        maxTextViewParams.setMargins(0,13, 17, 13)
        textLength.layoutParams = maxTextViewParams

        maxTextViewParams.bottomToBottom = LayoutParams.PARENT_ID
        maxTextViewParams.endToEnd = LayoutParams.PARENT_ID
        maxTextViewParams.topToTop = LayoutParams.PARENT_ID
    }

    private fun setupTextChangedListener() {
        editText.addTextChangedListener(CommonTextWatcher(
            afterChanged = {text ->
                text?.let {
                    textLength.text = getTextStyle("${text.length} / ${count}")
                }
            }
        ))
    }

    private fun getTextStyle(text:String):SpannableString{
        var endIndex = text.indexOf(" ")
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            ForegroundColorSpan(Color.RED), // Set the color to red
            0, // Starting index of the specific text
            endIndex,   // Ending index of the specific text
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun setMaxLength(count:Int){
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(count))
        textLength.text = "0 / ${count}"
        this.count = count
    }

    fun setHint(text:String){
        editText.hint = text
    }
    fun setText(text: CharSequence) {
        editText.setText(text)
    }

    fun getText(): CharSequence {
        return editText.text
    }

    // Extension function to convert dp to pixels
    private fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }
}