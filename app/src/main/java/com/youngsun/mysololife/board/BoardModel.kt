package com.youngsun.mysololife.board

// 게시글 ( 제목, 내용, 작성자, 작성 시간 )
data class BoardModel (
    val title : String = "",
    val contents : String = "",
    val uid : String = "",
    val time : String = ""
)