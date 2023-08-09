package com.cmc.recipe.domain.usecase

import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.UserRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: UserRepository
    ) {
    fun verifyNickname(nickname: RequestNickname) = repository.getVerifyNickname(nickname)

    fun getMyInfo() = repository.getMyInfo()
}