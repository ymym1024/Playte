package com.cmc.recipe.presentation.ui.shortform

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource

class ShortsDetailAdapter(private val context:Context,val videoPreparedListener: ShortsItemHolder.OnVideoPreparedListener):
    BaseAdapter<String, ItemShortsDetailBinding, ShortsDetailHolder>() {

    private lateinit var onShortsListener : onShortsListener

    fun setShortsListener(onShortsListener:onShortsListener){
        this.onShortsListener = onShortsListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsDetailHolder {
        return ShortsDetailHolder(
            ItemShortsDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            context,
            videoPreparedListener,
            onShortsListener
        )
    }
}

class ShortsDetailHolder(viewBinding: ItemShortsDetailBinding, val context: Context,val videoPreparedListener: ShortsItemHolder.OnVideoPreparedListener,val shortsListener: onShortsListener)
    :BaseHolder<String, ItemShortsDetailBinding>(viewBinding){
    override fun bind(binding: ItemShortsDetailBinding, item: String?) {

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
        binding.exoplayer.controllerShowTimeoutMs = 0

        var isPlaying = true

        binding.ivPlayPause.bringToFront()
        binding.exoplayer.setOnClickListener {
            binding.exoplayer.showController()
            if (isPlaying) {
                exoPlayer.playWhenReady = false // Pause the player
                isPlaying = false

            } else {
                exoPlayer.playWhenReady = true // Resume playing
                isPlaying = true
            }

            showPlayPause(binding.ivPlayPause,isPlaying)
        }

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

        // 댓글
        var favoriteFlag = true // TODO : 나중에 서버에서 받아오기
        // 좋아요
        binding.btnHeart.let { btn->
            btn.setOnClickListener{
                if(favoriteFlag){
                    btn.setImageResource(R.drawable.ic_shorts_heart_deactivate)
                    favoriteFlag = false
                }else{
                    btn.setImageResource(R.drawable.ic_shorts_heart_activate)
                    favoriteFlag = true
                }
                shortsListener.onFavorite()
            }
        }
        // 댓글
        binding.btnComment.setOnClickListener {
            shortsListener.onComment()
        }
        // 북마크
        var bookMarkFlag = true // TODO : 나중에 서버에서 받아오기
        binding.btnBookmark.let { btn ->
            btn.setOnClickListener {
                if(bookMarkFlag){
                    btn.setImageResource(R.drawable.ic_shorts_bookmark_deactivate)
                    bookMarkFlag = false
                }else{
                    btn.setImageResource(R.drawable.ic_shorts_bookmark_activate)
                    bookMarkFlag = true
                }
                shortsListener.onSave()
            }
        }
    }

    private fun showPlayPause(imageView:ImageView,flag:Boolean){
        if(flag) imageView.setImageResource(R.drawable.ic_shorts_play)
        else imageView.setImageResource(R.drawable.ic_shorts_pause)

        imageView.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            imageView.visibility = View.GONE
        }, 1000)
    }
}