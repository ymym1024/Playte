package com.cmc.recipe.data.datasource

import com.cmc.recipe.data.model.response.LoginResponse
import com.cmc.recipe.data.model.response.SignupResponse
import com.cmc.recipe.data.source.remote.api.AuthService
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.AuthRepository
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService
) :AuthRepository{

    override fun signup(accessToken:String,nickname: RequestNickname): Flow<NetworkState<SignupResponse>> = flow{
        val response = service.signup(accessToken,nickname)
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

    override fun login(accessToken: String): Flow<NetworkState<LoginResponse>> = flow {
        val response = service.login(accessToken)
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