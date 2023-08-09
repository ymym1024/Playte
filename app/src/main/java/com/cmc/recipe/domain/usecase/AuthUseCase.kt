package com.cmc.recipe.domain.usecase

import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.AuthRepository
import com.cmc.recipe.domain.repository.UserRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
    ) {
    fun signup(accessToken:String,nickname: RequestNickname) = repository.signup(accessToken,nickname)

    fun login(accessToken:String) = repository.login(accessToken)

    fun logout(accessToken:String,refreshToken:String) = repository.logout(accessToken,refreshToken)
}