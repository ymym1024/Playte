package com.cmc.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.usecase.AuthUseCase
import com.cmc.recipe.domain.usecase.RecipeUseCase
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val recipeUseCase: RecipeUseCase) : ViewModel() {

    var _recipeResult: MutableStateFlow<NetworkState<RecipesResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeResult: StateFlow<NetworkState<RecipesResponse>> = _recipeResult

    var _recipeDetailResult: MutableStateFlow<NetworkState<RecipeDetailResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeDetailResult: StateFlow<NetworkState<RecipeDetailResponse>> = _recipeDetailResult

    var _recipeSaveResult : MutableStateFlow<NetworkState<BaseResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeSaveResult: StateFlow<NetworkState<BaseResponse>> = _recipeSaveResult

    var _recipeShortsResult : MutableStateFlow<NetworkState<ShortsResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeShortsResult: StateFlow<NetworkState<ShortsResponse>> = _recipeShortsResult.asStateFlow()

    var _recipeShortsDetailResult : MutableStateFlow<NetworkState<ShortsDetailResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeShortsDetailResult: StateFlow<NetworkState<ShortsDetailResponse>> = _recipeShortsDetailResult

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

    fun getRecipesShortform() = viewModelScope.launch {
        _recipeShortsResult.value = NetworkState.Loading
        recipeUseCase.getRecipesShortform()
            .catch { error ->
                _recipeShortsResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeShortsResult.value = values
            }
    }

    fun getRecipesShortformDetail(id:Int) = viewModelScope.launch {
        _recipeShortsDetailResult.value = NetworkState.Loading
        recipeUseCase.getRecipesShortformDetail(id)
            .catch { error ->
                _recipeShortsDetailResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeShortsDetailResult.value = values
            }
    }
}