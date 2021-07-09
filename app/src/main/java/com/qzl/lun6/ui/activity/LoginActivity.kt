package com.qzl.lun6.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.qzl.lun6.databinding.ActivityLoginBinding
import com.qzl.lun6.ui.activity.mainactivity.MainActivity
import com.qzl.lun6.utils.exception.LoginDataException
import com.qzl.lun6.utils.exception.NetException
import com.qzl.lun6.utils.setDarkStatusBarTextColor
import com.qzl.lun6.utils.toastInScope
import com.qzl.lun6.logic.network.NetUtils
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private var logining = false//是否正在登入中

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDarkStatusBarTextColor()

        binding.tvChangeLogin.setOnClickListener {
            if (binding.loginEditGroupLogin.typeFlag) {
                binding.tvChangeLogin.text = "本科生登入"
                binding.loginEditGroupLogin.changeType()
            } else {
                binding.tvChangeLogin.text = "研究生登入"
                binding.loginEditGroupLogin.changeType()
            }
        }

        binding.btmLogin.setOnClickListener {
            if (binding.loginEditGroupLogin.typeFlag) {
                //是本科生模式才有后续
                if (!logining) {
                    lifecycleScope.launch {
                        try {

                            binding.btmLogin.text = "正 在 登 录 中..."

                            val mIntent = Intent(this@LoginActivity, MainActivity::class.java)

                            val loginData = binding.loginEditGroupLogin.getEditData()
                            //数据已有空判断
                            NetUtils.login(
                                loginData["user"]!!,
                                loginData["paswd"]!!,
                                loginData["code"]!!
                            )

                            //登入成功跳转
                            startActivity(mIntent)
                            this@LoginActivity.finish()

                        } catch (e: Exception) {
                            when {
                                (e is NetException) || (e is LoginDataException) -> {
                                    e.message?.toastInScope()
                                }//其他问题闪退
                            }
                        } finally {
                            binding.btmLogin.text = "登 录"
                            logining = false
                        }
                    }
                }

            }
        }

        binding.loginEditGroupLogin.setCodeImageOnClick {
            getCodePic()
        }

        binding.tvVisitorLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this@LoginActivity.finish()
        }

        getCodePic()

    }

    private fun getCodePic() {

        lifecycleScope.launch {

            try {
                val pic = NetUtils.getVerifyCode()
                binding.loginEditGroupLogin.setCodeImage(pic)
            } catch (e: Exception) {
                when {
                    (e is NetException) || (e is LoginDataException) -> {
                        e.message?.toastInScope()
                    }
                }
            }
        }

    }
}