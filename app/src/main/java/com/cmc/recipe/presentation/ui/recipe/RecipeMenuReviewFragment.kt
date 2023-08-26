package com.cmc.recipe.presentation.ui.recipe

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.RecipeReview
import com.cmc.recipe.data.model.response.ReviewContent
import com.cmc.recipe.data.model.response.ReviewData
import com.cmc.recipe.databinding.FragmentRecipeMenuReviewBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.presentation.ui.common.ImageAdapter
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.NetworkState
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RecipeMenuReviewFragment : BaseFragment<FragmentRecipeMenuReviewBinding>(FragmentRecipeMenuReviewBinding::inflate) {
    private val recipeViewModel : RecipeViewModel by viewModels()
    private var recipeId: Int = 0

    override fun initFragment() {
        arguments?.let {
            recipeId = it.getInt("recipeId", -1)
        }
        requestReview(recipeId)

    }

    private fun requestReview(id:Int){
        launchWithLifecycle(lifecycle) {
            recipeViewModel.getRecipesReview(id)
            recipeViewModel.reviewResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                initDataBinding(data.data)
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        recipeViewModel._reviewResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        recipeViewModel._reviewResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initDataBinding(data: ReviewData) {
        val url = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg"

        val itemList = arrayListOf(
            RecipeReview(nick = "냉파 CMC", date = "2023.05.11", content = "기름이 너무 많아서 망했습니다. 맛은 있는데 약간 짜고 느끼한\n" +
                    "감이 좀 있어요. 그래도 다들 한번씩 해보길", stars = 3, thumb_up = 2, thumb_down = 2,image_list = arrayListOf(url,url,url)),
            RecipeReview(nick = "냉파 CMC", date = "2023.05.11", content = "기름이 너무 많아서 망했습니다. 맛은 있는데 약간 짜고 느끼한\n" +
                    "감이 좀 있어요. 그래도 다들 한번씩 해보길", stars = 3, thumb_up = 2, thumb_down = 2,image_list = arrayListOf(url,url)),
            RecipeReview(nick = "냉파 CMC", date = "2023.05.11", content = "기름이 너무 많아서 망했습니다. 맛은 있는데 약간 짜고 느끼한\n" +
                    "감이 좀 있어요. 그래도 다들 한번씩 해보길", stars = 3, thumb_up = 2, thumb_down = 2,image_list = arrayListOf(url,url,url))
        )

        binding.ratingbar.rating = 4f
        initRV(data.content as ArrayList<ReviewContent>)

        initImageRV()

        initBarChart(binding.reviewChart)
        setChartData(binding.reviewChart)

        binding.btnImgReview.setOnClickListener {
            goImageActivity()
        }
    }

    private fun initRV(itemList:ArrayList<ReviewContent>){
        val clickListener = object : OnClickListener {
            override fun onMovePage(id: Int) {
            }
        }

        val adapter = RecipeMenuReviewAdapter(clickListener,clickListener)
        binding.rvReview.adapter = adapter
        binding.rvReview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        binding.rvReview.addItemDecoration(dividerItemDecoration)
        adapter.replaceData(itemList)
    }

    private fun initBarChart(barChart: BarChart) {
        barChart.setDrawBarShadow(true) // 막대 그림자 설정 (default = false)
        barChart.setDrawBorders(false) // 차트 테두리 설정 (default = false)
        barChart.description.isEnabled = false
        barChart.setTouchEnabled(false)
        barChart.legend.isEnabled = false
        barChart.setMaxVisibleValueCount(5)

        val xLabels: MutableList<String> = ArrayList()

        for (i in 6 downTo 1) {
            xLabels.add("${i}점")
        }

        // 바텀 좌표 값
        barChart.xAxis.run { // 아래 라벨 x축
            isEnabled = true // 라벨 표시 설정
            position = XAxis.XAxisPosition.BOTTOM // 라벨 위치 설정
            setDrawGridLines(false) // 격자구조
            granularity = 1f // 간격 설정
            setDrawAxisLine(false) // 그림
            textSize = 12f // 라벨 크기
            valueFormatter = IndexAxisValueFormatter(xLabels) // 라벨 포맷
            textColor = ContextCompat.getColor(requireContext(), R.color.primary_color)
        }

        barChart.axisLeft.run { // 왼쪽 y축
            isEnabled = false
            axisMinimum = 0f // 최소값
            axisMaximum = 100f // 최대값
            granularity = 10f // 값 만큼 라인선 설정
            setDrawLabels(false) // 값 셋팅 설정
        }
        barChart.axisRight.run { // 오른쪽 y축(왼쪽과동일)
            isEnabled = false
            setDrawAxisLine(false)
        }

    }

    private fun setChartData(barChart: BarChart) {

        // Zoom In / Out 가능 여부 설정
        barChart.setScaleEnabled(false)

        val values: ArrayList<BarEntry> = ArrayList()
        val MAX_Y_VALUE = 100
        val MIN_Y_VALUE = 10

        for (i in 5 downTo 1) {
            val x = i.toFloat()
            val y: Float = Random().nextFloat() * (MAX_Y_VALUE - MIN_Y_VALUE) + MIN_Y_VALUE
            values.add(BarEntry(x, y))
        }

        // 2. [BarDataSet] 단순 데이터를 막대 모양으로 표시, BarChart의 막대 커스텀

        val set2 = BarDataSet(values, "")
            .apply {
                setDrawIcons(false)
                setDrawValues(true)
                color = ContextCompat.getColor(requireContext(), R.color.primary_color)
                barShadowColor = ContextCompat.getColor(requireContext(), R.color.gray_11)
            }

        // 3. [BarData] 보여질 데이터 구성
        val data = BarData(set2)
        data.barWidth = 0.5f
        data.setValueTextSize(15f)


        barChart.data = data
        barChart.invalidate()
    }

    private fun goImageActivity(){
        movePage(R.id.action_recipeMenuFragment_to_imageReviewActivity)
    }

    private fun initImageRV(){
        val imageList = arrayListOf(
            "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",
            "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",
            "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",
        )
        val adapter = ImageAdapter(80)
        binding.rvImage.adapter = adapter
        binding.rvImage.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.replaceData(imageList)
    }
}