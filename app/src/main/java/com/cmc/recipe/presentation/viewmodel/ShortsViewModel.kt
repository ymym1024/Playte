package com.cmc.recipe.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.entity.ShortsEntity
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

    var _shortsSaveResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var shortsSaveResult = _shortsSaveResult.asSharedFlow()

    var _shortsUnSaveResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var shortsUnSaveResult = _shortsUnSaveResult.asSharedFlow()

    var _reportResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var reportResult = _reportResult.asSharedFlow()

    var _noInterestResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var noInterestResult = _noInterestResult.asSharedFlow()

    private val _recentShortsResult = MutableSharedFlow<List<ShortsEntity>>()
    val recentShortsResult: Flow<List<ShortsEntity>>
        get() = _recentShortsResult

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
        _shortsUnLikeResult.emit(NetworkState.Loading)
        shortsUseCase.postShortformUnLike(id)
            .catch { error ->
                _shortsUnLikeResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _shortsUnLikeResult.emit(values)
            }
    }

    fun postShortformSave(id:Int) = viewModelScope.launch {
        _shortsSaveResult.emit(NetworkState.Loading)
        shortsUseCase.postShortformSave(id)
            .catch { error ->
                _shortsSaveResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _shortsSaveResult.emit(values)
            }
    }

    fun postShortformUnSave(id:Int) = viewModelScope.launch {
        _shortsUnSaveResult.emit(NetworkState.Loading)
        shortsUseCase.postShortformUnSave(id)
            .catch { error ->
                _shortsUnSaveResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _shortsUnSaveResult.emit(values)
            }
    }

    fun reportShortform(id:Int) = viewModelScope.launch {
        _reportResult.emit(NetworkState.Loading)
        shortsUseCase.reportShortform(id)
            .catch { error ->
                _reportResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _reportResult.emit(values)
            }
    }

    fun postReviewNoInterest(id:Int) = viewModelScope.launch {
        _noInterestResult.emit(NetworkState.Loading)
        shortsUseCase.postReviewNoInterest(id)
            .catch { error ->
                _noInterestResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _noInterestResult.emit(values)
            }
    }

    //최근 본 레시피
    fun insertRecentShorts(item:ShortsContent){
        viewModelScope.launch (Dispatchers.IO){
            shortsUseCase.insertRecentShorts(item)
        }
    }

    fun loadRecentRecipes() = viewModelScope.launch {
        shortsUseCase.loadRecentShorts()
            .collect{ values ->
                _recentShortsResult.emit(values)
            }
    }
}