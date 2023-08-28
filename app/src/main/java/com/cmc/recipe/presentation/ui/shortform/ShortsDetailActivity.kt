package com.cmc.recipe.presentation.ui.shortform

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.cmc.recipe.R
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.data.model.response.ShortsContent
import com.cmc.recipe.data.model.response.ShortsResponse
import com.cmc.recipe.databinding.ActivityShortsDetailBinding
import com.cmc.recipe.presentation.ui.MainActivity
import com.cmc.recipe.presentation.ui.recipe.BottomSheetDetailDialog
import com.cmc.recipe.presentation.viewmodel.ShortsViewModel
import com.cmc.recipe.utils.NetworkState
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShortsDetailActivity : AppCompatActivity() {
    private val shortsViewModel : ShortsViewModel by viewModels()

    private lateinit var binding: ActivityShortsDetailBinding
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()
    private var currentPosition = 0

    private var isMute = false

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
        BottomSheetDetailDialog().show(supportFragmentManager,"RemoveBottomSheetFragment")
    }

    private fun initVideo(itemList:ArrayList<ShortsContent>){
        val adapter = ShortsDetailAdapter(applicationContext,object : ShortsItemHolder.OnVideoPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)

                if (exoPlayerItems.size == 1) {
                    exoPlayerItem.exoPlayer.playWhenReady = true
                    exoPlayerItem.exoPlayer.play()
                }
            }
        })


        adapter.setShortsListener(object : onShortsListener{
            override fun onFavorite(id:Int) {}

            override fun onSave(id:Int) {}

            override fun onComment(id:Int) {}
        })

        binding.vpExoplayer.adapter = adapter
        adapter.replaceData(itemList)

        binding.vpExoplayer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
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

//        binding.vpExoplayer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            private var lastPosition = -1
//
//            override fun onPageSelected(position: Int) {
//                if (lastPosition != position) {
//                    // 현재 페이지와 이전 페이지가 다를 경우에만 동작
//                    if (lastPosition != -1) {
//                        val lastPlayerIndex = exoPlayerItems.indexOfFirst { it.position == lastPosition }
//                        if (lastPlayerIndex != -1) {
//                            val lastPlayer = exoPlayerItems[lastPlayerIndex].exoPlayer
//                            lastPlayer.pause()
//                            lastPlayer.playWhenReady = false
//                            lastPlayer.seekTo(0)
//                        }
//                    }
//
//                    val playerIndex = exoPlayerItems.indexOfFirst { it.position == position }
//                    if (playerIndex != -1) {
//                        val player = exoPlayerItems[playerIndex].exoPlayer
//                        player.playWhenReady = true
//                        player.play()
//                        currentPosition = position
//                    }
//                    lastPosition = position
//                }
//
//            }
//        })

        binding.vpExoplayer.setCurrentItem(currentPosition, false)
    }
}