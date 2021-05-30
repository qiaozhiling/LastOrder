package com.qzl.lun6.ui.myviews

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class MyViewPager(context: Context, attributeSet: AttributeSet?) :
    ViewPager(context, attributeSet) {

    constructor(context: Context) : this(context, null)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
        //取消滑动
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // 如果点击
            performClick();
        }
        //取消滑动
        return false
    }

    override fun setCurrentItem(item: Int) {
        //取消平滑翻页
        super.setCurrentItem(item, false)
    }
}