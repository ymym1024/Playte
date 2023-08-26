package com.cmc.recipe.presentation.ui.recipe

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.Product
import com.cmc.recipe.data.model.response.RecipeDetail
import com.cmc.recipe.data.model.response.RecipeIngredient
import com.cmc.recipe.data.model.response.RecommendationRecipe
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

        val id = arguments?.getInt("id")
        Log.d("id",id.toString())

        requestRecipeDetail(id!!)
    }

    private fun requestRecipeDetail(id:Int){
        launchWithLifecycle(lifecycle) {
            recipeViewModel.getRecipesDetail(id)
            recipeViewModel.recipeDetailResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
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
        binding.tvRecipeDate.text = data.created_date.parseAndFormatDate()

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
        initRecommendRV(data.recommendation_recipes)
        initProductRV(data.ingredients)

    }

    private fun initMenu(){
        // 프래그먼트 내에서 투명한 상태 표시줄 설정
        requireActivity().setStatusBarTransparent()

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

    private fun initRecipeIngredientRV(itemList:List<RecipeIngredient>){
        val ingredientList = itemList.filter { it.ingredient_type == "INGREDIENTS" }
        val sauceList = itemList.filter { it.ingredient_type == "SAUCE" }

        val adapter = RecipeIngredientAdapter()
        binding.rvIngredient.adapter = adapter
        binding.rvIngredient.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(ingredientList)

        val adapter1 = RecipeIngredientAdapter()
        binding.rvSpices.adapter = adapter1
        binding.rvSpices.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter1.replaceData(sauceList)
    }

    private fun initRecipeRV(stage : List<Stage>){
        val adapter = RecipeOrderAdapter()
        binding.rvRecipeList.adapter = adapter
        binding.rvRecipeList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.replaceData(stage)
    }

    private fun initProductRV(productList: List<RecipeIngredient>) {
        val itemList = productList.map{
            Product(it.coupang_product_image,it.coupang_product_name,it.coupang_product_price,it.coupang_product_url)
        }

        val adapter = ShortsProductAdapter(object : ShortsProductItemHolder.OnClickListener{
            override fun onMoveSite(url: String) {

            }
        })
        binding.rvRecipeProduct.adapter = adapter
        binding.rvRecipeProduct.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.replaceData(itemList)
    }

    private fun initRecommendRV(recommendationRecipes: List<RecommendationRecipe>) {
        val adapter = RecipeRecommendAdapter()
        binding.rvRecommendRecipe.adapter = adapter
        binding.rvRecommendRecipe.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.replaceData(recommendationRecipes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().setStatusBarOrigin()
    }
}