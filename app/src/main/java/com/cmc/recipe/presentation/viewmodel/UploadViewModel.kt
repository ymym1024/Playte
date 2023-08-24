package com.cmc.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.usecase.UploadUseCase
import com.cmc.recipe.domain.usecase.UserUseCase
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val uploadUseCase: UploadUseCase) : ViewModel() {

    var _uploadImageResult: MutableStateFlow<NetworkState<BaseResponse>> = MutableStateFlow(NetworkState.Loading)
    var uploadImageResult: StateFlow<NetworkState<BaseResponse>> = _uploadImageResult

    fun uploadImage(file: MultipartBody.Part) = viewModelScope.launch {
        _uploadImageResult.value = NetworkState.Loading
        uploadUseCase.uploadImage(file)
            .catch { error ->
                _uploadImageResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _uploadImageResult.value = values
            }
    }

}