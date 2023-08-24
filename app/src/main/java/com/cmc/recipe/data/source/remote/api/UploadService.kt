package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.source.remote.request.RequestNickname
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UploadService {

    @Multipart
    @POST("/api/v1/file/upload/image")
    suspend fun uploadImage(@Part file: MultipartBody.Part) : Response<BaseResponse>

}