package com.qzl.lun6.ui.myviews.qzlviewpager

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.qzl.lun6.R
import java.lang.reflect.Field

class QzlViewPagerHolder(mContext: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    RelativeLayout(mContext, attrs, defStyleAttr) {

    constructor(mContext: Context, attrs: AttributeSet?) : this(mContext, attrs, 0)
    constructor(mContext: Context) : this(mContext, null)

    private lateinit var mTitle: TextView//标题
    private lateinit var mPager: QzlViewPager//轮播View
    private lateinit var mContainer: LinearLayout//指示器容器
    private lateinit var mDataList: List<QzlViewPagerBaseData>//数据列表

    //private lateinit var mAdapter: QzlViewPagerAdapter//适配器
    private var mIndicatorC: Drawable? = null//指示器 选中
    private var mIndicator: Drawable? = null//指示器 未选择

    private var mShowTitle = true// 是否显示title
    private var mIndicatorPosition = 1 // 指示器位置   0左 1中 2右
    private var mDelay = 2000L //轮播延迟
    private var mIndicatorSize: Int = dip2px(mContext, 5f) //indicator大小
    private var mTitleBarColor: Int = Color.parseColor("#99666666")
    private var mTitleColor: Int = Color.parseColor("#ffffff")

    init {
        mContext.obtainStyledAttributes(attrs, R.styleable.QzlViewPagerHolder).apply {
            mShowTitle = getBoolean(R.styleable.QzlViewPagerHolder_showViewPagerTitle, mShowTitle)
            mDelay = getInteger(R.styleable.QzlViewPagerHolder_switchDelay, mDelay.toInt()).toLong()

            mIndicatorC = getDrawable(R.styleable.QzlViewPagerHolder_indicatorCStyle)
            mIndicator = getDrawable(R.styleable.QzlViewPagerHolder_indicatorStyle)

            mIndicatorPosition =
                getInteger(R.styleable.QzlViewPagerHolder_indicatorPosition, mIndicatorPosition)
            mIndicatorSize =
                getDimensionPixelSize(R.styleable.QzlViewPagerHolder_indicatorSize, mIndicatorSize)

            mTitleBarColor = getColor(R.styleable.QzlViewPagerHolder_titleBarColor, mTitleBarColor)
            mTitleColor = getColor(R.styleable.QzlViewPagerHolder_titleColor, mTitleColor)
            recycle()
        }
        initView()
        initEvent()
    }

    //初始化View
    private fun initView() {
        //轮播图
        mPager = QzlViewPager(context).apply {
            val params = LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
            )
            setDelay(mDelay)
            this@QzlViewPagerHolder.addView(this, params)
        }

        if (mShowTitle) {
            //标题
            mTitle = TextView(context).apply {
                val params = LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
                )
                gravity = Gravity.CENTER_HORIZONTAL//内容水平居中
                setTextColor(mTitleColor)//title文字颜色
                setTypeface(null, Typeface.BOLD)//文字加粗
                setBackgroundColor(mTitleBarColor)//背景颜色
                bringToFront()//前置
                this@QzlViewPagerHolder.addView(this, params)
            }
        }


        //指示器container
        mContainer = LinearLayout(context).apply {
            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(
                    when (mIndicatorPosition) {
                        0 -> ALIGN_PARENT_LEFT//靠左
                        1 -> CENTER_HORIZONTAL//水平居中
                        2 -> ALIGN_PARENT_RIGHT//靠右
                        else -> CENTER_HORIZONTAL//水平居中
                    }
                )//水平对齐方式
                addRule(ALIGN_PARENT_BOTTOM)//垂直对齐方式 对齐parent底
                setMargins(0, 0, 0, dip2px(context, 5f))//右边缘间隔
            }
            //layoutParams = params
            this@QzlViewPagerHolder.addView(this, params)
        }
    }

    //初始化Pager轮播图事件
    private fun initEvent() {
        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                /*
                * @see ViewPager#SCROLL_STATE_IDLE 停止
                * @see ViewPager#SCROLL_STATE_DRAGGING 拖拽
                * @see ViewPager#SCROLL_STATE_SETTLING 归位
                **/
                //切换的状态改变回调
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //切换时回调
            }

            override fun onPageSelected(position: Int) {
                //切换结束回调

                //获得数据个数 设置显示 indicator & title
                setDisplay(position)
            }
        })
    }

    private fun setDisplay(position: Int) {

        val realPosition = position % mDataList.size
        if (mShowTitle) {
            mTitle.text =
                "${
                    if (mDataList[realPosition].title == null) {
                        ""
                    } else mDataList[realPosition].title
                }"
        }

        mContainer.removeAllViews()

        repeat(mDataList.size) {

            val layoutParams = LayoutParams(
                mIndicatorSize, mIndicatorSize
            ).apply {
                setMargins(
                    dip2px(context, 5f),
                    dip2px(context, 0f),
                    dip2px(context, 5f),
                    dip2px(context, 0f)
                )//左右间隔5dp
            }

            val view = View(context).apply {

                if (mIndicatorC == null && mIndicator == null) {
                    //默认指示器格样式
                    if (realPosition == it) {
                        setBackgroundColor(Color.parseColor("#ff5555"))
                    } else {
                        setBackgroundColor(Color.parseColor("#555555"))
                    }
                } else {
                    //传入的样式
                    background = if (realPosition == it)
                        mIndicatorC
                    else mIndicator
                }

                setLayoutParams(layoutParams)
            }

            mContainer.addView(view)
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    /*private fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }*/

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //对外的控制函数

    /*
    * setAdapter()
    * 设置ViewPager的Adapter
    * */
    fun setAdapter(adapter: QzlViewPagerAdapter) {
        //mAdapter = adapter
        mPager.adapter = adapter
        //mPager.currentItem = Int.MAX_VALUE / 2
        mDataList = adapter.getAdapterData()

        //获得数据个数 设置显示 indicator&title
        //设置轮播中间值
        val mid = (Int.MAX_VALUE / mDataList.size / 2) * mDataList.size
        setDisplay(mid)//设置title indicator显示

        mPager.setCurrentItem(mid, false)

        Log.i("size", mid.toString())

    }

    /*
    * changeData(newData: List<UserData>)
    * 改变轮播数据
    * */
    fun changeData(newData: List<QzlViewPagerBaseData>) {
        mPager.adapter.apply {
            setAdapterData(newData)
            mDataList = newData

            //利用反射，强行修改mFirstLayout的值为true
            val mFirstLayout: Field =
                ViewPager::class.java.getDeclaredField("mFirstLayout")
            mFirstLayout.isAccessible = true
            mFirstLayout.set(mPager, true)

            notifyDataSetChanged()

            val mid = (Int.MAX_VALUE / mDataList.size / 2) * mDataList.size
            mPager.currentItem = mid
            setDisplay(mid)//设置title indicator显示

        }

        Log.i("changeData", mPager.currentItem.toString())
    }

    /*
    * setTitleBarColor(color: String)
    * 设置标题栏的背景颜色
    **/
    @Deprecated("useless")
    fun setTitleBarColor(color: String) {
        if ((color.startsWith("#") || color.length in 7..9) && mShowTitle) {
            mTitle.setBackgroundColor(Color.parseColor(color))
        }
    }

    /*
    * setTitleColor(color: String)
    * 设置标题栏字体颜色
    * 默认白色#ffffff
    * */
    @Deprecated("useless")
    fun setTitleColor(color: String = "#ffffff") {
        if ((color.startsWith("#") || color.length in 7..9) && mShowTitle) {
            mTitle.setTextColor(Color.parseColor(color))
        }
    }

    /*
    * setDelay(time: Long = 2000)
    * 设置ViewPager滚动延时 默认2000ms
    */
    @Deprecated("useless")
    fun setDelay(time: Long = 2000) {
        mPager.setDelay(time)
    }

    /*
    * setIndicatorStyle(mBackgroundC: Drawable?, mBackground: Drawable?)
    * 设置Indicator样式 5dp*5dp 默认方形
    * mBackgroundC 选中项
    * mBackground 未选中项
    */
    @Deprecated("useless")
    fun setIndicatorStyle(mBackgroundC: Drawable?, mBackground: Drawable?) {
        this.mIndicatorC = mBackgroundC
        this.mIndicator = mBackground
        setDisplay(mPager.currentItem)
    }
}