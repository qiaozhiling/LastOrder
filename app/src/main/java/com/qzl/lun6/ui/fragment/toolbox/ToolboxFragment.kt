package com.qzl.lun6.ui.fragment.toolbox

import android.os.Bundle
import android.view.View
import com.qzl.lun6.R
import com.qzl.lun6.databinding.FragmentToolboxBinding
import com.qzl.lun6.ui.fragment.BaseFragment
import com.qzl.lun6.utils.setStatusBarColor

class ToolboxFragment : BaseFragment<FragmentToolboxBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datalist = listOf(
            CarouselData(R.mipmap.carousel1),
            CarouselData(R.mipmap.carousel2)
        )

        val adapter = CarouselAdapter(datalist)

        binding.carouselToolbox.setAdapter(adapter)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.statues_gray)
    }
}