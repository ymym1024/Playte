package com.cmc.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.IngredientsResponse
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

    var _uploadRecipeResult: MutableStateFlow<NetworkState<BaseResponse>> = MutableStateFlow(NetworkState.Loading)
    var uploadRecipeResult: StateFlow<NetworkState<BaseResponse>> = _uploadRecipeResult

    var _ingredientsResult: MutableStateFlow<NetworkState<IngredientsResponse>> = MutableStateFlow(NetworkState.Loading)
    var ingredientsResult: StateFlow<NetworkState<IngredientsResponse>> = _ingredientsResult

    fun uploadRequest(file: MultipartBody.Part) = viewModelScope.launch {
        _uploadImageResult.value = NetworkState.Loading
        uploadUseCase.uploadImage(file)
            .catch { error ->
                _uploadImageResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _uploadImageResult.value = values
            }
    }

    fun getIngredients() = viewModelScope.launch {
        _ingredientsResult.value = NetworkState.Loading
        uploadUseCase.getIngredients()
            .catch { error ->
                _ingredientsResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _ingredientsResult.value = values
            }
    }

}