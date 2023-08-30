package com.cmc.recipe.presentation.ui.shortform

import BottomSheetCommentFragment
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.recipe.R
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.data.model.Product
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.databinding.ItemShortsDetailBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.common.CommentAdapter
import com.cmc.recipe.presentation.ui.common.OnCommentListener
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ShortsDetailAdapter(private val context:Context,val videoPreparedListener: ShortsItemHolder.OnVideoPreparedListener):
    BaseAdapter<ShortsContent, ItemShortsDetailBinding, ShortsDetailHolder>() {

    private lateinit var onShortsListener : onShortsListener


    fun setShortsListener(onShortsListener:onShortsListener){
        this.onShortsListener = onShortsListener
    }

    fun removeItem(id:Int){
        val itemList = getData()
        val itemToRemove = itemList.find { it.shortform_id == id }
        itemList.remove(itemToRemove)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsDetailHolder {
        return ShortsDetailHolder(
            ItemShortsDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            context,
            videoPreparedListener,
            onShortsListener,
        )
    }
}

class ShortsDetailHolder(
    viewBinding: ItemShortsDetailBinding,
    val context: Context,
    val videoPreparedListener: ShortsItemHolder.OnVideoPreparedListener,
    val shortsListener: onShortsListener,
)
    :BaseHolder<ShortsContent, ItemShortsDetailBinding>(viewBinding){
  //  private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun bind(binding: ItemShortsDetailBinding, item: ShortsContent?) {
     //   bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetComment)

        binding.tvNick.text = item?.writtenBy
        binding.tvShortContent.text = item?.shortform_description
        binding.tvHeartCnt.text = item?.likes_count.toString()
        binding.tvCommentCnt.text = item?.comments_count.toString()
        binding.tvBookmarkCnt.text = item?.saved_count.toString()

        // product recyclerview
        initIngredientAdapter(binding,item!!.ingredients)

        // product recyclerview
        initProductAdapter(binding,item!!.ingredients)

        //댓글 설정
        initCommentAdapter(binding,item!!.shortform_id)

        //exoplayer 설정
        initShorts(binding)

        // 댓글
        // 좋아요
        var favoriteFlag = item.is_liked
        var favoriteCount = item.likes_count
        if(item.is_liked){
            binding.btnHeart.setImageResource(R.drawable.ic_shorts_heart_activate)
        }else{
            binding.btnHeart.setImageResource(R.drawable.ic_shorts_heart_deactivate)
        }
        binding.btnHeart.scaleType = ImageView.ScaleType.CENTER_CROP

        binding.btnHeart.let { btn->
            btn.setOnClickListener{
                shortsListener.onFavorite(item.shortform_id)
                if(favoriteFlag == false){
                    binding.btnHeart.setImageResource(R.drawable.ic_shorts_heart_activate)
                    if(favoriteCount == 0){
                        binding.tvHeartCnt.text = "${favoriteCount+1}"
                    }else{
                        binding.tvHeartCnt.text = "${favoriteCount}"
                    }
                    favoriteFlag = !favoriteFlag
                }else{
                    binding.btnHeart.setImageResource(R.drawable.ic_shorts_heart_deactivate)
                    if(favoriteCount == 0){
                        binding.tvHeartCnt.text = "${favoriteCount}"
                    }else{
                        binding.tvHeartCnt.text = "${favoriteCount-1}"
                    }
                    favoriteFlag = !favoriteFlag
                }
            }
        }

        // 북마크
        var saveFlag = item.is_liked
        var saveCount = item.saved_count
        if(!item.is_liked){
            binding.btnBookmark.setImageResource(R.drawable.ic_shorts_bookmark_deactivate)
        }else{
            binding.btnBookmark.setImageResource(R.drawable.ic_shorts_bookmark_activate)
        }
        binding.btnBookmark.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.btnBookmark.let { btn ->
            btn.setOnClickListener {
                shortsListener.onSave(item.shortform_id)
                if(saveFlag == false){  // false
                    if(saveCount == 0){
                        binding.tvBookmarkCnt.text = "${saveCount+1}"
                    }else{
                        binding.tvBookmarkCnt.text = "${saveCount}"
                    }
                    btn.setImageResource(R.drawable.ic_shorts_bookmark_activate)
                    saveFlag = !saveFlag
                }else{  // true

                    btn.setImageResource(R.drawable.ic_shorts_bookmark_deactivate)
                    if(saveCount == 0){
                        binding.tvBookmarkCnt.text = "${saveCount}"
                    }else{
                        binding.tvBookmarkCnt.text = "${saveCount-1}"
                    }
                    saveFlag = !saveFlag
                }
            }
        }
        // 댓글

        binding.btnComment.setOnClickListener {
             shortsListener.onComment(item.shortform_id)
        }

        binding.btnBookmark.scaleType = ImageView.ScaleType.CENTER_CROP
        // 텍스트 접기
        val originalMaxLines = binding.tvShortContent.maxLines
        val expandedMaxLines = Int.MAX_VALUE

        Log.d("maxLine","${binding.tvShortContent.maxLines}")
        binding.tvShowMore.setOnClickListener {
            if(binding.tvShortContent.maxLines > 1){
                if (binding.tvShortContent.maxLines == originalMaxLines) {
                    binding.tvShortContent.maxLines = expandedMaxLines
                    binding.tvShowMore.text = ""
                } else {
                    binding.tvShortContent.maxLines = originalMaxLines
                    binding.tvShowMore.text = "더보기"
                }
            }


        }
    }

    private fun initProductAdapter(binding:ItemShortsDetailBinding,itemList:List<Ingredient>){
        val itemList = itemList.map{
            Product(it.coupang_product_image,it.coupang_product_name,it.coupang_product_price,it.coupang_product_url)
        }

        val adapter = ShortsProductAdapter()
        binding.rvProduct.adapter = adapter
        binding.rvProduct.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.replaceData(itemList!!)
    }

    private fun initIngredientAdapter(binding:ItemShortsDetailBinding,itemList:List<Ingredient>){
        val itemList = itemList.map{
            it.ingredient_name
        }

        val adapter = ShortsIngredientAdapter()
        binding.linearLayout.adapter = adapter
        binding.linearLayout.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.replaceData(itemList)
    }

    private fun initCommentAdapter(binding:ItemShortsDetailBinding,shortformId: Int){
//        commentAdapter.setCommentListener(object : OnCommentListener{
//            override fun onFavorite(id: Int) {
//
//            }
//
//            override fun onReport(id: Int) {
//
//            }
//
//            override fun writeReply(id: Int) {
//
//            }
//
//        })
//        binding.rvComment.adapter = commentAdapter
//        binding.rvComment.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initShorts(binding:ItemShortsDetailBinding){
        var exoPlayer = ExoPlayer.Builder(context).build()
        val listener = object : Player.Listener {
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
        }
        exoPlayer.addListener(listener)

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
            MediaItem.fromUri(Uri.parse(item?.video_url)))

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()

        videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoPlayer, absoluteAdapterPosition))
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