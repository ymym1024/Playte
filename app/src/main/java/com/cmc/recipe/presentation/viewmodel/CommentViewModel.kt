package com.cmc.recipe.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.CommentResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.source.remote.request.CommentRequest
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.usecase.CommentUseCase
import com.cmc.recipe.domain.usecase.UserUseCase
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(private val commentUseCase: CommentUseCase) : ViewModel() {

    var _commentResult: MutableStateFlow<NetworkState<CommentResponse>> = MutableStateFlow(NetworkState.Loading)
    var commentResult: StateFlow<NetworkState<CommentResponse>> = _commentResult

    var _reportResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var reportResult = _reportResult.asSharedFlow()

    var _commentSaveResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var commentSaveResult = _commentSaveResult.asSharedFlow()

    var _commentLikeResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var commentLikeResult = _commentLikeResult.asSharedFlow()

    var _commentUnLikeResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var commentUnLikeResult = _commentUnLikeResult.asSharedFlow()

    var _commentRecipeResult: MutableStateFlow<NetworkState<CommentResponse>> = MutableStateFlow(NetworkState.Loading)
    var commentRecipeResult: StateFlow<NetworkState<CommentResponse>> = _commentRecipeResult

    var _reportRecipeResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var reportRecipeResult = _reportRecipeResult.asSharedFlow()

    var _commentRecipeSaveResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var commentRecipeSaveResult = _commentRecipeSaveResult.asSharedFlow()

    var _commentRecipeLikeResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var commentRecipeLikeResult = _commentRecipeLikeResult.asSharedFlow()

    var _commentRecipeUnLikeResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var commentRecipeUnLikeResult = _commentRecipeUnLikeResult.asSharedFlow()

    fun getShortfromComment(id:Int) = viewModelScope.launch {
        _commentResult.value = NetworkState.Loading
        commentUseCase.getShortfromComment(id)
            .catch { error ->
                _commentResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _commentResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _commentResult.emit(values)
                }
            }
    }

    fun reportShortfromComment(id:Int) = viewModelScope.launch {
        _reportResult.emit(NetworkState.Loading)
        commentUseCase.reportShortfromComment(id)
            .catch { error ->
                _reportResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _reportResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _reportResult.emit(values)
                }
            }
    }

    fun postShortfromCommentSave(id:Int,comment:CommentRequest) = viewModelScope.launch {
        _commentSaveResult.emit(NetworkState.Loading)
        commentUseCase.postShortfromCommentSave(id,comment)
            .catch { error ->
                _commentSaveResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _commentSaveResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _commentSaveResult.emit(values)
                }
            }
    }

    fun postShortfromCommentLike(id:Int) = viewModelScope.launch {
        _commentLikeResult.emit(NetworkState.Loading)
        commentUseCase.postShortfromCommentLike(id)
            .catch { error ->
                _commentLikeResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _commentLikeResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _commentSaveResult.emit(values)
                }
            }
    }

    fun postShortfromCommentUnLike(id:Int) = viewModelScope.launch {
        _commentUnLikeResult.emit(NetworkState.Loading)
        commentUseCase.postShortfromCommentUnLike(id)
            .catch { error ->
                _commentUnLikeResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _commentUnLikeResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _commentUnLikeResult.emit(values)
                }
            }
    }

    fun getRecipeComment(id:Int) = viewModelScope.launch {
        _commentRecipeResult.value = NetworkState.Loading
        commentUseCase.getRecipeComment(id)
            .catch { error ->
                _commentRecipeResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _commentRecipeResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _commentRecipeResult.emit(values)
                }
            }
    }

    fun reportRecipeComment(id:Int) = viewModelScope.launch {
        _reportRecipeResult.emit(NetworkState.Loading)
        commentUseCase.reportRecipeComment(id)
            .catch { error ->
                _reportRecipeResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _reportRecipeResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _reportRecipeResult.emit(values)
                }
            }
    }

    fun postRecipeCommentSave(id:Int,comment:CommentRequest) = viewModelScope.launch {
        _commentRecipeSaveResult.emit(NetworkState.Loading)
        commentUseCase.postRecipeCommentSave(id,comment)
            .catch { error ->
                _commentRecipeSaveResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _commentRecipeSaveResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _commentRecipeSaveResult.emit(values)
                }
            }
    }

    fun postRecipeCommentLike(id:Int) = viewModelScope.launch {
        _commentRecipeLikeResult.emit(NetworkState.Loading)
        commentUseCase.postRecipeCommentLike(id)
            .catch { error ->
                _commentRecipeLikeResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _commentRecipeLikeResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _commentRecipeLikeResult.emit(values)
                }
            }
    }

    fun postRecipeCommentUnLike(id:Int) = viewModelScope.launch {
        _commentRecipeUnLikeResult.emit(NetworkState.Loading)
        commentUseCase.postRecipeCommentUnLike(id)
            .catch { error ->
                _commentRecipeUnLikeResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                if (values is NetworkState.Error) {
                    _commentRecipeUnLikeResult.emit(NetworkState.Error(values.code,"${values.message}"))
                } else if (values is NetworkState.Success) {
                    _commentRecipeUnLikeResult.emit(values)
                }
            }
    }

}