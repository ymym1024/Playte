package com.cmc.recipe.domain.usecase

import com.cmc.recipe.data.source.remote.request.RequestVerifyNickname
import com.cmc.recipe.domain.repository.UserRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: UserRepository
    ) {
    suspend fun verifyNickname(nickname: RequestVerifyNickname) = repository.getVerifyNickname(nickname)
}