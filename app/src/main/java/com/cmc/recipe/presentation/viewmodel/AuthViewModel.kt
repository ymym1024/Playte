package com.cmc.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.LoginResponse
import com.cmc.recipe.data.model.response.SignupResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.usecase.AuthUseCase
import com.cmc.recipe.domain.usecase.UserUseCase
import com.cmc.recipe.utils.NetworkState
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
}