package com.cmc.recipe.presentation.ui.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmc.recipe.R
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.databinding.FragmentInnerView2Binding


class InnerViewFragment2 : BaseFragment<FragmentInnerView2Binding>(FragmentInnerView2Binding::inflate) {
    override fun initFragment() {
        val adapter = RefrigeratorItemAdapter(requireContext())

        val itemList = arrayListOf(
            getString(R.string.spices),getString(R.string.baking),
            getString(R.string.can),getString(R.string.instant), getString(R.string.etc)
        )
        adapter.replaceData(itemList)
        binding.rvAmbient.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(context,2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val size = itemList.size
                return if(size%2 ==1 && position == size-1){
                    2
                }else{
                    1
                }
            }

        }
        binding.rvAmbient.adapter = adapter
        binding.rvAmbient.layoutManager = gridLayoutManager

        binding.rvAmbient.addItemDecoration(object: RecyclerView.ItemDecoration(){
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val space = 10
                val spanCount = 2
                val position = parent.getChildAdapterPosition(view)
                val column = position % spanCount + 1      // 1부터 시작

                if (position < spanCount){
                    outRect.top = space
                }
                if (column == spanCount){
                    outRect.right = space
                }
                outRect.left = space
                outRect.bottom = space
            }
        })
        binding.rvAmbient.isNestedScrollingEnabled = false
    }
}