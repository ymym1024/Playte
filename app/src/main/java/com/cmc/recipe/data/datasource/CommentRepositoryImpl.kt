package com.cmc.recipe.data.datasource

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.CommentResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.source.remote.api.CommentService
import com.cmc.recipe.data.source.remote.api.UserService
import com.cmc.recipe.data.source.remote.request.CommentRequest
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.CommentRepository
import com.cmc.recipe.domain.repository.UserRepository
import com.cmc.recipe.utils.NetworkState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val service : CommentService
) :CommentRepository{
    override fun getShortfromComment(id: Int): Flow<NetworkState<CommentResponse>> = flow{
        val response = service.getShortfromComment(id)
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

    override fun reportShortfromComment(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.reportShortfromComment(id)
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

    override fun postShortfromCommentSave(
        id: Int,
        comment: CommentRequest
    ): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postShortfromCommentSave(id,comment)
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

    override fun postShortfromCommentLike(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postShortfromCommentLike(id)
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

    override fun postShortfromCommentUnLike(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postShortfromCommentUnLike(id)
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

    override fun getRecipeComment(id: Int): Flow<NetworkState<CommentResponse>> =flow{
        val response = service.getRecipeComment(id)
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

    override fun reportRecipeComment(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.reportRecipeComment(id)
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

    override fun postRecipeCommentSave(
        id: Int,
        comment: CommentRequest
    ): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postRecipeCommentSave(id,comment)
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

    override fun postRecipeCommentLike(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postRecipeCommentLike(id)
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

    override fun postRecipeCommentUnLike(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postRecipeCommentUnLike(id)
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