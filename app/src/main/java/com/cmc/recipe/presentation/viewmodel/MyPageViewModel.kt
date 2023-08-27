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

    var _saveRecipeResult: MutableStateFlow<NetworkState<RecipesResponse>> = MutableStateFlow(NetworkState.Loading)
    var saveRecipeResult: StateFlow<NetworkState<RecipesResponse>> = _saveRecipeResult

    var _reviewDeleteResult: MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var reviewDeleteResult: SharedFlow<NetworkState<BaseResponse>> = _reviewDeleteResult.asSharedFlow()

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

}