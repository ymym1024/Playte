package com.cmc.recipe.data.datasource

import com.cmc.recipe.BuildConfig
import com.cmc.recipe.data.model.response.GoogleLoginResponse
import com.cmc.recipe.data.source.remote.api.GoogleService
import com.cmc.recipe.data.source.remote.request.GoogleLoginRequest
import com.cmc.recipe.domain.repository.GoogleLoginRepository
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GoogleLoginRepositoryImpl @Inject constructor(
    private val googleService: GoogleService
) :GoogleLoginRepository{

    override fun fetchGoogleAuthInfo(authCode: String): Flow<NetworkState<GoogleLoginResponse>> = flow {
        val response = googleService.fetchGoogleAuthInfo(
            GoogleLoginRequest(
                grant_type ="authorization_code",
                client_id = BuildConfig.CLIENT_ID,
                client_secret = BuildConfig.CLIENT_SECRET_ID,
                redirect_uri = "",
                code = authCode
            )
        )

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