package com.cmc.recipe.domain.usecase

import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.MyPageRepository
import com.cmc.recipe.domain.repository.UserRepository
import javax.inject.Inject

class MyPageUseCase @Inject constructor(
    private val repository: MyPageRepository
    ) {
    fun getMyReview() = repository.getMyReview()

    fun deleteReview(id:Int) = repository.deleteReview(id)

    fun getSaveRecipe() = repository.getSaveRecipe()

    fun getWrittenRecipe() = repository.getWrittenRecipe()
}