package com.cmc.recipe.domain.usecase

import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.UploadRepository
import com.cmc.recipe.domain.repository.UserRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadUseCase @Inject constructor(
    private val repository: UploadRepository
    ) {

    fun uploadImage(file: MultipartBody.Part) = repository.uploadImage(file)
}