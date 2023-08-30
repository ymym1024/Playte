package com.cmc.recipe.data.datasource

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.model.response.NicknameResponse
import com.cmc.recipe.data.source.remote.api.UserService
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.UserRepository
import com.cmc.recipe.utils.NetworkState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService : UserService
) :UserRepository{
    override fun getVerifyNickname(nickname: RequestNickname): Flow<NetworkState<NicknameResponse>> = flow{
        val response = userService.verifyNickname(nickname)
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

    override fun getMyInfo(): Flow<NetworkState<MyInfoResponse>> = flow{
        val response = userService.getMyInfo()
        if(response.isSuccessful){
            response.body()?.let {
                emit(NetworkState.Success(it))
            }
        }else{
            try {
                val error = response.errorBody()!!.string().trimIndent()
                val result = Gson().fromJson(error, BaseResponse::class.java)

                emit(NetworkState.Error(result.code.toInt(),result.message))
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun changeNickname(nickname: RequestNickname): Flow<NetworkState<BaseResponse>> = flow{
        val response = userService.changeNickname(nickname)
        if(response.isSuccessful){
            response.body()?.let {
                emit(NetworkState.Success(it))
            }
        }else{
            try {
                val error = response.errorBody()!!.string().trimIndent()
                val result = Gson().fromJson(error, BaseResponse::class.java)

                emit(NetworkState.Error(result.code.toInt(),result.message))
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}