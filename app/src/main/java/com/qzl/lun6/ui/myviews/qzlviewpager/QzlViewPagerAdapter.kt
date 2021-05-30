package com.qzl.lun6.ui.myviews.qzlviewpager

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

abstract class QzlViewPagerAdapter(private var dataList: List<QzlViewPagerBaseData>) :
    PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = `object` == view

    override fun getCount(): Int = Int.MAX_VALUE//伪无限循环

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //初始化View
        /*val item = LayoutInflater.from(container.context)
            .inflate(R.layout.item_pager, container, false)
        val iv = item.findViewById(R.id.cover) as ImageView*/

        val itemView = ImageView(container.context)
        itemView.scaleType = ImageView.ScaleType.FIT_XY//设置图片显示格式
        /* //设置数据
        val realPosition = position % mData.size

        //设置图片
        itemView.setImageResource(mData[realPosition].picRes)
        itemView.scaleType = ImageView.ScaleType.FIT_CENTER

        itemView.setOnClickListener { onIVClickListener?.setOnIVClick(itemView) }*/

        //设置ImageView显示 点击 等
        val realPosition = position % dataList.size
        setIV(itemView, realPosition, dataList)

        if (itemView.parent is ViewGroup) (itemView.parent as ViewGroup).removeView(itemView)
        container.addView(itemView)
        return itemView
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    //position may be fake
    //设置ImageView显示 点击 等
    abstract fun setIV(iv: ImageView, position: Int, dataList: List<QzlViewPagerBaseData>)

    fun setAdapterData(data: List<QzlViewPagerBaseData>) {
        this.dataList = data
    }

    fun getAdapterData() = this.dataList

}
