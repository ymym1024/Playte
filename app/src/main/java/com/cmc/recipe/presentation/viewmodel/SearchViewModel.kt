package com.cmc.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.usecase.AuthUseCase
import com.cmc.recipe.domain.usecase.RecipeUseCase
import com.cmc.recipe.domain.usecase.SearchUseCase
import com.cmc.recipe.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchUseCase: SearchUseCase) : ViewModel() {

    var _recipeResult: MutableStateFlow<NetworkState<RecipesResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeResult: StateFlow<NetworkState<RecipesResponse>> = _recipeResult

    var _shortsResult: MutableStateFlow<NetworkState<ShortsResponse>> = MutableStateFlow(NetworkState.Loading)
    var shortsResult: StateFlow<NetworkState<ShortsResponse>> = _shortsResult

    fun getSearchRecipe(keyword:String) = viewModelScope.launch {
        _recipeResult.value = NetworkState.Loading
        searchUseCase.getSearchRecipe(keyword)
            .catch { error ->
                _recipeResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _recipeResult.value = values
            }
    }

    fun getSearchShortform(keyword:String) = viewModelScope.launch {
        _shortsResult.value = NetworkState.Loading
        searchUseCase.getSearchShortform(keyword)
            .catch { error ->
                _shortsResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _shortsResult.value = values
            }
    }

}