package com.cmc.recipe.data.datasource

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.LoginResponse
import com.cmc.recipe.data.model.response.SignupResponse
import com.cmc.recipe.data.source.remote.api.AuthService
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.AuthRepository
import com.cmc.recipe.utils.NetworkState
import com.google.gson.Gson
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

    override fun logout(refreshToken: String): Flow<NetworkState<BaseResponse>> = flow{
        val response = service.logout(refreshToken)
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

    override fun refreshToken(refreshToken: String): Flow<NetworkState<SignupResponse>> = flow{
        val response = service.refreshToken(refreshToken)
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

    override fun withdrawal(): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.withdrawal()
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
