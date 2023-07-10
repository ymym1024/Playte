package com.cmc.recipe.presentation.ui.home

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.databinding.FragmentTrendDetailBinding
import com.cmc.recipe.data.model.TrendsItem


class TrendDetailFragment : BaseFragment<FragmentTrendDetailBinding>(FragmentTrendDetailBinding::inflate) {
    override fun initFragment() {
        //TODO : 네트워크 연결 후 삭제
        val itemList = arrayListOf<TrendsItem>(
            TrendsItem("","유제품","계란","+8원(0.4%)","1개","05/13기준","214원"),
            TrendsItem("","유제품","계란","+8원(0.4%)","1개","05/13기준","214원"),
            TrendsItem("","유제품","계란","+8원(0.4%)","1개","05/13기준","214원"),
            TrendsItem("","유제품","계란","+8원(0.4%)","1개","05/13기준","214원"),
            TrendsItem("","유제품","계란","+8원(0.4%)","1개","05/13기준","214원"),
        )
        initList(itemList)
    }

    private fun initList(itemList:ArrayList<TrendsItem>){
        val adapter = TrendsDetailAdapter(requireContext())
        binding.rvTrends.adapter = adapter
        binding.rvTrends.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        binding.rvTrends.addItemDecoration(dividerItemDecoration)
        adapter.replaceData(itemList)
    }

}