package com.youngsun.mysololife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.youngsun.mysololife.auth.IntroActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth

        // 현재 사용자의 uid가 없다면, 로그인/회원가입을 위해 IntroActivity 로 이동.
        if( auth.currentUser?.uid == null ) {
            Handler().postDelayed({
                val intent = Intent( this, IntroActivity :: class.java )
                startActivity(intent)
                finish()
            },1500)
        }

        // 사용자가 App을 사용한 적이 있다면, MainActivity 로 이동.
        else {
            Handler().postDelayed({
                val intent = Intent( this, MainActivity :: class.java )
                startActivity(intent)
                finish()
            },1500)
        }



    }
}