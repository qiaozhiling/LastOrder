package com.qzl.lun6.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qzl.lun6.R
import com.qzl.lun6.databinding.ActivityMainBinding
import com.qzl.lun6.ui.fragment.my.MyFragment
import com.qzl.lun6.ui.fragment.table.TableFragment
import com.qzl.lun6.ui.fragment.toolbox.ToolboxFragment
import com.qzl.lun6.ui.myviews.myviewpager.MyViewPagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        DisplayUtils.setStatusBarTextColor(window)
        setNav()
    }

    private fun setNav() {

        binding.navViewMain.apply {
            //time 2021/5/30/19:25
            itemIconTintList = null
            itemRippleColor = null

            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.table_navigation_menu -> binding.viewpagerMain.currentItem = 0
                    R.id.toolbox_navigation_menu -> binding.viewpagerMain.currentItem = 1
                    R.id.my_navigation_menu -> binding.viewpagerMain.currentItem = 2
                }
                return@setOnNavigationItemSelectedListener true
            }
        }

        binding.viewpagerMain.adapter = MyViewPagerAdapter(supportFragmentManager).apply {
            addFragment(TableFragment())
            addFragment(ToolboxFragment())
            addFragment(MyFragment())
        }
    }
}