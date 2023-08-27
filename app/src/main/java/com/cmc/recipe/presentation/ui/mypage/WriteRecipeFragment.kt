package com.cmc.recipe.presentation.ui.mypage

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.databinding.FragmentWriteRecipeBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.common.CustomBottomSheetFragment
import com.cmc.recipe.presentation.ui.recipe.RecipeItemHolder
import com.cmc.recipe.presentation.ui.recipe.RecipeListAdapter
import com.cmc.recipe.presentation.viewmodel.MyPageViewModel
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WriteRecipeFragment : BaseFragment<FragmentWriteRecipeBinding>(FragmentWriteRecipeBinding::inflate) {

    private val myPageViewModel : MyPageViewModel by viewModels()
    private lateinit var adapter : RecipeListAdapter

    override fun initFragment() {

        requestSaveRecipe()

    }
    private fun requestSaveRecipe(){
        launchWithLifecycle(lifecycle) {
            myPageViewModel.getWrittenRecipe()
            myPageViewModel.writtenRecipeResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data.let { data ->
                            if(data.code == "SUCCESS"){
                                recipeRecyclerview(it.data.data as ArrayList<RecipeItem>)
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        myPageViewModel._writtenRecipeResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        myPageViewModel._writtenRecipeResult.value  = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    private fun requestDeleteRecipe(item:RecipeItem){
        myPageViewModel.deleteRecipe(item.recipe_id)
        viewLifecycleOwner.lifecycleScope.launch {
            myPageViewModel.recipeDeleteResult.take(1).onEach{
                when(it) {
                    is NetworkState.Success -> {
                        if (it.data.code == "SUCCESS") {
                            adapter.removeItem(item)
                            showToastMessage("레시피가 삭제되었습니다")
                        }
                        showToastMessage("${it.data}")
                    }
                    is NetworkState.Error -> {
                        showToastMessage("레시피 삭제에 실패했습니다. ${it.message.toString()}")
                    }
                    is NetworkState.Loading -> {
                        // 프로그레스바 띄우기
                    }
                    else -> {
                        showToastMessage("${it}")
                    }
                }
            }
                .launchIn(this)
        }
    }

    private fun recipeRecyclerview(itemList:ArrayList<RecipeItem>){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {

            }
        }

        val bottomSheetFragment = CustomBottomSheetFragment()
        bottomSheetFragment.setTitle("삭제하시겠습니까?")

        adapter = RecipeListAdapter(clickListener)
        adapter.setType(Constant.WRITE)
        adapter.setListener(object : RecipeItemHolder.onActionListener{
            override fun action(item: RecipeItem) {
                bottomSheetFragment.setItemToDelete(item)
                bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
            }
        })

        binding.rvRecipe.adapter = adapter
        binding.rvRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }

    fun CustomBottomSheetFragment.setItemToDelete(item:RecipeItem) {
        setListener {
            requestDeleteRecipe(item) // 선택된 아이템으로 삭제 요청 보냄
        }
    }

}