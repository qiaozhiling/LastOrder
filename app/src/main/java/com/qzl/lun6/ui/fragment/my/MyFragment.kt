package com.qzl.lun6.ui.fragment.my

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import com.qzl.lun6.R
import com.qzl.lun6.databinding.FragmentMyBinding
import com.qzl.lun6.logic.Repository
import com.qzl.lun6.logic.data.SpfUtil
import com.qzl.lun6.ui.activity.LoginActivity
import com.qzl.lun6.ui.activity.TableSettingActivity
import com.qzl.lun6.ui.fragment.BaseFragment
import com.qzl.lun6.utils.setStatusBarColor
import com.qzl.lun6.utils.toast
import kotlinx.coroutines.launch

class MyFragment : BaseFragment<FragmentMyBinding>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.toolbarMy.apply {
            inflateMenu(R.menu.my_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.setting_my_menu -> {
                        "退出登入".toast()

                        lifecycleScope.launch {
                            SpfUtil.clearAll()
                            Repository.deleteAllCourse()
                        }

                        startActivity(Intent(context, LoginActivity::class.java))

                        activity?.finish()
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }

    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.statues_gray)
    }
}