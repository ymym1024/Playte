package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.entity.SearchEntity
import com.cmc.recipe.data.model.response.RecipesResponse
import com.cmc.recipe.data.model.response.SearchKeywordResponse
import com.cmc.recipe.data.model.response.ShortsResponse
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface SearchRepository {

    fun getSearchRecipe(keyword:String) : Flow<NetworkState<RecipesResponse>>

    fun getSearchShortform(keyword:String) : Flow<NetworkState<ShortsResponse>>

    fun getSearchKeywords() : Flow<NetworkState<SearchKeywordResponse>>

    // 최근 검색어 저장
    suspend fun insertRecentSearch(item: String): Boolean

    fun loadRecentSearch() : Flow<List<SearchEntity>>

}