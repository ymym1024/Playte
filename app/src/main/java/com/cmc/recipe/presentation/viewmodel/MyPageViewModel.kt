package com.cmc.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.model.response.ReviewMyResponse
import com.cmc.recipe.data.model.response.ReviewResponse
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

    fun getMyReview() = viewModelScope.launch {
        _myReviewResult.value = NetworkState.Loading
        myPageUseCase.getMyReview()
            .catch { error ->
                _myReviewResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _myReviewResult.value = values
            }

    }

}