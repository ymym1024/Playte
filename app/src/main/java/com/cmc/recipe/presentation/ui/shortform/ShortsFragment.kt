package com.cmc.recipe.presentation.ui.shortform


import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.cmc.recipe.R
import com.cmc.recipe.data.model.ExoPlayerItem
import com.cmc.recipe.data.model.response.ShortsContent
import com.cmc.recipe.databinding.FragmentShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseFragment
import com.cmc.recipe.presentation.ui.search.SearchActivity
import com.cmc.recipe.presentation.viewmodel.ShortsViewModel
import com.cmc.recipe.utils.Constant
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max


@AndroidEntryPoint
class ShortsFragment : BaseFragment<FragmentShortsBinding>(FragmentShortsBinding::inflate) {

    private val shortsViewModel : ShortsViewModel by viewModels()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    private lateinit var adapter : ShortsAdapter
    private lateinit var itemList : ArrayList<ShortsContent>

    private var favoriteFlag : Boolean? = null
    private var saveFlag : Boolean? = null

    override fun initFragment() {

      //  requestRecipeList()
        searchShorts()
    }


    private fun requestRecipeList(){
        launchWithLifecycle(lifecycle) {
            shortsViewModel.getRecipesShortform()
            shortsViewModel.recipeShortsResult.collect{
                when(it){
                    is NetworkState.Success -> {
                        it.data?.let {data ->
                            if(data.code == "SUCCESS"){
                                itemList = it.data.data.content as ArrayList<ShortsContent>
                                initVideo()
                            }else{
                                Log.d("data","${data.data}")
                            }
                        }
                    }
                    is NetworkState.Error ->{
                        showToastMessage(it.message.toString())
                        shortsViewModel._recipeShortsResult.value = NetworkState.Loading
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

    private fun moveShortsPage(position:Int){
        val intent = Intent(requireContext(), ShortsDetailActivity::class.java)
        intent.putExtra("position",position)
        startActivity(intent)
    }

    private fun initVideo(){
        val clickListener = object : ShortsItemHolder.OnClickListener{
            override fun onMoveDetailPage(id:Int) {
                moveShortsPage(id)
            }
        }

        adapter = ShortsAdapter(requireContext(),clickListener)
        adapter.setShortsListener(object : onShortsListener{
            override fun onFavorite(id:Int) {
                requestShortsLikeOrUnLike(id)
            }

            override fun onSave(id:Int) {
                requestShortsSaveOrUnSave(id)
            }

            override fun onComment(id:Int) {
                moveShortsPage(id)
            }

        })

        val videoPreparedListener = object : ShortsItemHolder.OnVideoPreparedListener{
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }
        }
        adapter.setvideoPreparedListener(videoPreparedListener)
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
            }
        })

    }

    override fun onPause() {
        super.onPause()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.vpExoplayer.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.stop()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()

        requestRecipeList()

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
                player.clearMediaItems()
                player.release()
            }
            exoPlayerItems.clear()
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
            exoPlayerItems.clear()
        }
    }

    private fun findShortsItemById(shortsId: Int): ShortsContent? {
        return itemList.find { it.shortform_id == shortsId }
    }

    private fun requestShortsLikeOrUnLike(id: Int) {
        val item = findShortsItemById(id)

        if(favoriteFlag == null){
            favoriteFlag = item!!.is_liked
        }
        if (item != null) {
            if (!favoriteFlag!!) {
                requestFavorite(id)
                favoriteFlag = true
            } else {
                requestUnFavorite(id)
                favoriteFlag = false
            }
        }
    }

    private fun requestFavorite(id:Int) {
        shortsViewModel.postShortformLike(id)
        launchWithLifecycle(lifecycle){
            shortsViewModel.shortsLikeResult.collect{
                when (it) {
                    is NetworkState.Success -> {
                        if (it.data.code == "SUCCESS") {
                            val item = findShortsItemById(id)
                            item?.is_liked = true
                            item?.likes_count = item?.likes_count!! + 1
                            favoriteFlag = true

                            adapter.getData().add(id,item)
                        }
                    }
                    is NetworkState.Error -> {
                        showToastMessage("${it}")
                    }
                    is NetworkState.Loading -> {
                        // 프로그레스바 띄우기
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun requestUnFavorite(id:Int) {
        shortsViewModel.postShortformUnLike(id)
        launchWithLifecycle(lifecycle){
            shortsViewModel.shortsUnLikeResult.collect {
                when (it) {
                    is NetworkState.Success -> {
                        if (it.data.code == "SUCCESS") {
                            val item = findShortsItemById(id)
                            item?.is_liked = false
                            item?.likes_count = item?.likes_count!! - 1
                            favoriteFlag = false

                            adapter.getData().add(id,item)
                        }
                        showToastMessage("${it.data}")
                    }
                    is NetworkState.Error -> {
                        showToastMessage("${it}")
                    }
                    is NetworkState.Loading -> {
                        // 프로그레스바 띄우기
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun requestShortsSaveOrUnSave(id: Int) {
        val item = findShortsItemById(id)

        if(saveFlag == null){
            saveFlag = item!!.is_saved
        }
        if (item != null) {
            if (!saveFlag!!) {
                requestSave(id)
                saveFlag = true
            } else {
                requestUnSave(id)
                saveFlag = false
            }
        }
    }

    private fun requestSave(id:Int) {
        shortsViewModel.postShortformSave(id)
        launchWithLifecycle(lifecycle){
            shortsViewModel.shortsSaveResult.collect{
                when (it) {
                    is NetworkState.Success -> {
                        if (it.data.code == "SUCCESS") {
                            val item = findShortsItemById(id)
                            item?.is_saved = true
                            item?.saved_count = item?.saved_count!! + 1
                            saveFlag = true

                            adapter.getData().add(id,item)
                        }
                    }
                    is NetworkState.Error -> {
                        showToastMessage("${it}")
                    }
                    is NetworkState.Loading -> {
                        // 프로그레스바 띄우기
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun requestUnSave(id:Int) {
        shortsViewModel.postShortformUnSave(id)
        launchWithLifecycle(lifecycle){
            shortsViewModel.shortsUnSaveResult.collect {
                when (it) {
                    is NetworkState.Success -> {
                        if (it.data.code == "SUCCESS") {
                            val item = findShortsItemById(id)
                            item?.is_saved = false
                            item?.saved_count = item?.saved_count!! - 1
                            saveFlag = false

                            adapter.getData().add(id,item)
                        }
                        showToastMessage("${it.data}")
                    }
                    is NetworkState.Error -> {
                        showToastMessage("${it}")
                    }
                    else -> {

                    }
                }
            }
        }
    }
}