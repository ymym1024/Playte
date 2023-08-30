package com.cmc.recipe.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.recipe.data.model.ErrorMessage
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.entity.SearchEntity
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.usecase.AuthUseCase
import com.cmc.recipe.domain.usecase.RecipeUseCase
import com.cmc.recipe.domain.usecase.SearchUseCase
import com.cmc.recipe.utils.NetworkState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchUseCase: SearchUseCase) : ViewModel() {

    var _recipeResult: MutableStateFlow<NetworkState<RecipesResponse>> = MutableStateFlow(NetworkState.Loading)
    var recipeResult: StateFlow<NetworkState<RecipesResponse>> = _recipeResult

    var _shortsResult: MutableStateFlow<NetworkState<ShortsResponse>> = MutableStateFlow(NetworkState.Loading)
    var shortsResult: StateFlow<NetworkState<ShortsResponse>> = _shortsResult

    var _keywordResult: MutableStateFlow<NetworkState<SearchKeywordResponse>> = MutableStateFlow(NetworkState.Loading)
    var keywordResult: StateFlow<NetworkState<SearchKeywordResponse>> = _keywordResult

    private val _recentKeywordResult = MutableSharedFlow<List<SearchEntity>>()
    val recentKeywordResult: Flow<List<SearchEntity>>
        get() = _recentKeywordResult

    fun getSearchRecipe(keyword:String) = viewModelScope.launch {
        _recipeResult.value = NetworkState.Loading
        searchUseCase.getSearchRecipe(keyword)
            .catch { error ->
                _recipeResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                if(values is NetworkState.Error){
                    val msg = Gson().fromJson("${values.message}", ErrorMessage::class.java)
                    _recipeResult.emit(NetworkState.Error(msg.code.toInt(),"${msg.message}"))
                } else if (values is NetworkState.Success) {
                    _recipeResult.value = values
                }

            }
    }

    fun getSearchShortform(keyword:String) = viewModelScope.launch {
        _shortsResult.value = NetworkState.Loading
        searchUseCase.getSearchShortform(keyword)
            .catch { error ->
                _shortsResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                if(values is NetworkState.Error){
                    val msg = Gson().fromJson("${values.message}", ErrorMessage::class.java)
                    _shortsResult.emit(NetworkState.Error(msg.code.toInt(),"${msg.message}"))
                } else if (values is NetworkState.Success) {
                    _shortsResult.value = values
                }
            }
    }

    fun getSearchKeywords() = viewModelScope.launch {
        _keywordResult.value = NetworkState.Loading
        searchUseCase.getSearchKeywords()
            .catch { error ->
                _keywordResult.value = NetworkState.Error(400,"${error.message}")
            }.collect { values ->
                _keywordResult.value = values
            }
    }

    //최근 검색어
    fun insertRecentRecipe(keyword: String){
        viewModelScope.launch (Dispatchers.IO){
            searchUseCase.insertRecentSearch(keyword)
        }
    }

    fun loadRecentSearch() = viewModelScope.launch {
        searchUseCase.loadRecentSearch()
            .collect{ values ->
                _recentKeywordResult.emit(values)
                Log.d("ddd","${values}")
            }
    }

}