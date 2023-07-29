package com.cmc.recipe.domain.usecase

import com.cmc.recipe.domain.repository.GoogleLoginRepository
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val repository: GoogleLoginRepository
    ) {
    fun fetchAuthInfo(authCode: String) = repository.fetchGoogleAuthInfo(authCode)
}