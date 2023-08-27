package com.cmc.recipe.presentation.ui.mypage

import android.util.Log
import androidx.fragment.app.viewModels
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

@AndroidEntryPoint
class WriteRecipeFragment : BaseFragment<FragmentWriteRecipeBinding>(FragmentWriteRecipeBinding::inflate) {

    private val myPageViewModel : MyPageViewModel by viewModels()

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

    private fun recipeRecyclerview(itemList:ArrayList<RecipeItem>){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {

            }
        }

        val adapter = RecipeListAdapter(clickListener)
        adapter.setType(Constant.WRITE)
        adapter.setListener(object : RecipeItemHolder.onActionListener{
            override fun action(item: RecipeItem) {
                // bottom sheet show
                CustomBottomSheetFragment().show(fragmentManager!!, "RemoveBottomSheetFragment")
            }
        })

        binding.rvRecipe.adapter = adapter
        binding.rvRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)
    }

}