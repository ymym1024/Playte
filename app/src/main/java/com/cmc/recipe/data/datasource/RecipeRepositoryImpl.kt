package com.cmc.recipe.data.datasource

import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.api.AuthService
import com.cmc.recipe.data.source.remote.api.RecipeService
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.AuthRepository
import com.cmc.recipe.domain.repository.RecipeRepository
import com.cmc.recipe.utils.NetworkState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val service: RecipeService
) :RecipeRepository{

    override fun getRecipes(accessToken: String): Flow<NetworkState<RecipesResponse>> = flow {
        val response = service.getRecipes(accessToken)
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

    override fun getRecipesDetail(
        accessToken: String,
        id: Int
    ): Flow<NetworkState<RecipeDetailResponse>> = flow{
        val response = service.getRecipesDetail(accessToken,id)
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
