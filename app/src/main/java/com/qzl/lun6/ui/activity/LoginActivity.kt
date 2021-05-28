package com.qzl.lun6.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qzl.lun6.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        DisplayUtils.setStatusBarTextColor(window)

        tv_change_login.setOnClickListener {
            if (loginEditGroup.typeFlag) {
                tv_change_login.text = "本科生登入"
                loginEditGroup.changeType()
            } else {
                tv_change_login.text = "研究生登入"
                loginEditGroup.changeType()
            }
        }

        btm_login.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}