package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.source.remote.request.RequestVerifyNickname
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getVerifyNickname(nickname:RequestVerifyNickname) : Flow<NetworkState<BaseResponse>>
}