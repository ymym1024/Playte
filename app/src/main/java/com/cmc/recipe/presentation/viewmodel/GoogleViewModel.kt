package com.cmc.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.GoogleLoginResponse
import com.cmc.recipe.domain.usecase.GoogleLoginUseCase
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleViewModel @Inject constructor(private val usecase: GoogleLoginUseCase) : ViewModel() {

    var _authResult: MutableStateFlow<NetworkState<GoogleLoginResponse>> = MutableStateFlow(NetworkState.Loading)
    var authResult: StateFlow<NetworkState<GoogleLoginResponse>> = _authResult

    fun fetchAuthInfo(authCode: String) = viewModelScope.launch {
        _authResult.value = NetworkState.Loading
        usecase.fetchAuthInfo(authCode)
            .catch { error ->
                _authResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _authResult.value = values
            }

    }
}