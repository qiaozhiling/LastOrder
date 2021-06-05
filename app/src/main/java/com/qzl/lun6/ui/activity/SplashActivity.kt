package com.qzl.lun6.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.qzl.lun6.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay((Math.random() * 15).toLong())
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            this@SplashActivity.finish()
        }
    }


}