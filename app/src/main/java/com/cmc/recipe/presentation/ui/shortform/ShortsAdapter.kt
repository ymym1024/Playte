package com.cmc.recipe.presentation.ui.shortform

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.databinding.ItemShortsBinding
import com.cmc.recipe.databinding.ItemShortsDetailBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class ShortsAdapter(private val context:Context,val videoPreparedListener: ShortsItemHolder.OnVideoPreparedListener,val clickListener: ShortsItemHolder.OnClickListener):
    BaseAdapter<String, ItemShortsBinding, ShortsItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsItemHolder {
        return ShortsItemHolder(
            ItemShortsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            context,
            videoPreparedListener,
            clickListener
        )
    }
}

class ShortsItemHolder(viewBinding: ItemShortsBinding, val context: Context,val videoPreparedListener: ShortsItemHolder.OnVideoPreparedListener,val clickListener: OnClickListener):
    BaseHolder<String, ItemShortsBinding>(viewBinding){

    override fun bind(binding: ItemShortsBinding, item: String?) {
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
                clickListener.onMoveDetailPage(0)
            }
            v.videoExoplay.player = exoPlayer
            v.ivEyes.bringToFront()
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
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }

    interface OnClickListener{
        fun onMoveDetailPage(id:Int)
    }
}