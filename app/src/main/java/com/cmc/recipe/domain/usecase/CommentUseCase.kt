package com.cmc.recipe.domain.usecase

import com.cmc.recipe.data.model.Comment
import com.cmc.recipe.data.source.remote.request.CommentRequest
import com.cmc.recipe.data.source.remote.request.RequestNickname
import com.cmc.recipe.domain.repository.CommentRepository
import com.cmc.recipe.domain.repository.UserRepository
import javax.inject.Inject

class CommentUseCase @Inject constructor(
    private val repository: CommentRepository
    ) {
    fun getShortfromComment(id:Int) = repository.getShortfromComment(id)

    fun postShortfromCommentLike(id:Int) = repository.postShortfromCommentLike(id)

    fun postShortfromCommentSave(id:Int,comment: CommentRequest) = repository.postShortfromCommentSave(id, comment)

    fun reportShortfromComment(id:Int) = repository.reportShortfromComment(id)

    fun postShortfromCommentUnLike(id:Int) = repository.postShortfromCommentUnLike(id)

    fun getRecipeComment(id:Int) = repository.getRecipeComment(id)

    fun postRecipeCommentLike(id:Int) = repository.postRecipeCommentLike(id)

    fun postRecipeCommentSave(id:Int,comment: CommentRequest) = repository.postRecipeCommentSave(id, comment)

    fun reportRecipeComment(id:Int) = repository.reportRecipeComment(id)

    fun postRecipeCommentUnLike(id:Int) = repository.postRecipeCommentUnLike(id)

}