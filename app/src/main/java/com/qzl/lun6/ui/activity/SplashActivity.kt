package com.qzl.lun6.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.qzl.lun6.databinding.ActivitySplashBinding
import com.qzl.lun6.logic.MyApplication
import com.qzl.lun6.logic.data.SpfUtil
import com.qzl.lun6.ui.activity.mainactivity.MainActivity
import com.qzl.lun6.utils.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            //todo 获取本地数据判断是否登入  如果没有 跳转登录界面,如果有 跳转mainActivity


            val intent = if (SpfUtil.isLogin) {
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