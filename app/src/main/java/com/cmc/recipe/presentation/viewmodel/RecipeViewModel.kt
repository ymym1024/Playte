package com.cmc.recipe.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.domain.usecase.AuthUseCase
import com.cmc.recipe.domain.usecase.RecipeUseCase
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val recipeUseCase: RecipeUseCase) : ViewModel() {

    private val _reciepeId = MutableStateFlow(0)
    val recipeId : StateFlow<Int> = _reciepeId.asStateFlow()

    var _recipeResult: MutableStateFlow<NetworkState<RecipesResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeResult: StateFlow<NetworkState<RecipesResponse>> = _recipeResult

    var _recipeDetailResult: MutableStateFlow<NetworkState<RecipeDetailResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeDetailResult: StateFlow<NetworkState<RecipeDetailResponse>> = _recipeDetailResult

    var _recipeSaveResult : MutableStateFlow<NetworkState<BaseResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeSaveResult: StateFlow<NetworkState<BaseResponse>> = _recipeSaveResult

    var _reviewResult : MutableStateFlow<NetworkState<ReviewResponse>> = MutableStateFlow(NetworkState.Loading)
    var reviewResult: StateFlow<NetworkState<ReviewResponse>> = _reviewResult

    var _reviewScoreResult : MutableStateFlow<NetworkState<ReviewScoreResponse>> = MutableStateFlow(NetworkState.Loading)
    var reviewScoreResult: StateFlow<NetworkState<ReviewScoreResponse>> = _reviewScoreResult

    var _reviewPhotosResult : MutableStateFlow<NetworkState<PhotoResponse>> = MutableStateFlow(NetworkState.Loading)
    var reviewPhotosResult: StateFlow<NetworkState<PhotoResponse>> = _reviewPhotosResult

    var _reviewAddResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var reviewAddResult = _reviewAddResult.asSharedFlow()

    var _reviewLikeResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var reviewLikeResult = _reviewLikeResult.asSharedFlow()

    var _reviewUnLikeResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var reviewUnLikeResult = _reviewUnLikeResult.asSharedFlow()

    var _reviewReportResult : MutableSharedFlow<NetworkState<BaseResponse>> = MutableSharedFlow()
    var reviewReportResult = _reviewReportResult.asSharedFlow()

    fun updateReicpeId(id:Int) = viewModelScope.launch {
        _reciepeId.value = id
    }

    fun getRecipes() = viewModelScope.launch {
        _recipeResult.value = NetworkState.Loading
        recipeUseCase.getRecipes()
            .catch { error ->
                _recipeResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeResult.value = values
            }
    }

    fun getRecipesDetail(id:Int) = viewModelScope.launch {
        _recipeDetailResult.value = NetworkState.Loading
        recipeUseCase.getRecipesDetail(id)
            .catch { error ->
                _recipeDetailResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeDetailResult.value = values
            }
    }

    fun postRecipesSave(id:Int) = viewModelScope.launch {
        _recipeSaveResult.value = NetworkState.Loading
        recipeUseCase.postRecipesSave(id)
            .catch { error ->
                _recipeSaveResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeSaveResult.value = values
            }
    }

    fun postRecipesNotSave(id:Int) = viewModelScope.launch {
        _recipeSaveResult.value = NetworkState.Loading
        recipeUseCase.postRecipesNotSave(id)
            .catch { error ->
                _recipeSaveResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeSaveResult.value = values
            }
    }

    // 리뷰
    fun getRecipesReview(id:Int) = viewModelScope.launch {
        _reviewResult.value = NetworkState.Loading
        recipeUseCase.getRecipesReview(id)
            .catch { error ->
                _reviewResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _reviewResult.value = values
            }
    }

    fun getRecipesReviewPhotos(id:Int) = viewModelScope.launch {
        _reviewPhotosResult.value = NetworkState.Loading
        recipeUseCase.getRecipesReviewPhotos(id)
            .catch { error ->
                _reviewPhotosResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _reviewPhotosResult.value = values
            }
    }

    fun getRecipesReviewScores(id:Int) = viewModelScope.launch {
        _reviewScoreResult.value = NetworkState.Loading
        recipeUseCase.getRecipesReviewScores(id)
            .catch { error ->
                _reviewScoreResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _reviewScoreResult.value = values
            }
    }

    fun postRecipesReview(request: ReviewRequest) = viewModelScope.launch {
        _reviewAddResult.emit(NetworkState.Loading)
        recipeUseCase.postRecipesReview(request)
            .catch { error ->
                _reviewAddResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _reviewAddResult.emit(values)
            }
    }

    fun updateReviewLike(id:Int) = viewModelScope.launch {
        _reviewLikeResult.emit(NetworkState.Loading)
        recipeUseCase.updateReviewLike(id)
            .catch { error ->
                _reviewLikeResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _reviewLikeResult.emit(values)
            }
    }

    fun updateReviewUnLike(id:Int) = viewModelScope.launch {
        _reviewUnLikeResult.emit(NetworkState.Loading)
        recipeUseCase.updateReviewUnLike(id)
            .catch { error ->
                _reviewUnLikeResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _reviewUnLikeResult.emit(values)
            }
    }

    fun postReviewReport(id:Int) = viewModelScope.launch {
        _reviewReportResult.emit(NetworkState.Loading)
        recipeUseCase.postReviewReport(id)
            .catch { error ->
                _reviewReportResult.emit(NetworkState.Error(400,"${error.message}"))
            }.collect { values ->
                _reviewReportResult.emit(values)
            }
    }
}