package com.cmc.recipe.data.datasource

import android.util.Log
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.RecipeMapper.toEntity
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.local.dao.RecipeDao
import com.cmc.recipe.data.source.remote.api.RecipeService
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.domain.repository.RecipeRepository
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import kotlin.math.ceil

class RecipeRepositoryImpl @Inject constructor(
    private val service: RecipeService,
    private val dao : RecipeDao
) :RecipeRepository{

    override fun getRecipes(): Flow<NetworkState<RecipesResponse>> = flow {
        val response = service.getRecipes()
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
        id: Int
    ): Flow<NetworkState<RecipeDetailResponse>> = flow{
        val response = service.getRecipesDetail(id)
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

    override fun postRecipesSave(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postRecipesSave(id)
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

    override fun postRecipesNotSave(id: Int): Flow<NetworkState<BaseResponse>> = flow{
        val response = service.postRecipesNotSave(id)
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

    override fun postRecipesLike(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postRecipesLike(id)
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

    override fun postRecipesUnLike(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postRecipesUnLike(id)
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

    override fun getRecipesTheme(themeName: String): Flow<NetworkState<RecipesResponse>> =flow{
        val response = service.getRecipesTheme(themeName)
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

    override fun postRecipeReport(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postRecipeReport(id)
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

    override fun getNoInterestRecipe(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.getNoInterestRecipe(id)
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

    override fun getRecipesReview(id: Int): Flow<NetworkState<ReviewResponse>> = flow{
        val response = service.getRecipesReview(id)

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

    override fun getRecipesReviewPhotos(id: Int): Flow<NetworkState<PhotoResponse>> =flow{
        val response = service.getRecipesReviewPhotos(id)
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

    override fun getRecipesReviewScores(id: Int): Flow<NetworkState<ReviewScoreResponse>> =flow{
        val response = service.getRecipesReviewScores(id)
        if(response.isSuccessful){
            response.body()?.let { emit(NetworkState.Success(it)) }
        }else{
            try {
                emit(NetworkState.Error(response.code(),response.errorBody()!!.string()))
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun postRecipesReview(id:Int,request: ReviewRequest): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postRecipesReview(id,request)
        if(response.isSuccessful){
            response.body()?.let { emit(NetworkState.Success(it)) }
        }else{
            try {
                emit(NetworkState.Error(response.code(),response.errorBody()!!.string()))
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun updateReviewLike(id: Int): Flow<NetworkState<BaseResponse>> = flow{
        val response = service.updateReviewLike(id)
        if(response.isSuccessful){
            response.body()?.let { emit(NetworkState.Success(it)) }
        }else{
            try {
                emit(NetworkState.Error(response.code(),response.errorBody()!!.string()))
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun updateReviewUnLike(id: Int): Flow<NetworkState<BaseResponse>> = flow{
        val response = service.updateReviewUnLike(id)
        if(response.isSuccessful){
            response.body()?.let { emit(NetworkState.Success(it)) }
        }else{
            try {
                emit(NetworkState.Error(response.code(),response.errorBody()!!.string()))
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun postReviewReport(id: Int): Flow<NetworkState<BaseResponse>> =flow{
        val response = service.postReviewReport(id)
        if(response.isSuccessful){
            response.body()?.let { emit(NetworkState.Success(it)) }
        }else{
            try {
                emit(NetworkState.Error(response.code(),response.errorBody()!!.string()))
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun insertRecentRecipe(item: RecipeItem): Boolean {
        return try{
            dao.insert(item.toEntity())
            true
        }catch (e: IOException) {
            false
        }
    }

    override fun loadRecentRecipes(): Flow<List<RecipeEntity>> {
        return flow {
            dao.selectAll().collect { list ->
                emit(list)
            }
        }
    }
}
