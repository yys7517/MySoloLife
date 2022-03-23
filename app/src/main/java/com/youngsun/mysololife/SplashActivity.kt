package com.youngsun.mysololife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.youngsun.mysololife.auth.IntroActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // 3초 후 IntroActivity로 Intent를 통하여 이동.
        Handler().postDelayed({
            val intent = Intent( this, IntroActivity :: class.java )
            startActivity(intent)
            finish()
        },3000)
    }
}