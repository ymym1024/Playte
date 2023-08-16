package com.cmc.recipe.presentation.ui.shortform

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cmc.recipe.R
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.databinding.ItemShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultDataSource

class ShortsAdapter(private val context:Context,val videoPreparedListener: ShortsItemHolder.OnVideoPreparedListener,val clickListener: ShortsItemHolder.OnClickListener):
    BaseAdapter<String, ItemShortsBinding, ShortsItemHolder>() {

    private lateinit var onShortsListener : onShortsListener

    fun setShortsListener(onShortsListener:onShortsListener){
        this.onShortsListener = onShortsListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsItemHolder {
        return ShortsItemHolder(
            ItemShortsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            context,
            videoPreparedListener,
            clickListener,
            onShortsListener
        )
    }
}

class ShortsItemHolder(viewBinding: ItemShortsBinding, val context: Context,val videoPreparedListener: OnVideoPreparedListener,val clickListener: OnClickListener,val shortsListener: onShortsListener):
    BaseHolder<String, ItemShortsBinding>(viewBinding){

    override fun bind(binding: ItemShortsBinding, item: String?) {
        //오디오 처리
        val exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Toast.makeText(context, "네트워크 연결상태를 확인하세요", Toast.LENGTH_SHORT).show()
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    binding.pbLoading.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    binding.pbLoading.visibility = View.INVISIBLE
                }
            }
        })

        binding.let { v ->
            v.shortParent.setOnClickListener {
                clickListener.onMoveDetailPage("")
            }
            v.videoExoplay.player = exoPlayer
            v.ivEyes.bringToFront()
            v.tvTitle.bringToFront()
            v.tvDesc.bringToFront()
            v.tvBookmarkCnt.bringToFront()
            v.ibBookmark.bringToFront()
            v.tvCommentCnt.bringToFront()
            v.ibComment.bringToFront()
            v.tvHeartCnt.bringToFront()
            v.ibHeart.bringToFront()
        }

        exoPlayer.seekTo(0)
        exoPlayer.volume = 0f
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

        binding.videoExoplay.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

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

        var favoriteFlag = true // TODO : 나중에 서버에서 받아오기
        // 좋아요
        binding.ibHeart.setOnClickListener {
            if(favoriteFlag){
                binding.ibHeart.setImageResource(R.drawable.ic_shorts_heart_deactivate)
                favoriteFlag = false
            }else{
                binding.ibHeart.setImageResource(R.drawable.ic_shorts_heart_activate)
                favoriteFlag = true
            }
            shortsListener.onFavorite() //서버로 통신하는 로직 추가
        }
        // 댓글
        binding.ibComment.setOnClickListener {
            shortsListener.onComment()
        }
        // 북마크
        var bookMarkFlag = true // TODO : 나중에 서버에서 받아오기
        binding.ibBookmark.setOnClickListener {
            if(bookMarkFlag){
                binding.ibBookmark.setImageResource(R.drawable.ic_shorts_bookmark_deactivate)
                bookMarkFlag = false
            }else{
                binding.ibBookmark.setImageResource(R.drawable.ic_shorts_bookmark_activate)
                bookMarkFlag = true
            }
            shortsListener.onSave()
        }
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }

    interface OnClickListener{
        fun onMoveDetailPage(item:String)
    }
}