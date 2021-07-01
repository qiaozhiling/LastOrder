package com.qzl.lun6.ui.activity.mainactivity

import android.os.Bundle
import com.qzl.lun6.R
import com.qzl.lun6.databinding.ActivityMainBinding
import com.qzl.lun6.ui.activity.BaseActivity
import com.qzl.lun6.ui.fragment.my.MyFragment
import com.qzl.lun6.ui.fragment.table.TableFragment
import com.qzl.lun6.ui.fragment.toolbox.ToolboxFragment
import com.qzl.lun6.utils.setDarkStatusBarTextColor

class MainActivity : BaseActivity<ActivityMainBinding>() {

    //private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        initView()
    }

    private fun initView() {
        setDarkStatusBarTextColor()
        setNav()
    }

    private fun setNav() {

        val fragments = listOf(
            TableFragment(),
            ToolboxFragment(),
            MyFragment()
        )


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