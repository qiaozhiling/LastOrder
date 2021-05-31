package com.qzl.lun6.ui.fragment.toolbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qzl.lun6.R
import com.qzl.lun6.databinding.FragmentToolboxBinding
import com.qzl.lun6.ui.fragment.BaseFragment

class ToolboxFragment : BaseFragment<FragmentToolboxBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_toolbox, container, false)
    }

}