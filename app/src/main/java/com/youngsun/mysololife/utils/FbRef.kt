package com.youngsun.mysololife.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FbRef {
    companion object {
        private val database = Firebase.database

        val category_all = database.getReference("contents")        // 전체 카테고리 컨텐츠 DB 경로
        val bookmarkRef = database.getReference("bookmark_list")    // 북마크 컨텐츠 DB 경로
        val boardRef = database.getReference("board")               // 커뮤니티 게시글 DB 경로
    }
}