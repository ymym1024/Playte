package com.cmc.recipe.domain.repository

import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.model.RecipeItem
import com.cmc.recipe.data.model.entity.RecipeEntity
import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.CommentRequest
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.utils.NetworkState
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    fun getShortfromComment(id:Int) : Flow<NetworkState<CommentResponse>>

    fun reportShortfromComment(id:Int) : Flow<NetworkState<BaseResponse>>

    fun postShortfromCommentSave(id:Int,comment:CommentRequest) : Flow<NetworkState<BaseResponse>>

    fun postShortfromCommentLike(id:Int) : Flow<NetworkState<BaseResponse>>

    fun postShortfromCommentUnLike(id:Int) : Flow<NetworkState<BaseResponse>>

    fun getRecipeComment(id:Int) : Flow<NetworkState<CommentResponse>>

    fun reportRecipeComment(id:Int) : Flow<NetworkState<BaseResponse>>

    fun postRecipeCommentSave(id:Int,comment:CommentRequest) : Flow<NetworkState<BaseResponse>>

    fun postRecipeCommentLike(id:Int) : Flow<NetworkState<BaseResponse>>

    fun postRecipeCommentUnLike(id:Int) : Flow<NetworkState<BaseResponse>>

}