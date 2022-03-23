package com.youngsun.mysololife.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.youngsun.mysololife.MainActivity
import com.youngsun.mysololife.R
import com.youngsun.mysololife.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView( this, R.layout.activity_intro )

        auth = Firebase.auth

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
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 익명 로그인 성공
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                        val intent = Intent( this, MainActivity::class.java )
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {
                        // 실패
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}