package com.qzl.lun6.ui.myviews.myviewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyViewPagerAdapter(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragments: MutableList<Fragment> = mutableListOf()

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    fun addFragment(fg: Fragment) {
        mFragments.add(fg)
    }


}