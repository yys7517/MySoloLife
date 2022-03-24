package com.youngsun.mysololife.contentsList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.youngsun.mysololife.R

class ContentShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_show)

        val webUrl = intent.getStringExtra("webUrl").toString()
        val webView = findViewById<WebView>(R.id.webView)

        webView.webViewClient = WebViewClient() // webViewClient를 설정해주어야 앱 내에서 웹 사이트를 열 수 있다.

        webView.loadUrl( webUrl )
    }
}