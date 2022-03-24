package com.youngsun.mysololife.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FbRef {
    companion object {
        private val database = Firebase.database

        val category_all = database.getReference("contents")

        val bookmarkRef = database.getReference("bookmark_list")
    }
}