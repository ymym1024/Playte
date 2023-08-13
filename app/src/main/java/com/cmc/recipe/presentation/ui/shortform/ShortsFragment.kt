package com.cmc.recipe.presentation.ui.shortform


import android.util.Log
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.cmc.recipe.R
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.databinding.FragmentShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import kotlin.math.abs
import kotlin.math.max

class ShortsFragment : BaseFragment<FragmentShortsBinding>(FragmentShortsBinding::inflate) {
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun initFragment() {
        //TODO : 네트워크 연결 후 모델 변경
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
        val clickListener = object : ShortsItemHolder.OnClickListener{
            override fun onMoveDetailPage(item:String) {
                movePage(R.id.action_shortsFragment_to_shortsDetailActivity)
            }
        }

        val videoPreparedListener = object : ShortsItemHolder.OnVideoPreparedListener{
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }
        }
        val adapter = ShortsAdapter(requireContext(),videoPreparedListener,clickListener)
        adapter.setShortsListener(object : onShortsListener{
            override fun onFavorite() {

            }

            override fun onSave() {

            }

            override fun onComment() {
                //숏츠 상세로 이동
                movePage(R.id.action_shortsFragment_to_shortsDetailActivity)
            }

        })

        adapter.replaceData(itemList)
        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()

        binding.vpExoplayer.adapter = adapter
        binding.vpExoplayer.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vpExoplayer.offscreenPageLimit = 3
        binding.vpExoplayer.setPageTransformer { page, position ->
            val myOffset = position * -(2 * pageOffset + pageMargin)
            if (position < -1) {
                page.translationX = -myOffset
            } else if (position <= 1) {
                val scaleFactor = max(0.7f, 1 - abs(position - 0.14285715f))
                page.translationX = myOffset
                page.scaleY = scaleFactor
                page.alpha = scaleFactor
            } else {
                page.alpha = 0f
                page.translationX = myOffset
            }
        }

        binding.vpExoplayer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("page","${position}")
                Log.d("registerOnPageChangeCallback : ","${exoPlayerItems[position]}")
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

//    override fun onDestroy() {
//        super.onDestroy()
//        if (exoPlayerItems.isNotEmpty()) {
//            for (item in exoPlayerItems) {
//                val player = item.exoPlayer
//                player.release()
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer
                player.release()
            }
        }
    }
}