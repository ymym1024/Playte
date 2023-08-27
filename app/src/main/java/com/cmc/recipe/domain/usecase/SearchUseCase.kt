package com.cmc.recipe.domain.usecase

import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.domain.repository.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: SearchRepository
    ) {
    fun getSearchRecipe(keyword:String) = repository.getSearchRecipe(keyword)

    fun getSearchShortform(keyword:String) = repository.getSearchShortform(keyword)

    fun getSearchKeywords() = repository.getSearchKeywords()

    //최근 검색어
    suspend fun insertRecentSearch(keyword: String) = repository.insertRecentSearch(keyword)

    fun loadRecentSearch() = repository.loadRecentSearch()
}