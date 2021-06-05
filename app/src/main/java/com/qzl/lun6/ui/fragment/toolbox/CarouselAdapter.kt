package com.qzl.lun6.ui.fragment.toolbox

import android.widget.ImageView
import com.qzl.lun6.ui.myviews.qzlviewpager.QzlViewPagerAdapter
import com.qzl.lun6.ui.myviews.qzlviewpager.QzlViewPagerBaseData

class CarouselAdapter(data: List<QzlViewPagerBaseData>) : QzlViewPagerAdapter(data) {

    override fun setIV(iv: ImageView, position: Int, dataList: List<QzlViewPagerBaseData>) {
        val data = dataList[position] as CarouselData

        iv.setImageResource(data.resID)
    }

}