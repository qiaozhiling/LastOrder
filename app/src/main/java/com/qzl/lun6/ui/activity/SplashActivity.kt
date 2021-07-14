package com.qzl.lun6.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.qzl.lun6.databinding.ActivitySplashBinding
import com.qzl.lun6.logic.Repository
import com.qzl.lun6.ui.activity.loginactivity.LoginActivity
import com.qzl.lun6.ui.activity.mainactivity.MainActivity
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val isLogin = Repository.isLogin
            val intent = if (isLogin) {
                //已登入 直接进入
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                //未登入 跳转登录
                Intent(this@SplashActivity, LoginActivity::class.java)
            }
            startActivity(intent)
            this@SplashActivity.finish()
        }
    }
}