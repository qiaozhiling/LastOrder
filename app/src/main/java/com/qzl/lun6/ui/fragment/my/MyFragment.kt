package com.qzl.lun6.ui.fragment.my

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.qzl.lun6.R
import com.qzl.lun6.databinding.FragmentMyBinding
import com.qzl.lun6.ui.fragment.BaseFragment
import com.qzl.lun6.utils.setStatusBarColor

class MyFragment : BaseFragment<FragmentMyBinding>() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.my_menu, menu)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarMy)

    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.statues_gray)
    }
}