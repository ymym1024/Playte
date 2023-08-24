package com.cmc.recipe.presentation.ui.shortform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.cmc.recipe.R
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.data.model.response.Product
import com.cmc.recipe.data.model.response.ShortsContent
import com.cmc.recipe.databinding.ActivityShortsDetailBinding
import com.cmc.recipe.databinding.ItemShortsDetailBinding
import com.cmc.recipe.presentation.ui.recipe.BottomSheetDetailDialog
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.NetworkState
import com.cmc.recipe.utils.navigationHeight
import com.cmc.recipe.utils.setStatusBarTransparent
import com.cmc.recipe.utils.statusBarHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShortsDetailActivity : AppCompatActivity() {
    private val recipeViewModel : RecipeViewModel by viewModels()

    private lateinit var binding: ActivityShortsDetailBinding
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()
    private var currentPosition = 0

    private var isMute = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShortsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //position 전달 받음
        val shortsPosition = ShortsDetailActivityArgs.fromBundle(intent.extras!!).id
        currentPosition = shortsPosition

        requestRecipeList()
        initMenu()
    }

    private fun requestRecipeList(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.getRecipesShortform()
                recipeViewModel.recipeShortsResult.collect { response ->
                    when (response) {
                        is NetworkState.Success -> {
                            response.data?.let { data ->
                                if (data.code == "SUCCESS") {
                                    val itemList = response.data.data.content
                                    Log.d("여기 호출되는지 확인",itemList[0].video_url)
                                    initVideo(itemList as ArrayList<ShortsContent>)
                                } else {
                                    Log.d("data", "${data.data}")
                                }
                            }
                            recipeViewModel._recipeResult.value = NetworkState.Loading
                        }
                        is NetworkState.Error -> {
                            recipeViewModel._recipeResult.value = NetworkState.Loading
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun requestRecipeDetail(id:Int){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.getRecipesShortformDetail(id)
                recipeViewModel.recipeShortsDetailResult.collect {
                    when (it) {
                        is NetworkState.Success -> {
                            it.data?.let { data ->
                                if (data.code == "SUCCESS") {
                                    val productList = it.data.data.ingredients
                                    //initProductAdapter(productList)
                                } else {
                                    Log.d("data", "${data.data}")
                                }
                            }
                            recipeViewModel._recipeResult.value = NetworkState.Loading
                        }
                        is NetworkState.Error -> {
                            recipeViewModel._recipeResult.value = NetworkState.Loading
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
            }
        })

        adapter.setShortsListener(object : onShortsListener{
            override fun onFavorite() {}

            override fun onSave() {}

            override fun onComment(id:Int) {}
            override fun requestDetail(id: Int) {
                requestRecipeDetail(id)
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
                currentPosition = position

              //  requestRecipeDetail(itemList[currentPosition].shortform_id)
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