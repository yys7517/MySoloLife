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
import com.youngsun.mysololife.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView( this, R.layout.activity_join )

        auth = Firebase.auth

        binding.btnSubmit.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPass.text.toString()
            val passCheck = binding.edtPassCheck.text.toString()

            var isGoToJoin  = true

            when {
                email.isEmpty() -> { // 이메일이 비어있다면
                    Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    isGoToJoin = false
                }

                password.isEmpty() -> { // 비밀번호가 비어있다면
                    Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    isGoToJoin = false
                }

                password.length < 6 -> { // 비밀번호의 길이가 6보다 짧다면
                    Toast.makeText(this, "비밀번호를 더 길게 입력해주세요.", Toast.LENGTH_SHORT).show()
                    isGoToJoin = false
                    binding.edtPass.text.clear()
                }

                passCheck.isEmpty() -> { // 비밀번호 확인 값이 비어있다면
                    Toast.makeText(this, "비밀번호를 한 번 더 입력해주세요.", Toast.LENGTH_SHORT).show()
                    isGoToJoin = false
                }

                ! password.equals( passCheck ) -> { // 비밀번호가 일치하지 않는다면
                    isGoToJoin = false
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    binding.edtPassCheck.text.clear()
                }

                // 회원가입 조건을 만족한다면 ?
                isGoToJoin -> {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                            // 회원가입이 성공하면 메인 화면으로 이동.
                            val intent = Intent( this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }



        }

    }
}