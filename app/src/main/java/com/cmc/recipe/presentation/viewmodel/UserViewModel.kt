package com.cmc.recipe.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.usecase.UserUseCase
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {

    var _verifyResult: MutableStateFlow<NetworkState<BaseResponse>> = MutableStateFlow(NetworkState.Loading)
    var verifyResult: StateFlow<NetworkState<BaseResponse>> = _verifyResult

    var _myInfoResult: MutableStateFlow<NetworkState<MyInfoResponse>> = MutableStateFlow(NetworkState.Loading)
    var myInfoResult: StateFlow<NetworkState<MyInfoResponse>> = _myInfoResult

    var _changeResult: MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var changeResult = _changeResult

    fun verifyNickname(name: String) = viewModelScope.launch {
        val nickname = RequestNickname(name)
        _verifyResult.value = NetworkState.Loading
        userUseCase.verifyNickname(nickname)
            .catch { error ->
                _verifyResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                if (values is NetworkState.Error) {
                    Log.d("err","${values.code} ${values.message}")
                    _verifyResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _verifyResult.emit(values)
                }
            }

    }

    fun getMyInfo() = viewModelScope.launch {
        _myInfoResult.value = NetworkState.Loading
        userUseCase.getMyInfo()
            .catch { error ->
                _myInfoResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _myInfoResult.value = values
            }

    }

    fun changeNickname(request:RequestNickname) = viewModelScope.launch {
        _changeResult.emit(NetworkState.Loading)
        userUseCase.changeNickname(request)
            .catch { error ->
                _changeResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _changeResult.emit(values)
            }

    }
}