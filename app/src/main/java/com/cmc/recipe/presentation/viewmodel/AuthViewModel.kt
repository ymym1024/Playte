package com.cmc.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.source.remote.request.RequestVerifyNickname
import com.cmc.recipe.domain.usecase.AuthUseCase
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val AuthUseCase: AuthUseCase) : ViewModel() {

    var _verifyResult: MutableStateFlow<NetworkState<BaseResponse>> = MutableStateFlow(NetworkState.Loading)
    var verifyResult: StateFlow<NetworkState<BaseResponse>> = _verifyResult

    fun verifyNickname(name: String) = viewModelScope.launch {
        val nickname = RequestVerifyNickname(name)
        _verifyResult.value = NetworkState.Loading
        AuthUseCase.verifyNickname(nickname)
            .catch { error ->
                _verifyResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _verifyResult.value = values
            }

    }
}