package com.cmc.recipe.presentation.ui.mypage


import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeMapper.toRecipe
import com.cmc.recipe.data.model.entity.ShortsEntity
import com.cmc.recipe.data.model.response.RecommendationRecipe
import com.cmc.recipe.databinding.FragmentMypageBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.recipe.RecipeActivity
import com.cmc.recipe.presentation.ui.recipe.RecipeRecommendAdapter
import com.cmc.recipe.presentation.ui.shortform.ShortsDetailActivity
import com.cmc.recipe.presentation.viewmodel.*
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate) {

    private val userViewModel : UserViewModel by viewModels()
    private val recipeViewModel : RecipeViewModel by viewModels()
    private val shortsViewModel : ShortsViewModel by viewModels()

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

    private fun moveDetailPage(id:Int){
        val intent = Intent(requireContext(), RecipeActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
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
        val adapter = RecipeRecommendAdapter()
        adapter.setListener(object : OnClickListener{
            override fun onMovePage(id: Int) {
                moveDetailPage(id)
            }
        })

        launchWithLifecycle(lifecycle){
            recipeViewModel.loadRecentRecipes()
            var itemList : MutableList<RecommendationRecipe> = arrayListOf()

            recipeViewModel.recentRecipeResult.collect{ list ->
                for (item in list){
                    itemList.add(item.toRecipe())
                }
                binding.rvViewRecipe.adapter = adapter
                binding.rvViewRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                adapter.replaceData(itemList)
            }
        }
    }

    private fun shortsRecyclerview(){
        val adapter = MypageShortsAdapter()
        adapter.setListener(object : OnClickListener{
            override fun onMovePage(id: Int) {
                moveShortsPage(id)
            }

        })

        launchWithLifecycle(lifecycle){
            shortsViewModel.loadRecentRecipes()
            var itemList : MutableList<ShortsEntity> = arrayListOf()

            shortsViewModel.recentShortsResult.collect{ list ->
                Log.d("recentRecipeResult","${list}")
                for (item in list){
                    itemList.add(item)
                }
                binding.rvViewShorts.adapter = adapter
                binding.rvViewShorts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                adapter.replaceData(itemList)
            }
        }
    }

    private fun moveShortsPage(position:Int){
        val intent = Intent(requireContext(), ShortsDetailActivity::class.java)
        intent.putExtra("detailId",position)
        startActivity(intent)
    }

}