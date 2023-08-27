package com.cmc.recipe.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.usecase.MyPageUseCase
import com.cmc.recipe.domain.usecase.UserUseCase
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(private val myPageUseCase: MyPageUseCase) : ViewModel() {

    var _myReviewResult: MutableStateFlow<NetworkState<ReviewMyResponse>> = MutableStateFlow(NetworkState.Loading)
    var myReviewResult: StateFlow<NetworkState<ReviewMyResponse>> = _myReviewResult

    var _saveRecipeResult: MutableStateFlow<NetworkState<SaveWriteRecipeResponse>> = MutableStateFlow(NetworkState.Loading)
    var saveRecipeResult: StateFlow<NetworkState<SaveWriteRecipeResponse>> = _saveRecipeResult

    var _writtenRecipeResult: MutableStateFlow<NetworkState<SaveWriteRecipeResponse>> = MutableStateFlow(NetworkState.Loading)
    var writtenRecipeResult: StateFlow<NetworkState<SaveWriteRecipeResponse>> = _writtenRecipeResult

    var _reviewDeleteResult: MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var reviewDeleteResult: SharedFlow<NetworkState<BaseResponse>> = _reviewDeleteResult.asSharedFlow()

    var _recipeDeleteResult: MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var recipeDeleteResult: SharedFlow<NetworkState<BaseResponse>> = _recipeDeleteResult.asSharedFlow()

    var _noticeResult: MutableStateFlow<NetworkState<NoticeResponse>> = MutableStateFlow(NetworkState.Loading)
    var noticeResult: StateFlow<NetworkState<NoticeResponse>> = _noticeResult



    fun getMyReview() = viewModelScope.launch {
        _myReviewResult.value = NetworkState.Loading
        myPageUseCase.getMyReview()
            .catch { error ->
                _myReviewResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _myReviewResult.value = values
            }

    }

    fun getSaveRecipe() = viewModelScope.launch {
        _saveRecipeResult.value = NetworkState.Loading
        myPageUseCase.getSaveRecipe()
            .catch { error ->
                _saveRecipeResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _saveRecipeResult.value = values
            }

    }

    fun getNotice() = viewModelScope.launch {
        _noticeResult.value = NetworkState.Loading
        myPageUseCase.getNotice()
            .catch { error ->
                _noticeResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _noticeResult.value = values
            }

    }

    fun getWrittenRecipe() = viewModelScope.launch {
        _writtenRecipeResult.value = NetworkState.Loading
        myPageUseCase.getWrittenRecipe()
            .catch { error ->
                _writtenRecipeResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _writtenRecipeResult.value = values
            }

    }

    fun deleteReview(id:Int) = viewModelScope.launch {
        _reviewDeleteResult.emit(NetworkState.Loading)
        myPageUseCase.deleteReview(id)
            .catch { error ->
                _reviewDeleteResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _reviewDeleteResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _reviewDeleteResult.emit(values)
                }
            }

    }

    fun deleteRecipe(id:Int) = viewModelScope.launch {
        _reviewDeleteResult.emit(NetworkState.Loading)
        myPageUseCase.deleteRecipe(id)
            .catch { error ->
                _reviewDeleteResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _reviewDeleteResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _reviewDeleteResult.emit(values)
                }
            }

    }

}