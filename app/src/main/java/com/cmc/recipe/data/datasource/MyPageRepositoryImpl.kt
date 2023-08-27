package com.cmc.recipe.data.datasource

import com.cmc.recipe.data.model.response.BaseResponse
import com.cmc.recipe.data.model.response.MyInfoResponse
import com.cmc.recipe.data.model.response.ReviewMyResponse
import com.cmc.recipe.data.model.response.ReviewResponse
import com.cmc.recipe.data.source.remote.api.MyPageService
import com.cmc.recipe.data.source.remote.api.UserService
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.MyPageRepository
import com.cmc.recipe.domain.repository.UserRepository
import com.cmc.recipe.utils.NetworkState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val service : MyPageService
) : MyPageRepository {
    override fun getMyReview(): Flow<NetworkState<ReviewMyResponse>> = flow{
        val response = service.getMyReview()
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

    override fun deleteReview(id:Int): Flow<NetworkState<BaseResponse>> = flow{
        val response = service.deleteReview(id)
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