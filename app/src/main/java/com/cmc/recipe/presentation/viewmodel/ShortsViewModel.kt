package com.cmc.recipe.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.domain.usecase.AuthUseCase
import com.cmc.recipe.domain.usecase.RecipeUseCase
import com.cmc.recipe.domain.usecase.ShortsUseCase
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortsViewModel @Inject constructor(private val shortsUseCase: ShortsUseCase) : ViewModel() {

    var _recipeShortsResult : MutableStateFlow<NetworkState<ShortsResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeShortsResult: StateFlow<NetworkState<ShortsResponse>> = _recipeShortsResult.asStateFlow()

    var _recipeShortsDetailResult : MutableStateFlow<NetworkState<ShortsDetailResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeShortsDetailResult: StateFlow<NetworkState<ShortsDetailResponse>> = _recipeShortsDetailResult

//    var _shortsLikeResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
//    var shortsLikeResult = _shortsLikeResult.asSharedFlow()
//
//    var _shortsUnLikeResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
//    var shortsUnLikeResult = _shortsUnLikeResult.asSharedFlow()

    var _shortsLikeResult : MutableStateFlow<NetworkState<BaseResponse>> = MutableStateFlow(NetworkState.Loading)
    var shortsLikeResult : StateFlow<NetworkState<BaseResponse>> = _shortsLikeResult.asStateFlow()

    var _shortsUnLikeResult : MutableStateFlow<NetworkState<BaseResponse>> = MutableStateFlow(NetworkState.Loading)
    var shortsUnLikeResult : StateFlow<NetworkState<BaseResponse>> = _shortsUnLikeResult.asStateFlow()

    fun getRecipesShortform() = viewModelScope.launch {
        _recipeShortsResult.value = NetworkState.Loading
        shortsUseCase.getRecipesShortform()
            .catch { error ->
                _recipeShortsResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeShortsResult.value = values
            }
    }

    fun getRecipesShortformDetail(id:Int) = viewModelScope.launch {
        _recipeShortsDetailResult.value = NetworkState.Loading
        shortsUseCase.getRecipesShortformDetail(id)
            .catch { error ->
                _recipeShortsDetailResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeShortsDetailResult.value = values
            }
    }

    fun postShortformLike(id:Int) = viewModelScope.launch {
        _shortsLikeResult.emit(NetworkState.Loading)
        shortsUseCase.postShortformLike(id)
            .catch { error ->
                _shortsLikeResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _shortsLikeResult.emit(values)
            }
    }

    fun postShortformUnLike(id:Int) = viewModelScope.launch {
        _shortsLikeResult.emit(NetworkState.Loading)
        shortsUseCase.postShortformUnLike(id)
            .catch { error ->
                _shortsLikeResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _shortsLikeResult.emit(values)
            }
    }
}