package com.cmc.recipe.presentation.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cmc.recipe.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class RemoveBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.bottom_sheet_remove, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noButton: Button = view.findViewById(R.id.btn_no)
        noButton.setOnClickListener {
            dismiss()
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialog
}