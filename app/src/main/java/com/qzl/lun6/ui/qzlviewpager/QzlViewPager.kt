package com.qzl.lun6.ui.qzlviewpager

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager

class QzlViewPager(mContext: Context) : ViewPager(mContext) {
    // private val mHandler: Handler = Handler(Looper.getMainLooper())
    private var delay: Long = 2000
    private lateinit var mRunnable: Runnable

    init {

        mRunnable = Runnable {
            currentItem += 1
            Log.i("Loop Handler", "currentItem$currentItem")
            postDelayed(mRunnable, delay)
        }

        //设置Pager触摸事件
        setOnTouchListener { _: View, motionEvent: MotionEvent ->
            Log.i("Loop Handler", motionEvent.action.toString())
            when (motionEvent.action) {

                //0 点击
                MotionEvent.ACTION_DOWN -> {
                    Log.i("Loop Handler", "ACTION_DOWN")
                    stopLoop()
                }

                //1 放开
                MotionEvent.ACTION_UP -> {
                    Log.i("Loop Handler", "ACTION_UP")
                    startLoop()
                }

                //2 滑动
                MotionEvent.ACTION_MOVE -> {
                    stopLoop()
                }

                //取消warning
                -100 -> {
                    Log.i("Loop Handler", "else")
                    performClick()
                }
            }
            return@setOnTouchListener false
        }

        Log.i("Loop Handler", "init")

    }

    //进入
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        Log.i("Loop Handler", "进入")
        startLoop()
    }

    //离开
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        Log.i("Loop Handler", "离开")
        stopLoop()
    }

    private fun startLoop() {
        Log.i("Loop Handler", "startLoop")
        postDelayed(mRunnable, delay)
    }

    private fun stopLoop() {
        Log.i("Loop Handler", "stopLoop")
        removeCallbacks(mRunnable)
    }

    override fun getAdapter(): QzlViewPagerAdapter {
        return super.getAdapter() as QzlViewPagerAdapter
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //设置轮播延迟
    fun setDelay(time: Long) {
        //单位ms
        if (time >= 0) delay = time
    }
}