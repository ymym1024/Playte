package com.cmc.recipe.data.datasource

import com.cmc.recipe.data.model.RecipeMapper.toEntity
import com.cmc.recipe.data.model.entity.SearchEntity
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.local.dao.RecipeDao
import com.cmc.recipe.data.source.local.dao.SearchDao
import com.cmc.recipe.data.source.remote.api.RecipeService
import com.cmc.recipe.data.source.remote.api.SearchService
import com.cmc.recipe.domain.repository.RecipeRepository
import com.cmc.recipe.domain.repository.SearchRepository
import com.cmc.recipe.utils.NetworkState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.math.ceil

class SearchRepositoryImpl @Inject constructor(
    private val service: SearchService,
    private val dao : SearchDao
) :SearchRepository{

    override fun getSearchRecipe(keyword: String): Flow<NetworkState<RecipesResponse>> = flow{
        val response = service.getSearchRecipe(keyword)
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

    override fun getSearchShortform(keyword: String): Flow<NetworkState<ShortsResponse>> = flow{
        val response = service.getSearchShortform(keyword)
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

    override fun getSearchKeywords(): Flow<NetworkState<SearchKeywordResponse>> = flow{
        val response = service.getSearchKeywords()
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

    override suspend fun insertRecentSearch(item: String): Boolean {
        return try{
            val entity = SearchEntity(search_id = 0, keyword = item, createdDate = Date())
            dao.insert(entity)
            true
        }catch (e: IOException) {
            false
        }
    }

    override fun loadRecentSearch(): Flow<List<SearchEntity>> {
        return flow {
            dao.selectAll().collect { list ->
                emit(list)
            }
        }
    }

}
