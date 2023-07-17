package com.cmc.recipe.data.datasource

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.source.remote.api.AuthService
import com.cmc.recipe.data.source.remote.request.RequestVerifyNickname
import com.cmc.recipe.domain.repository.AuthRepository
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService : AuthService
) :AuthRepository{
    override fun getVerifyNickname(nickname: RequestVerifyNickname): Flow<NetworkState<BaseResponse>> = flow{
        val response = authService.verifyNickname(nickname)
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