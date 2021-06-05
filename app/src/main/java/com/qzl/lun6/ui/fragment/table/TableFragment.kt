package com.qzl.lun6.ui.fragment.table

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.qzl.lun6.R
import com.qzl.lun6.databinding.FragmentTableBinding
import com.qzl.lun6.ui.fragment.BaseFragment
import com.qzl.lun6.utils.setStatusBarColor


class TableFragment : BaseFragment<FragmentTableBinding>() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.table_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarTable)


    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.white)
    }
}