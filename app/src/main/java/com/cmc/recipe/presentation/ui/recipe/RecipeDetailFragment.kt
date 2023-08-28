package com.cmc.recipe.presentation.ui.recipe

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.Product
import com.cmc.recipe.data.model.response.RecipeDetail
import com.cmc.recipe.data.model.response.RecipeIngredient
import com.cmc.recipe.data.model.response.RecommendationRecipe
import com.cmc.recipe.data.model.response.Stage
import com.cmc.recipe.databinding.FragmentRecipeDetailBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.search.SearchActivity
import com.cmc.recipe.presentation.ui.shortform.ShortsProductAdapter
import com.cmc.recipe.presentation.ui.shortform.ShortsProductItemHolder
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment : BaseFragment<FragmentRecipeDetailBinding>(FragmentRecipeDetailBinding::inflate) {
    private val recipeViewModel : RecipeViewModel by viewModels()
    private var recipeId : Int = 0
    private var recipeImg : String = ""

    // id 전달 받기
    override fun onDestroyView() {
        super.onDestroyView()
        val activity = activity as RecipeActivity
        activity.hideToolbar(false)
        requireActivity().setStatusBarOrigin()
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().setStatusBarOrigin()
    }

    override fun onResume() {
        super.onResume()
        Log.d("여기 확인","onResume")

        initMenu()
    }

    override fun initFragment() {
        val activity = activity as RecipeActivity
        activity.hideToolbar(true)

        recipeId = arguments?.getInt("id")!!

        recipeViewModel.updateReicpeId(recipeId)
        requestRecipeDetail(recipeId!!)
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

        // 레시피 id
        recipeId = data.recipe_id
        recipeImg = data.recipe_thumbnail_img

        binding.ivThumb.loadImagesWithGlide(recipeImg)
        binding.tvNickname.text = data.writtenby
        binding.tvRecipeTitle.text = data.recipe_name
        binding.tvRecipeInfo.text = data.recipe_description
        binding.tvRecipeDate.text = data.created_date.parseAndFormatDate()

        // 상세정보 바인딩
        binding.tvScore.text = "${data.rating}"
        binding.tvPeople.text = "${data.serving_size}인분"
        binding.tvTime.text = "${data.cook_time}분"

        if(!data.is_saved) binding.ibBookmark.setImageResource(R.drawable.ic_bookmark_deactive)
        else binding.ibBookmark.setImageResource(R.drawable.ic_bookmark_activate)

        binding.tvPeople.setOnClickListener {
            showBottomSheet()
        }
        binding.btnReview.setOnClickListener {
            val action = RecipeDetailFragmentDirections.actionRecipeDetailFragmentToRecipeMenuFragment(recipeId)
            findNavController().navigate(action)
        }

        binding.btnWriteReview.setOnClickListener {
//            val action = RecipeDetailFragmentDirections.actionRecipeDetailFragmentToReviewRegisterActivity(recipeId = recipeId, recipeImg = recipeImg)
//            findNavController().navigate(action)
            val intent = Intent(requireContext(), ReviewRegisterActivity::class.java)
            intent.putExtra("recipeId", recipeId)
            intent.putExtra("recipeImg", recipeImg)
            startActivity(intent)
        }

        initRecipeRV(data.stages)
        initRecipeIngredientRV(data.ingredients)
        initRecommendRV(data.recommendation_recipes)
        initProductRV(data.ingredients)

        // 이벤트 바인딩

    }

    private fun initMenu(){
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

}