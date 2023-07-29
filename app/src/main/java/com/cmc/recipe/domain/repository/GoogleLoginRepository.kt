package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.response.GoogleLoginResponse
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow

interface GoogleLoginRepository {
    fun fetchGoogleAuthInfo(authCode:String) : Flow<NetworkState<GoogleLoginResponse>>
}