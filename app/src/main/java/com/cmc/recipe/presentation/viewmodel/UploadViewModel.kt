package com.cmc.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.IngredientsResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.data.source.remote.request.UploadRecipeRequest
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

    var _uploadImageResult: MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow(replay = 0)
    var uploadImageResult = _uploadImageResult.asSharedFlow()

    var _uploadRecipeResult: MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow(replay = 0)
    var uploadRecipeResult: SharedFlow<NetworkState<BaseResponse>> = _uploadRecipeResult

    var _ingredientsResult: MutableStateFlow<NetworkState<IngredientsResponse>> = MutableStateFlow(NetworkState.Loading)
    var ingredientsResult: StateFlow<NetworkState<IngredientsResponse>> = _ingredientsResult

    fun uploadImage(file: MultipartBody.Part) = viewModelScope.launch {
        launch {
            _uploadImageResult.emit(NetworkState.Loading)
            uploadUseCase.uploadImage(file)
                .catch { error ->
                    _uploadImageResult.emit(NetworkState.Error(400, "${error.message}"))
                }
                .collect { values ->
                    _uploadImageResult.emit(values)
                }
        }
    }

//    fun uploadRecipe(request:UploadRecipeRequest) = viewModelScope.launch {
//        _uploadRecipeResult.value = NetworkState.Loading
//        uploadUseCase.uploadRecipe(request)
//            .catch { error ->
//                _uploadRecipeResult.value = NetworkState.Error(400,"${error.message}")
//            }.collect { values ->
//                _uploadRecipeResult.value = values
//            }
//    }

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