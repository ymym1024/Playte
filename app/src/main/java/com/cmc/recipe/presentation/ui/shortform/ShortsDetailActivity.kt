package com.cmc.recipe.presentation.ui.shortform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.databinding.ActivityShortsDetailBinding

class ShortsDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShortsDetailBinding
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShortsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemList = arrayListOf(
            "https://recipe-application-bucket.s3.ap-northeast-2.amazonaws.com/videos/testvideo.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            "https://d1jg55wkcrciwu.cloudfront.net/videos/testvideo.mp4",
            "https://www.youtube.com/shorts/ku5PCueK_CY?feature=share"
        )

        initVideo(itemList)

    }

    private fun initVideo(itemList:ArrayList<String>){
        val adapter = ShortsDetailAdapter(applicationContext,object : ShortsItemHolder.OnVideoPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }
        })

        adapter.replaceData(itemList)
        binding.vpExoplayer.adapter = adapter

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
                }
                Log.d("onPageSelected","${previousIndex} , ${newIndex}")
            }
        })

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
        }
    }
}