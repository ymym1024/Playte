package com.cmc.recipe.data.model.response

data class CommentResponse(
    val code: String,
    val data: CommentData,
    val message: String
)

data class CommentData(
    val content: List<CommentContent>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)

data class CommentContent(
    val comment_content: String,
    val comment_id: Int,
    var comment_likes: Int,
    val comment_writtenby: String,
    val created_at: String,
    var is_liked: Boolean,
    val replyList: List<Any>
)