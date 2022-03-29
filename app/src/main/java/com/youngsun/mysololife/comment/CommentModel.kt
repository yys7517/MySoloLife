package com.youngsun.mysololife.comment

// 댓글 ( 내용, 작성 시간, 작성자 )
data class CommentModel (
    val comment : String = "",
    val time : String = "",
    val writer : String = ""
)