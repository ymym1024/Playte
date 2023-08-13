package com.cmc.recipe.presentation.ui.shortform

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.data.model.Product
import com.cmc.recipe.databinding.ItemShortsDetailBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.upstream.DefaultDataSource

class ShortsDetailAdapter(private val context:Context,val videoPreparedListener: ShortsItemHolder.OnVideoPreparedListener):
    BaseAdapter<String, ItemShortsDetailBinding, ShortsDetailHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsDetailHolder {
        return ShortsDetailHolder(
            ItemShortsDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            context,
            videoPreparedListener
        )
    }
}

class ShortsDetailHolder(viewBinding: ItemShortsDetailBinding, val context: Context,val videoPreparedListener: ShortsItemHolder.OnVideoPreparedListener)
    :BaseHolder<String, ItemShortsDetailBinding>(viewBinding){
    override fun bind(binding: ItemShortsDetailBinding, item: String?) {

        val videoView = binding.exoplayer
        val defaultTimeBarView = videoView.findViewById<DefaultTimeBar>(com.google.android.exoplayer2.ui.R.id.exo_progress)
        val timeTextView = videoView.findViewById<LinearLayout>(R.id.tv_video_time)

        defaultTimeBarView.setPlayedColor(ContextCompat.getColor(binding.root.context, R.color.primary_color))
        defaultTimeBarView.setScrubberColor(ContextCompat.getColor(binding.root.context, R.color.primary_color))
        defaultTimeBarView.setAdMarkerColor(ContextCompat.getColor(binding.root.context, R.color.primary_color))

        val parent = defaultTimeBarView.parent as? ViewGroup
        parent?.removeView(defaultTimeBarView)
        parent?.removeView(timeTextView)

        binding.llPlayer.addView(defaultTimeBarView)
        binding.llPlayer.addView(timeTextView)

        val exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Toast.makeText(context, "네트워크 연결상태를 확인하세요", Toast.LENGTH_SHORT).show()
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    binding.pbLoad.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    binding.pbLoad.visibility = View.INVISIBLE
                }
            }
        })

        binding.exoplayer.player = exoPlayer

        exoPlayer.seekTo(0)
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

        val dataSourceFactory = DefaultDataSource.Factory(context)

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
            MediaItem.fromUri(Uri.parse(item)))

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()

        if (absoluteAdapterPosition == 0) {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        }

        videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoPlayer, absoluteAdapterPosition))


        // product recyclerview
        val adapter = ShortsProductAdapter(object :ShortsProductItemHolder.OnClickListener{
            override fun onMoveSite(url: String) {
               // 화면이동
            }
        })
        binding.rvProduct.adapter = adapter
        binding.rvProduct.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //mock data
        val itemList = arrayListOf(
            Product(image = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",name="전남 국내산 대추방...", price = 18000),
            Product(image = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",name="전남 국내산 대추방...", price = 18000),
            Product(image = "https://recipe1.ezmember.co.kr/cache/recipe/2022/02/02/dbb3f34bfe348a4bb4d142ff353815651.jpg",name="전남 국내산 대추방...", price = 18000),
        )

        adapter.replaceData(itemList)
    }

}