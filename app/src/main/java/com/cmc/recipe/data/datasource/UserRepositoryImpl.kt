package com.cmc.recipe.data.datasource

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.source.remote.api.UserService
import com.cmc.recipe.data.source.remote.request.RequestVerifyNickname
import com.cmc.recipe.domain.repository.UserRepository
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService : UserService
) :UserRepository{
    override fun getVerifyNickname(nickname: RequestVerifyNickname): Flow<NetworkState<BaseResponse>> = flow{
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
}