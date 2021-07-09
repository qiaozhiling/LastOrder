package com.qzl.lun6.ui.activity

import android.os.Bundle
import android.view.MenuItem
import com.qzl.lun6.databinding.ActivityTableSettingBinding
import com.qzl.lun6.utils.setDarkStatusBarTextColor


class TableSettingActivity : BaseActivity<ActivityTableSettingBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDarkStatusBarTextColor()

        binding.toolbarTableSetting.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}