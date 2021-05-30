package com.qzl.lun6.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qzl.lun6.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        DisplayUtils.setStatusBarTextColor(window)

        binding.tvChangeLogin.setOnClickListener {
            if (binding.loginEditGroup.typeFlag) {
                binding.tvChangeLogin.text = "本科生登入"
                binding.loginEditGroup.changeType()
            } else {
                binding.tvChangeLogin.text = "研究生登入"
                binding.loginEditGroup.changeType()
            }
        }

        binding.btmLogin.setOnClickListener {

            if(binding.loginEditGroup.typeFlag) {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }


    }
}