package com.cmc.recipe.presentation.ui.shortform

import BottomSheetCommentFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.cmc.recipe.R
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.data.model.response.CommentContent
import com.cmc.recipe.data.model.response.ShortsContent
import com.cmc.recipe.databinding.ActivityShortsDetailBinding
import com.cmc.recipe.presentation.ui.MainActivity
import com.cmc.recipe.presentation.ui.common.CommentAdapter
import com.cmc.recipe.presentation.ui.common.OnCommentListener
import com.cmc.recipe.presentation.ui.recipe.BottomSheetDetailDialog
import com.cmc.recipe.presentation.viewmodel.CommentViewModel
import com.cmc.recipe.presentation.viewmodel.ShortsViewModel
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShortsDetailActivity : AppCompatActivity() {
    private val shortsViewModel : ShortsViewModel by viewModels()
    private val commentViewModel : CommentViewModel by viewModels()

    private lateinit var binding: ActivityShortsDetailBinding
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()
    private var currentPosition = 0
    private var currentId = 0

    private var isMute = false

    private lateinit var adapter : ShortsDetailAdapter
    private var favoriteFlag : Boolean? = null
    private var saveFlag : Boolean? = null
    private var itemSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShortsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //position 전달 받음
        currentPosition = intent.getIntExtra("position",0)

        requestRecipeList()
        initMenu()
    }

    override fun onPause() {
        super.onPause()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.vpExoplayer.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.vpExoplayer.currentItem }
        Log.d("onResume ","${index}")
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer
                player.clearMediaItems()
                player.release()
            }
            exoPlayerItems.clear()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun requestRecipeList(){
        shortsViewModel.getRecipesShortform()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                shortsViewModel.recipeShortsResult.collect { response ->
                    when (response) {
                        is NetworkState.Success -> {
                            response.data?.let { data ->
                                if (data.code == "SUCCESS") {
                                    itemSize = response.data.data.content.size
                                    initVideo(response.data.data.content as ArrayList<ShortsContent>)
                                }
                            }
                            shortsViewModel._recipeShortsResult.value = NetworkState.Loading
                        }
                        is NetworkState.Error -> {
                            shortsViewModel._recipeShortsResult.value = NetworkState.Loading
                        }
                        else -> {}
                    }
                }
            }
        }
    }
    private fun initMenu(){

        binding.let {
            it.btnSpeak.bringToFront()
            it.btnBack.bringToFront()
            it.btnMore.bringToFront()
        }

        binding.btnBack.setOnClickListener {
            this.onBackPressed()
        }

        binding.btnMore.setOnClickListener {
            showBottomSheet()
        }

        binding.btnSpeak.setOnClickListener {
            if(!isMute){ // mute 아님
                binding.btnSpeak.setImageResource(R.drawable.ic_mute)
                exoPlayerItems[currentPosition].exoPlayer.volume = 0f
                isMute = true
            }else{
                binding.btnSpeak.setImageResource(R.drawable.ic_speak)
                exoPlayerItems[currentPosition].exoPlayer.volume = 0.5f
                isMute = false
            }
        }

    }

    private fun showBottomSheet(){
        val dialog = BottomSheetDetailDialog()
        dialog.setReportListener {   //신고하기
            requestReport(currentId)
        }
        dialog.setNoshowListener {// 관심없음
            postReviewNoInterest(currentId)
        }
        dialog.show(supportFragmentManager,"RemoveBottomSheetFragment")
    }


    private fun initVideo(itemList:ArrayList<ShortsContent>){
        adapter = ShortsDetailAdapter(applicationContext,object : ShortsItemHolder.OnVideoPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)

                if (exoPlayerItems.size == 1) {
                    exoPlayerItem.exoPlayer.playWhenReady = true
                    exoPlayerItem.exoPlayer.play()
                }
            }
        })


        adapter.setShortsListener(object : onShortsListener{
            override fun onFavorite(id:Int) {
                requestShortsLikeOrUnLike(id)
            }

            override fun onSave(id:Int) {
                requestShortsSaveOrUnSave(id)
            }

            override fun onComment(id:Int) {
                requestCommentList(id)
            }
        })

        binding.vpExoplayer.adapter = adapter
        adapter.replaceData(itemList)

        binding.vpExoplayer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentId = adapter.getData().get(position).shortform_id

                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }

                if (previousIndex != -1) {
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                    player.seekTo(0)
                }
                val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                    currentPosition = newIndex
                }
            }

        })
        binding.vpExoplayer.setCurrentItem(currentPosition, false)
    }

    private fun findShortsItemById(shortsId: Int): ShortsContent? {
        return adapter.getData().find { it.shortform_id == shortsId }
    }

    private fun requestFavorite(id:Int) {
        shortsViewModel.postShortformLike(id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                shortsViewModel.shortsLikeResult.collect{
                    when (it) {
                        is NetworkState.Success -> {
                            if (it.data.code == "SUCCESS") {
                                val item = findShortsItemById(id)
                                item?.is_liked = true
                                item?.likes_count = item?.likes_count!! + 1

                                adapter.getData().add(id,item)
                            }
                        }
                        is NetworkState.Error -> {
                            Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is NetworkState.Loading -> {
                            // 프로그레스바 띄우기
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun requestCommentList(id:Int) {
        commentViewModel.getShortfromComment(id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                commentViewModel.commentResult.collect{
                    when (it) {
                        is NetworkState.Success -> {
                            if (it.data.code == "SUCCESS") {
                                val newList = it.data.data.content

                                showComment(newList)
                            }
                            commentViewModel._commentResult.value = NetworkState.Loading
                        }

                        is NetworkState.Error -> {
                            Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT)
                                .show()
                            commentViewModel._commentResult.value = NetworkState.Loading
                        }
                        is NetworkState.Loading -> {
                            // 프로그레스바 띄우기
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun showComment(newList:List<CommentContent>){
        val adapter = CommentAdapter()
        adapter.setCommentListener(object : OnCommentListener {
            override fun onFavorite(id: Int) {
            }

            override fun onReport(id: Int) {
            }

            override fun writeReply(id: Int) {
            }

        })
        adapter.replaceData(newList)

        val dialog = BottomSheetCommentFragment(this,R.layout.bottom_sheet_comment,adapter,newList)
        dialog.show()
    }

    private fun requestUnFavorite(id:Int) {
        shortsViewModel.postShortformUnLike(id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                shortsViewModel.shortsUnLikeResult.collect{
                    when (it) {
                        is NetworkState.Success -> {
                            if (it.data.code == "SUCCESS") {
                                val item = findShortsItemById(id)
                                item?.is_liked = false
                                item?.likes_count = item?.likes_count!! - 1

                                adapter.getData().add(id,item)
                            }
                        }
                        is NetworkState.Error -> {
                            Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is NetworkState.Loading -> {
                            // 프로그레스바 띄우기
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun requestReport(id:Int) {
        shortsViewModel.reportShortform(id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                shortsViewModel.reportResult.collect{
                    when (it) {
                        is NetworkState.Success -> {
                            if (it.data.code == "SUCCESS") {
                                val nextItem: Int = (currentPosition + 1) % itemSize
                                binding.vpExoplayer.setCurrentItem(nextItem, true)
                                itemSize--
                                adapter.removeItem(currentId)
                                Toast.makeText(applicationContext, "해당 숏폼은 신고 되었습니다", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        is NetworkState.Error -> {
                            Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is NetworkState.Loading -> {
                            // 프로그레스바 띄우기
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun postReviewNoInterest(id:Int) {
        shortsViewModel.postReviewNoInterest(id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                shortsViewModel.noInterestResult.collect{
                    when (it) {
                        is NetworkState.Success -> {
                            if (it.data.code == "SUCCESS") {
                                val nextItem: Int = (currentPosition + 1) % itemSize
                                binding.vpExoplayer.setCurrentItem(nextItem, true)
                                itemSize--
                                adapter.removeItem(currentId)
                                Toast.makeText(applicationContext, "해당 숏폼은 관심없음 처리 되었습니다", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        is NetworkState.Error -> {
                            Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is NetworkState.Loading -> {
                            // 프로그레스바 띄우기
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }
    fun requestShortsLikeOrUnLike(id: Int) {
        val item = findShortsItemById(id)
        if(favoriteFlag == null){
            favoriteFlag = item!!.is_liked
        }
        if (item != null) {
            if (!favoriteFlag!!) {
                requestFavorite(id)
                favoriteFlag = true
            } else {
                requestUnFavorite(id)
                favoriteFlag = false
            }
        }
    }

    private fun requestShortsSaveOrUnSave(id: Int) {
        val item = findShortsItemById(id)

        if(saveFlag == null){
            saveFlag = item!!.is_saved
        }
        if (item != null) {
            if (!saveFlag!!) {
                requestSave(id)
                saveFlag = true
            } else {
                requestUnSave(id)
                saveFlag = false
            }
        }
    }

    private fun requestSave(id:Int) {
        shortsViewModel.postShortformSave(id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                shortsViewModel.shortsSaveResult.collect {
                    when (it) {
                        is NetworkState.Success -> {
                            if (it.data.code == "SUCCESS") {
                                val item = findShortsItemById(id)
                                item?.is_saved = true
                                item?.saved_count = item?.saved_count!! + 1
                                saveFlag = true

                                adapter.getData().add(id, item)
                            }
                        }
                        is NetworkState.Error -> {
                            //showToastMessage("${it}")
                        }
                        is NetworkState.Loading -> {
                            // 프로그레스바 띄우기
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun requestUnSave(id:Int) {
        shortsViewModel.postShortformUnSave(id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                shortsViewModel.shortsUnSaveResult.collect {
                    when (it) {
                        is NetworkState.Success -> {
                            if (it.data.code == "SUCCESS") {
                                val item = findShortsItemById(id)
                                item?.is_saved = false
                                item?.saved_count = item?.saved_count!! - 1
                                saveFlag = false

                                adapter.getData().add(id, item)
                            }
                            //showToastMessage("${it.data}")
                        }
                        is NetworkState.Error -> {
                           // showToastMessage("${it}")
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }
}

