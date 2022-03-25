package com.youngsun.mysololife.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {
    companion object {

        // 현재 시간 String으로 반환하기
        fun getTime() : String {

            val currentDateTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format( currentDateTime )

            return dateFormat
        }
    }
}