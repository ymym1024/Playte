package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface UploadRepository {
    fun uploadImage(file: MultipartBody.Part) : Flow<NetworkState<BaseResponse>>
}