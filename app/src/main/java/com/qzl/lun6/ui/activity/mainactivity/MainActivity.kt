package com.qzl.lun6.ui.activity.mainactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.qzl.lun6.R
import com.qzl.lun6.databinding.ActivityMainBinding
import com.qzl.lun6.logic.Repository
import com.qzl.lun6.ui.activity.BaseActivity
import com.qzl.lun6.ui.fragment.MyFragment
import com.qzl.lun6.ui.fragment.TableFragment
import com.qzl.lun6.ui.fragment.toolboxfragment.ToolboxFragment
import com.qzl.lun6.utils.log
import com.qzl.lun6.utils.setDarkStatusBarTextColor
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>() {

    //private lateinit var viewModel: MainViewModel

    val viewModel by lazy { ViewModelProvider(this).get(TableViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDarkStatusBarTextColor()

        val fragments = listOf(
            TableFragment(),
            ToolboxFragment(),
            MyFragment()
        )

        setNav(fragments)


        lifecycleScope.launch {

            launch {
                if (Repository.isLogin) {
                    viewModel.loadData()
                } else {
                    //网络请求课
                    //viewModel.requestData()
                    Repository.isLogin = true
                }
                "setNav".log()
            }
        }


    }

    override fun onPause() {
        viewModel.saveData()
        super.onPause()
    }

    private fun setNav(fragments: List<Fragment>) {

        binding.viewpagerMain.apply {
            adapter =
                MainViewPagerAdapter(supportFragmentManager, lifecycle, fragments)
            isUserInputEnabled = false

        }


        binding.navViewMain.apply {
            //time 2021/5/30/19:25
            itemIconTintList = null
            itemRippleColor = null

            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.table_navigation_menu -> binding.viewpagerMain.setCurrentItem(0, false)
                    R.id.toolbox_navigation_menu -> binding.viewpagerMain.setCurrentItem(1, false)
                    R.id.my_navigation_menu -> binding.viewpagerMain.setCurrentItem(2, false)
                }
                return@setOnNavigationItemSelectedListener true
            }
        }

    }
}