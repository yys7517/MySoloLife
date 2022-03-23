package com.youngsun.mysololife.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.youngsun.mysololife.R
import com.youngsun.mysololife.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView( this, R.layout.activity_intro )

        binding = ActivityIntroBinding.inflate( layoutInflater )
        setContentView( binding.root )

        // 로그인 버튼 클릭
        binding.btnLogin.setOnClickListener {
            val intent = Intent( this, LoginActivity::class.java )
            startActivity(intent)
        }

        // 회원가입 버튼 클릭
        binding.btnJoin.setOnClickListener {
            val intent = Intent( this, JoinActivity::class.java )
            startActivity(intent)
        }

        // 비회원 로그인 버튼 클릭
        binding.btnAnonymous.setOnClickListener {

        }
    }
}