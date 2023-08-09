package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getVerifyNickname(nickname:RequestNickname) : Flow<NetworkState<BaseResponse>>

    fun getMyInfo() : Flow<NetworkState<MyInfoResponse>>
}