package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.response.SignupResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signup(accessToken:String,nickname:RequestNickname) : Flow<NetworkState<SignupResponse>>
}