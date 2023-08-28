package com.cmc.recipe.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.cmc.recipe.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDetailDialog : BottomSheetDialogFragment() {

    private var reportListener: (() -> Unit)? = null
    private var noshowListener: (() -> Unit)? = null

    fun setReportListener(reportListener: () -> Unit) {
        this.reportListener = reportListener
    }

    fun setNoshowListener(noshowListener: () -> Unit) {
        this.noshowListener = noshowListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.bottom_sheet_recipe_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noFavoriteButton: ConstraintLayout = view.findViewById(R.id.btn_no_favorite)
        noFavoriteButton.setOnClickListener {
            reportListener?.invoke()
            dismiss()
        }

        val alertButton : ConstraintLayout = view.findViewById(R.id.btn_alert)
        alertButton.setOnClickListener {
            noshowListener?.invoke()
            dismiss()
        }

    }

    override fun getTheme(): Int = R.style.BottomSheetDialog
}