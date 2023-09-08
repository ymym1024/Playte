package com.cmc.recipe.presentation.ui.mypage

import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.Notice
import com.cmc.recipe.databinding.FragmentMypageQnaBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment

class MypageQnaFragment : BaseFragment<FragmentMypageQnaBinding>(FragmentMypageQnaBinding::inflate) {
    override fun initFragment() {
        val itemList = arrayListOf(
            Notice(1,"Q. 계정 탈퇴가 안될 때는 어떻게 해야 하나요? ","A. 계정 탈퇴는 마이페이지>설정>회원탈퇴를 통해 이루\n" +
                    "어집니다. 해당 방법으로 재시도 한 경우에도 계정 탈퇴 방법을 찾지 못한 경우 고객센터로 연락 바랍니다. "),
            Notice(2,"Q. 내가 작성한 리뷰를 수정하고 싶어요. ","A. 내가 작성한 리뷰는 마이페이지>내가 쓴 리뷰에 들어가면 수정 및 삭제가 가능합니다. "),
            Notice(3,"Q. 내 레시피 작성 시, 단계 조절은 어떻게 해야 할까요?","A. 레시피 작성 페이지에서 조리단계를 작성하는 위치 오른쪽의 직선 아이콘을 3초 이상 누르게 되면 상하이동이 가능합니다. 원하는 레시피의 내용을 작성 후 위의 방법으로 단계를 조절할 수 있습니다."),
        )

        val adapter = AccordionAdapter()
        binding.rvQna.adapter = adapter
        binding.rvQna.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        adapter.replaceData(itemList)
    }

}