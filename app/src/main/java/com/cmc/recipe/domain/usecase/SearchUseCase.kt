package com.cmc.recipe.domain.usecase

import com.cmc.recipe.domain.repository.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: SearchRepository
    ) {
    fun getSearchRecipe(keyword:String) = repository.getSearchRecipe(keyword)
}