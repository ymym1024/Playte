package com.cmc.recipe.data.datasource

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.IngredientsResponse
import com.cmc.recipe.data.source.remote.api.UploadService
import com.cmc.recipe.data.source.remote.request.UploadRecipeRequest
import com.cmc.recipe.domain.repository.UploadRepository
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import java.io.IOException
import javax.inject.Inject

class UploadRepositoryImpl @Inject constructor(
    private val service : UploadService
) :UploadRepository{
    override fun uploadImage(file: MultipartBody.Part): Flow<NetworkState<BaseResponse>> = flow{
        val response = service.uploadImage(file)
        if(response.isSuccessful){
            response.body()?.let {
                emit(NetworkState.Success(it))
            }
        }else{
            try {
                emit(NetworkState.Error(response.code(),response.errorBody()!!.string()))
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun uploadRecipe(request: UploadRecipeRequest): Flow<NetworkState<BaseResponse>> = flow{
        val response = service.uploadRecipe(request)
        if(response.isSuccessful){
            response.body()?.let {
                emit(NetworkState.Success(it))
            }
        }else{
            try {
                emit(NetworkState.Error(response.code(),response.errorBody()!!.string()))
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun getIngredients(): Flow<NetworkState<IngredientsResponse>> = flow{
        val response = service.getIngredients()
        if(response.isSuccessful){
            response.body()?.let {
                emit(NetworkState.Success(it))
            }
        }else{
            try {
                emit(NetworkState.Error(response.code(),response.errorBody()!!.string()))
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}