package com.cmc.recipe.presentation.ui.shortform

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.cmc.recipe.R
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.data.model.response.ShortsContent
import com.cmc.recipe.databinding.ItemShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.dpToPx
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultDataSource

class ShortsAdapter(private val context:Context,val clickListener: ShortsItemHolder.OnClickListener):
    BaseAdapter<ShortsContent, ItemShortsBinding, ShortsItemHolder>() {

    private lateinit var onShortsListener : onShortsListener
    private lateinit var videoPreparedListener: ShortsItemHolder.OnVideoPreparedListener

    fun setShortsListener(onShortsListener:onShortsListener){
        this.onShortsListener = onShortsListener
    }

    fun setvideoPreparedListener(videoPreparedListener:ShortsItemHolder.OnVideoPreparedListener){
        this.videoPreparedListener = videoPreparedListener
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
    BaseHolder<ShortsContent, ItemShortsBinding>(viewBinding){

    private var exoPlayer: ExoPlayer? = null

    override fun bind(binding: ItemShortsBinding, item: ShortsContent?) {
        //오디오 처리
        exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer!!.addListener(object : Player.Listener {
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

        exoPlayer!!.seekTo(0)
        exoPlayer!!.volume = 0f
        exoPlayer!!.repeatMode = Player.REPEAT_MODE_ONE

        binding.videoExoplay.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        exoPlayer!!.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING

        val dataSourceFactory = DefaultDataSource.Factory(context)

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
            MediaItem.fromUri(Uri.parse(item!!.video_url)))

        exoPlayer!!.setMediaSource(mediaSource)
        exoPlayer!!.prepare()

        videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoPlayer!!, absoluteAdapterPosition))

        // 데이터 바인딩
        binding.let { v ->
            v.shortParent.setOnClickListener {
                clickListener.onMoveDetailPage(position)
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

        binding.tvTitle.text = item.writtenBy
        binding.tvDesc.text = item.shortform_name
        binding.tvHeartCnt.text = "${item.likes_count}"
        binding.tvCommentCnt.text = "${item.comments_count}"
        binding.tvBookmarkCnt.text = "${item.saved_count}"

        if(!item.is_liked) binding.ibHeart.setImageResource(R.drawable.ic_shorts_heart_deactivate)
        else binding.ibHeart.setImageResource(R.drawable.ic_shorts_heart_activate)
        binding.ibHeart.scaleType = ImageView.ScaleType.CENTER_CROP

        if(!item.is_saved) binding.ibBookmark.setImageResource(R.drawable.ic_shorts_bookmark_deactivate)
        else binding.ibBookmark.setImageResource(R.drawable.ic_shorts_bookmark_activate)
        binding.ibBookmark.scaleType = ImageView.ScaleType.CENTER_CROP

        var favoriteFlag = item.is_liked
        // 좋아요
        binding.ibHeart.setOnClickListener {
            shortsListener.onFavorite(item.shortform_id) //서버로 통신하는 로직 추가
            if(!favoriteFlag){
                binding.ibHeart.setImageResource(R.drawable.ic_shorts_heart_deactivate)
                favoriteFlag = !favoriteFlag
            }else{
                binding.ibHeart.setImageResource(R.drawable.ic_shorts_heart_activate)
                favoriteFlag = !favoriteFlag
            }
        }
        // 댓글
        binding.ibComment.setOnClickListener {
            shortsListener.onComment(position)
        }
        // 북마크
        binding.ibBookmark.setOnClickListener {
            shortsListener.onSave(item.shortform_id)
        }
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }

    interface OnClickListener{
        fun onMoveDetailPage(id:Int)
    }
}