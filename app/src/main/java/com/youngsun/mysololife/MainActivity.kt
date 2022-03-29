package com.youngsun.mysololife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.youngsun.mysololife.databinding.ActivityMainBinding
import com.youngsun.mysololife.setting.SettingActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView( this, R.layout.activity_main )

        binding.btnSetting.setOnClickListener {
            val intent = Intent( this, SettingActivity::class.java )
            startActivity(intent)
        }


    }
}