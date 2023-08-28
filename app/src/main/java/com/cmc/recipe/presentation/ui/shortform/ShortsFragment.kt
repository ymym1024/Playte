package com.cmc.recipe.presentation.ui.shortform


import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.cmc.recipe.R
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.data.model.response.ShortsContent
import com.cmc.recipe.databinding.FragmentShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.search.SearchActivity
import com.cmc.recipe.presentation.viewmodel.RecipeViewModel
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs
import kotlin.math.max

@AndroidEntryPoint
class ShortsFragment : BaseFragment<FragmentShortsBinding>(FragmentShortsBinding::inflate) {

    private val recipeViewModel : RecipeViewModel by viewModels()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun initFragment() {

        requestRecipeList()
        searchShorts()
    }

    private fun requestRecipeList(){
        launchWithLifecycle(lifecycle) {
            recipeViewModel.getRecipesShortform()
            recipeViewModel.recipeShortsResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                val itemList = it.data.data.content
                                initVideo(itemList as ArrayList<ShortsContent>)
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                        recipeViewModel._recipeResult.value = NetworkState.Loading
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        recipeViewModel._recipeResult.value = NetworkState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    private fun searchShorts(){
        binding.searchView.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                val searchText = binding.searchView.text.toString()

                if(searchText.isEmpty()){
                    movePage(Constant.SHORTS,Constant.SEARCH,null)
                }else{
                    movePage(Constant.SHORTS,Constant.SHORTS,searchText)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun movePage(current:String,destination:String,keyword:String?){
        binding.searchView.setText("")
        val intent = Intent(requireContext(), SearchActivity::class.java)
        intent.putExtra("startDestination", destination)
        intent.putExtra("currentDestination", current)
        intent.putExtra("keyword", keyword)
        startActivity(intent)
    }

    private fun initVideo(itemList:ArrayList<ShortsContent>){
        val clickListener = object : ShortsItemHolder.OnClickListener{
            override fun onMoveDetailPage(id:Int) {
                val action = ShortsFragmentDirections.actionShortsFragmentToShortsDetailActivity(id)
                findNavController().navigate(action)
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

            override fun onComment(id:Int) {
                val action = ShortsFragmentDirections.actionShortsFragmentToShortsDetailActivity(id)
                findNavController().navigate(action)
            }

            override fun requestDetail(id: Int) {

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