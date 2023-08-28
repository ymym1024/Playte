package com.cmc.recipe.data.datasource

import android.util.Log
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.RecipeMapper.toEntity
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.local.dao.RecipeDao
import com.cmc.recipe.data.source.remote.api.RecipeService
import com.cmc.recipe.data.source.remote.api.ShortsService
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.domain.repository.RecipeRepository
import com.cmc.recipe.domain.repository.ShortsRepository
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import kotlin.math.ceil

class ShortsRepositoryImpl @Inject constructor(
    private val service: ShortsService,
) : ShortsRepository {

    override fun getRecipesShortform(): Flow<NetworkState<ShortsResponse>> = flow {
        val response = service.getRecipesShortform()
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

    override fun getRecipesShortformDetail(id:Int): Flow<NetworkState<ShortsDetailResponse>> = flow{
        val response = service.getRecipesShortformDetail(id)
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

    override fun postShortformLike(id: Int): Flow<NetworkState<BaseResponse>> = flow{
        val response = service.postShortformLike(id)
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

    override fun postShortformUnLike(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postShortformUnLike(id)
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
