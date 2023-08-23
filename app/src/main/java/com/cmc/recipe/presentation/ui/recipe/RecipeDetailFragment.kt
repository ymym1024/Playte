package com.cmc.recipe.presentation.ui.recipe

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.MainApplication
import com.cmc.recipe.R
import com.cmc.recipe.data.IngredientItem
import com.cmc.recipe.data.model.Product
import com.cmc.recipe.data.model.RecipeOrder
import com.cmc.recipe.data.model.response.Ingredient
import com.cmc.recipe.data.model.response.RecipeDetail
import com.cmc.recipe.data.model.response.Stage
import com.cmc.recipe.databinding.FragmentRecipeDetailBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.shortform.ShortsProductAdapter
import com.cmc.recipe.presentation.ui.shortform.ShortsProductItemHolder
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment : BaseFragment<FragmentRecipeDetailBinding>(FragmentRecipeDetailBinding::inflate) {
    private val recipeViewModel : RecipeViewModel by viewModels()

    // id 전달 받기
    override fun onStop() {
        super.onStop()

        val activity = activity as RecipeActivity
        activity.hideToolbar(false)
    }

    override fun initFragment() {
        val activity = activity as RecipeActivity
        activity.hideToolbar(true)

        initMenu()

        requestRecipeDetail(1)

    }

    private fun requestRecipeDetail(id:Int){
        launchWithLifecycle(lifecycle) {
            recipeViewModel.getRecipesDetail(id)
            recipeViewModel.recipeDetailResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                Log.d("data",data.data.toString())
                                initDatabinding(data.data)
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        recipeViewModel._recipeResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        recipeViewModel._recipeResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }
    private fun initDatabinding(data:RecipeDetail){

        binding.ivThumb.loadImagesWithGlide(data.recipe_thumbnail_img)
        binding.tvNickname.text = ""
        binding.tvRecipeTitle.text = data.recipe_name
        binding.tvRecipeInfo.text = data.recipe_description
        binding.tvRecipeDate.text = data.created_date

        // 상세정보 바인딩
        binding.tvScore.text = "${data.rating}"
        binding.tvPeople.text = "${1}인분"
        binding.tvTime.text = "${data.cook_time}분"

        binding.tvPeople.setOnClickListener {
            showBottomSheet()
        }
        binding.btnReview.setOnClickListener {
            movePage(R.id.action_recipeDetailFragment_to_recipeMenuFragment)
        }

        binding.btnWriteReview.setOnClickListener {
            movePage(R.id.action_recipeDetailFragment_to_recipeReviewFragment)
        }

        initRecipeRV(data.stages)
        initRecipeIngredientRV(data.ingredients)
        initRecommendRV()
        initProductRV()

    }

    private fun initMenu(){
        // 프래그먼트 내에서 투명한 상태 표시줄 설정
        requireActivity().setStatusBarTransparent()
        Log.d("initMenu test","${requireContext().navigationHeight()}")

        binding.innerContainer.setPadding(
            0,
            requireContext().statusBarHeight(),
            0,
            0
        )

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnMore.setOnClickListener {
            showBottomSheet()
        }

    }

    private fun showBottomSheet(){
        BottomSheetDetailDialog().show(fragmentManager!!, "RemoveBottomSheetFragment")
    }

    private fun initRecipeIngredientRV(list:List<Ingredient>){
//        val itemList = arrayListOf(
//            IngredientItem(name = "토마토", cnt = "3개"),
//            IngredientItem(name = "계란", cnt = "3개"),
//            IngredientItem(name = "대파", cnt = "3개"),
//        )
//
//        val itemList1 = arrayListOf(
//            IngredientItem(name = "굴소스", cnt = "2T"),
//            IngredientItem(name = "소금", cnt = "2T"),
//        )

        val itemList = list.filter { it.ingredient_type == "INGREDIENTS" }
        val itemList1 = list.filter { it.ingredient_type == "*" }

        val adapter = RecipeIngredientAdapter()
        binding.rvIngredient.adapter = adapter
        binding.rvIngredient.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(itemList)

        val adapter1 = RecipeIngredientAdapter()
        binding.rvSpices.adapter = adapter1
        binding.rvSpices.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter1.replaceData(itemList1)
    }

    private fun initRecipeRV(stage : List<Stage>){
        val adapter = RecipeOrderAdapter()
        binding.rvRecipeList.adapter = adapter
        binding.rvRecipeList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(stage)
    }

    private fun initProductRV(){
        val itemList = arrayListOf(
            Product(image = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",name="전남 국내산 대추방...", price = 18000),
            Product(image = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",name="전남 국내산 대추방...", price = 18000),
            Product(image = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",name="전남 국내산 대추방...", price = 18000),
        )

        val adapter = ShortsProductAdapter(object : ShortsProductItemHolder.OnClickListener{
            override fun onMoveSite(url: String) {

            }
        })
        binding.rvRecipeProduct.adapter = adapter
        binding.rvRecipeProduct.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.replaceData(itemList)
    }

    private fun initRecommendRV(){
//        val itemList = arrayListOf(
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
//            RecipeItem(image_url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg", name = "토마토 계란 볶음밥", time = 10, nickName = "구땡뿡야",star=30, flag = true),
//        )

        val adapter = RecipeRecommendAdapter()
        binding.rvRecommendRecipe.adapter = adapter
        binding.rvRecommendRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //adapter.replaceData(itemList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().setStatusBarOrigin()
    }
}