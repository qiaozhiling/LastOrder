package com.qzl.lun6.ui.fragment.table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qzl.lun6.R
import com.qzl.lun6.databinding.FragmentTableBinding
import com.qzl.lun6.ui.fragment.BaseFragment


class TableFragment : BaseFragment<FragmentTableBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

}