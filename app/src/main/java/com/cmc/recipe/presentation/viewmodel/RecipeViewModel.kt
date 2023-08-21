package com.cmc.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.RecipeDetailResponse
import com.cmc.recipe.data.model.response.RecipesResponse
import com.cmc.recipe.data.model.response.SignupResponse
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

    fun getRecipes(accessToken:String) = viewModelScope.launch {
        _recipeResult.value = NetworkState.Loading
        recipeUseCase.getRecipes(accessToken)
            .catch { error ->
                _recipeResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeResult.value = values
            }
    }

    fun getRecipesDetail(accessToken:String,id:Int) = viewModelScope.launch {
        _recipeDetailResult.value = NetworkState.Loading
        recipeUseCase.getRecipesDetail(accessToken,id)
            .catch { error ->
                _recipeDetailResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeDetailResult.value = values
            }
    }

    fun postRecipesSave(accessToken:String,id:Int) = viewModelScope.launch {
        _recipeSaveResult.value = NetworkState.Loading
        recipeUseCase.postRecipesSave(accessToken,id)
            .catch { error ->
                _recipeSaveResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeSaveResult.value = values
            }
    }

    fun postRecipesNotSave(accessToken:String,id:Int) = viewModelScope.launch {
        _recipeSaveResult.value = NetworkState.Loading
        recipeUseCase.postRecipesNotSave(accessToken,id)
            .catch { error ->
                _recipeSaveResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeSaveResult.value = values
            }
    }
}