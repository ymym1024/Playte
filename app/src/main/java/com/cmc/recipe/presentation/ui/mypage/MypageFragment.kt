package com.cmc.recipe.presentation.ui.mypage


import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.cmc.recipe.R
import com.cmc.recipe.databinding.FragmentMypageBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.viewmodel.AuthViewModel
import com.cmc.recipe.presentation.viewmodel.MyPageViewModel
import com.cmc.recipe.presentation.viewmodel.UserViewModel
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate) {

    private val userViewModel : UserViewModel by viewModels()

    override fun initFragment() {

        initMenu()
        initView()

        reqeustMyInfo()

        recipeRecyclerview()

        shortsRecyclerview()

    }

    private fun reqeustMyInfo(){
        userViewModel.getMyInfo()
        launchWithLifecycle(lifecycle) {
            userViewModel.myInfoResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                //닉네임, 아이디 설정
                                binding.tvNick.text = it.data.data.nickname
                                binding.tvEmail.text = it.data.data.email
                            }
                        }
                        userViewModel._myInfoResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        userViewModel._myInfoResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initView() {
        binding.let {
            it.btnSaveRecipe.setOnClickListener {
                moveMyPage("saveRecipeFragment")
            }
            it.btnWriteRecipe.setOnClickListener {
                moveMyPage("writeRecipeFragment")
            }
            it.btnMyReview.setOnClickListener {
                moveMyPage("myReviewFragment")
            }

        }
    }

    private fun moveMyPage(destination:String){
        val action = MypageFragmentDirections.actionMypageFragmentToMypageActivity(destination)
        findNavController().navigate(action)
    }

    private fun initMenu(){
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_mypage, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_edit_button -> {
                        moveMyPage("settingFragment")
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun recipeRecyclerview(){
//        val itemList = arrayListOf(
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = false),
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = false),
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
//        )
//
//        val adapter = RecipeRecommendAdapter(requireContext())
//        binding.rvViewRecipe.adapter = adapter
//        binding.rvViewRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
//        adapter.replaceData(itemList)
    }

    private fun shortsRecyclerview(){
//        val itemList = arrayListOf(
//            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
//            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
//            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
//            SearchShorts(shorts_thumb = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",shorts_nick = "codud01", shorts_title = "토마토 계란볶음밥 쉽게...", shorts_time = "dd:dd"),
//        )
//
//        val clickListener = object : OnClickListener {
//            override fun onMovePage(id: Int) {
//
//            }
//        }
//
//        val adapter = SearchShortsAdapter(clickListener)
//        binding.rvViewShorts.adapter = adapter
//        binding.rvViewShorts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        adapter.replaceData(itemList)
    }

}