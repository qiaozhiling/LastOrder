package com.qzl.lun6.ui.fragment.my

import android.os.Bundle
import android.view.*
import com.qzl.lun6.R
import com.qzl.lun6.databinding.FragmentMyBinding
import com.qzl.lun6.logic.MyApplication
import com.qzl.lun6.ui.fragment.BaseFragment
import com.qzl.lun6.utils.setStatusBarColor

class MyFragment : BaseFragment<FragmentMyBinding>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.toolbarMy.inflateMenu(R.menu.my_menu)

    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.statues_gray)
    }
}