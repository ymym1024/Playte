package com.cmc.recipe.presentation.ui.recipe

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.response.ReviewContent
import com.cmc.recipe.data.model.response.ReviewData
import com.cmc.recipe.data.model.response.ScoreData
import com.cmc.recipe.databinding.FragmentRecipeMenuReviewBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.common.ImageAdapter
import com.cmc.recipe.presentation.ui.common.OnReviewListener
import com.cmc.recipe.presentation.ui.common.RecipeSnackBar
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.NetworkState
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class RecipeMenuReviewFragment : BaseFragment<FragmentRecipeMenuReviewBinding>(FragmentRecipeMenuReviewBinding::inflate) {
    private val recipeViewModel : RecipeViewModel by viewModels()
    private var recipeId: Int = 0
    private lateinit var reviewAdapter : RecipeMenuReviewAdapter
    private lateinit var reviewItemList : ArrayList<ReviewContent>

    override fun initFragment() {
        arguments?.let {
            recipeId = it.getInt("recipeId", -1)
        }
        requestScore(recipeId)
        requestReview(recipeId)
    }

    private fun requestScore(id:Int){
        launchWithLifecycle(lifecycle) {
            recipeViewModel.getRecipesReviewScores(id)
            recipeViewModel.reviewScoreResult.collect {
                when (it) {
                    is NetworkState.Success -> {
                        it.data?.let { data ->
                            if (data.code == "SUCCESS") {
                                binding.tvRating.text = "${data.data.totalRating}"
                                binding.ratingbar.rating = data.data.totalRating.toFloat()
                                initBarChart(binding.reviewChart)
                                setChartData(binding.reviewChart, data.data)
                            } else {
                                Log.d("data", "${data.data}")
                            }
                        }
                        recipeViewModel._reviewResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error -> {
                        showToastMessage(it.message.toString())
                        recipeViewModel._reviewResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
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
        reviewItemList = data.content as ArrayList<ReviewContent>
        initRV(reviewItemList)

        val filteredImages: List<String> = data.content.filter { it.review_images.isNotEmpty() }.flatMap { it.review_images }
        initImageRV(filteredImages)
        binding.tvReviewImageCnt.text = "${filteredImages.count()}개"
        binding.tvReviewCount.text  = "${data.content.count()}개"

        binding.btnImgReview.setOnClickListener {
            goImageActivity()
        }

        binding.btnWriteReview.setOnClickListener {
           // movePage(R.id.action_recipeMenuFragment_to_recipeReviewFragment)
        }
    }

    private fun initRV(itemList:ArrayList<ReviewContent>){
        val clickListener = object : OnReviewListener {
            override fun onFavorite(id: Int){
                requestReviewLikeOrUnLike(id)
            }

            override fun onReport(id: Int) {
                requestReviewReport(id)
            }
        }

        reviewAdapter = RecipeMenuReviewAdapter(clickListener)
        binding.rvReview.adapter = reviewAdapter
        binding.rvReview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        binding.rvReview.addItemDecoration(dividerItemDecoration)
        reviewAdapter.replaceData(itemList)
    }

    private fun requestReviewLikeOrUnLike(id: Int) {
        val review = findReviewItemById(id)
        if (review != null) {
            if (review.liked) {
                requestReviewUnLike(id)
            } else {
                requestReviewLike(id)
            }
        }
    }

    private fun findReviewItemById(reviewId: Int): ReviewContent? {
        return reviewItemList.find { it.review_id == reviewId }
    }

    private fun requestReviewReport(id:Int){
        recipeViewModel.postReviewReport(id)
        viewLifecycleOwner.lifecycleScope.launch{
            recipeViewModel.reviewReportResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS" && data.data.toString().toBoolean()){
                                Log.d("data","${data}")
                                // 리뷰 신고 시 해당 리뷰 안보이게
                                reviewAdapter.removeItem(id)
                                binding.tvReviewCount.text = "${reviewAdapter.itemCount}개"
                                RecipeSnackBar(binding.btnWriteReview,"리뷰가 정상적으로 신고되었습니다").show()
                            }
                        }
                        recipeViewModel._reviewResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        Log.d("data","${it.message}")
                        showToastMessage(it.message.toString())
                        recipeViewModel._reviewResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    private fun requestReviewLike(id:Int){
        recipeViewModel.updateReviewLike(id)
        viewLifecycleOwner.lifecycleScope.launch{
            recipeViewModel.reviewLikeResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                Log.d("data","${data}")
                                val review =findReviewItemById(id)
                                review?.liked = true
                                review?.like_count = review?.like_count!! + 1
                                reviewAdapter.notifyDataSetChanged()

                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        recipeViewModel._reviewResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        Log.d("data","${it.message}")
                        showToastMessage(it.message.toString())
                        recipeViewModel._reviewResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    private fun requestReviewUnLike(id:Int){
        recipeViewModel.updateReviewUnLike(id)
        viewLifecycleOwner.lifecycleScope.launch{
            recipeViewModel.reviewUnLikeResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                Log.d("data-unlike","${data}")
                                val review =findReviewItemById(id)
                                review?.liked = false
                                review?.like_count = review?.like_count!! - 1
                                reviewAdapter.notifyDataSetChanged()

                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        recipeViewModel._reviewResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        Log.d("data","${it.message}")
                        showToastMessage(it.message.toString())
                        recipeViewModel._reviewResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
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

    private fun setChartData(barChart: BarChart, data: ScoreData) {

        // Zoom In / Out 가능 여부 설정
        barChart.setScaleEnabled(false)

        val values: ArrayList<BarEntry> = ArrayList()

        values.add(BarEntry(5f, data.fivePoint.toFloat()))
        values.add(BarEntry(4f, data.fourPoint.toFloat()))
        values.add(BarEntry(3f, data.threePoint.toFloat()))
        values.add(BarEntry(2f, data.twoPoint.toFloat()))
        values.add(BarEntry(1f, data.onePoint.toFloat()))

        // BarDataSet 설정
        val set2 = BarDataSet(values, "")
            .apply {
                setDrawIcons(false)
                setDrawValues(true)
                color = ContextCompat.getColor(requireContext(), R.color.primary_color)
                barShadowColor = ContextCompat.getColor(requireContext(), R.color.gray_11)
            }

        // BarData 설정
        val barData = BarData(set2)
        barData.barWidth = 0.5f
        barData.setValueTextSize(14f)

        // X축 라벨 설정
        val xLabels: MutableList<String> = ArrayList()
        xLabels.add("") // 인덱스 0은 빈 값으로 설정
        xLabels.add("5점")
        xLabels.add("4점")
        xLabels.add("3점")
        xLabels.add("2점")
        xLabels.add("1점")

        barChart.xAxis.run {
            valueFormatter = IndexAxisValueFormatter(xLabels)
        }

        // Y축 설정
        barChart.axisLeft.run {
            isEnabled = false
            axisMinimum = 0f
            axisMaximum = findMaxValue(data)
            granularity = 10f
            setDrawLabels(false)
        }

        barChart.axisRight.isEnabled = false

        // BarChart에 데이터 설정 및 갱신
        barChart.data = barData
        barChart.invalidate()
    }

    fun findMaxValue(data: ScoreData): Float {
        val values = listOf(
            data.fivePoint,
            data.fourPoint,
            data.onePoint,
            data.threePoint,
            data.twoPoint
        )

        return values.maxOrNull() ?: 0.0f
    }

    private fun setChartData2(barChart: BarChart) {

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
        val intent = Intent(activity, ImageReviewActivity::class.java)
        intent.putExtra("recipeId",recipeId)
        startActivity(intent)
    }

    private fun initImageRV(filteredImages: List<String>) {
        val adapter = ImageAdapter(85)
        binding.rvImage.adapter = adapter
        binding.rvImage.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.replaceData(filteredImages)
    }
}