package com.cmc.recipe.presentation.ui.mypage

import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.Notice
import com.cmc.recipe.databinding.FragmentMypageNoticeBinding
import com.cmc.recipe.databinding.FragmentMypageTermsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment


class MypageNoticeFragment : BaseFragment<FragmentMypageNoticeBinding>(FragmentMypageNoticeBinding::inflate) {
    override fun initFragment() {
        val itemList = emptyList<Notice>()

        val adapter = AccordionAdapter()
        binding.rvNotice.adapter = adapter
        binding.rvNotice.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        adapter.replaceData(itemList)
    }

}