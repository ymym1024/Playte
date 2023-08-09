package com.cmc.recipe.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.LoginResponse
import com.cmc.recipe.data.model.response.SignupResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.usecase.AuthUseCase
import com.cmc.recipe.domain.usecase.UserUseCase
import com.cmc.recipe.utils.NetworkState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val AuthUseCase: AuthUseCase) : ViewModel() {

    var _signupResult: MutableStateFlow<NetworkState<SignupResponse>> = MutableStateFlow(NetworkState.Loading)
    var signupResult: StateFlow<NetworkState<SignupResponse>> = _signupResult

    var _loginResult: MutableStateFlow<NetworkState<LoginResponse>> = MutableStateFlow(NetworkState.Loading)
    var loginResult: StateFlow<NetworkState<LoginResponse>> = _loginResult

    var _logoutResult: MutableStateFlow<NetworkState<BaseResponse>> = MutableStateFlow(NetworkState.Loading)
    var logoutResult: StateFlow<NetworkState<BaseResponse>> = _logoutResult

    var _refreshResult: MutableStateFlow<NetworkState<SignupResponse>> = MutableStateFlow(NetworkState.Loading)
    var refreshResult: StateFlow<NetworkState<SignupResponse>> = _refreshResult

    fun signup(accessToken:String,name: String) = viewModelScope.launch {
        val nickname = RequestNickname(name)
        _signupResult.value = NetworkState.Loading
        AuthUseCase.signup(accessToken,nickname)
            .catch { error ->
                _signupResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _signupResult.value = values
            }

    }

    fun login(accessToken:String) = viewModelScope.launch {
        _loginResult.value = NetworkState.Loading
        AuthUseCase.login(accessToken)
            .catch { error ->
                _loginResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _loginResult.value = values
            }

    }

    fun logout(accessToken:String,refreshToken:String) = viewModelScope.launch {
        _logoutResult.value = NetworkState.Loading
        AuthUseCase.logout(accessToken,refreshToken)
            .catch { error ->
                _logoutResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _logoutResult.value = values
            }

    }

    fun refreshToken(refreshToken:String) = viewModelScope.launch {
        _refreshResult.value = NetworkState.Loading
        AuthUseCase.refreshToken(refreshToken)
            .catch { error ->
                _refreshResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _refreshResult.value = values
            }

    }
}