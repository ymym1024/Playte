package com.cmc.recipe.data.source.remote.api

import com.cmc.recipe.data.model.response.*
import com.cmc.recipe.data.source.remote.request.CommentRequest
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.data.source.remote.request.ReviewRequest
import com.cmc.recipe.data.source.remote.request.UploadShortsRequest
import retrofit2.Response
import retrofit2.http.*

interface CommentService {

    @GET("/api/v1/comments/shortform/{shortform-id}")
    suspend fun getShortfromComment(@Path("shortform-id")id:Int) : Response<CommentResponse>

    @POST("/api/v1/comments/shortform/{shortform-comment-id}/report")
    suspend fun reportShortfromComment(@Path("shortform-comment-id")id:Int) : Response<BaseResponse>

    @POST("/api/v1/comments/shortform/{shortform-id}")
    suspend fun postShortfromCommentSave(@Path("shortform-id")id:Int,@Body comment: CommentRequest) : Response<BaseResponse>

    @POST("/api/v1/comments/shortform/like/{shortform-comment-id}")
    suspend fun postShortfromCommentLike(@Path("shortform-comment-id")id:Int) : Response<BaseResponse>

    @POST("/api/v1/comments/shortform/unlike/{shortform-comment-id}")
    suspend fun postShortfromCommentUnLike(@Path("shortform-comment-id")id:Int) : Response<BaseResponse>


}