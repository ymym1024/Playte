package com.cmc.recipe.presentation.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.cmc.recipe.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CustomBottomSheetFragment : BottomSheetDialogFragment() {

    private var title: String? = null
    private var actionListener: (() -> Unit)? = null

    fun setTitle(title: String) {
        this.title = title
    }

    // Function to set the listener
    fun setListener(actionListener: () -> Unit) {
        this.actionListener = actionListener
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.bottom_sheet_remove, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val infoTextView: TextView = view.findViewById(R.id.tv_title)
        title?.let {
            infoTextView.text = it
        }

        val noButton: Button = view.findViewById(R.id.btn_no)
        noButton.setOnClickListener {
            dismiss()
        }

        val actionButton : Button = view.findViewById(R.id.btn_yes)
        actionButton.setOnClickListener {
            actionListener?.invoke()
            dismiss()
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialog
}