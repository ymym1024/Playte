package com.cmc.recipe.domain.usecase

import com.cmc.recipe.data.source.remote.request.RequestVerifyNickname
import com.cmc.recipe.domain.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
    ) {
    suspend fun verifyNickname(nickname: RequestVerifyNickname) = repository.getVerifyNickname(nickname)
}