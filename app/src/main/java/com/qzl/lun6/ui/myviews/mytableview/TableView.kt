package com.qzl.lun6.ui.myviews.mytableview

import  android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.qzl.lun6.R
import com.qzl.lun6.logic.model.course.Course
import java.util.*

class TableView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    ///////////
    private val dateBarPager: ViewPager2
    private val mainTable: ViewPager2
    private val dateBarAdapter = TableDateAdapter()
    private val mainAdapter = TableMainAdapter()

    val currentItem: LiveData<Int>

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.tableview_layout, this, true)

        dateBarPager = view.findViewById(R.id.viewpager_date_bar_tableview)
        mainTable = view.findViewById(R.id.viewpager_main_table_tableview)
        dateBarPager.adapter = dateBarAdapter
        mainTable.adapter = mainAdapter

        mainTable.setPageTransformer(MarginPageTransformer(resources.getDimensionPixelOffset(R.dimen.table_main_table_margin)))//边距

        //绑定两pager的currentItem同步
        val callback1 = MyOnPageChangeCallback(dateBarPager, mainTable)
        currentItem = callback1.currentItem
        dateBarPager.registerOnPageChangeCallback(callback1)
        mainTable.registerOnPageChangeCallback(MyOnPageChangeCallback(mainTable, dateBarPager))
    }

    /**
     *   绑定date列表和course列表给俩adapter
     *   @param dates:List<这周的第一天>
     */
    fun setData(dates: List<Calendar>, course: List<Course>) {
        dateBarAdapter.dates = dates
        mainAdapter.setData(dates, course)
    }

    private var counter = 0

    fun notifyDataChange() {
        dateBarAdapter.notifyDataSetChanged()
        mainAdapter.notifyDataSetChanged()
        if (counter == 0) {
            //如果是第一次进入 设置currentItem
            setCurrentItem()
        }

    }

    fun setCurrentItem() {
        val dates = dateBarAdapter.dates

        if (dates.isNotEmpty()) {

            val today = Calendar.getInstance()

            val week = (today.get(Calendar.DAY_OF_WEEK) - 1).let {
                if (it <= 0) {
                    7
                } else {
                    it
                }
            }

            val thisWeek =
                today.get(Calendar.WEEK_OF_YEAR) - dates[0].get(Calendar.WEEK_OF_YEAR) + if (week == 7) 0 else 1

            dateBarPager.setCurrentItem(
                if (thisWeek in 1..22) {
                    thisWeek - 1
                } else {
                    0
                }, false
            )

            mainTable.setCurrentItem(
                if (thisWeek in 1..22) {
                    thisWeek - 1
                } else {
                    0
                }, false
            )

            counter = 1
        }
    }

    fun setCurrentItem(thisWeek: Int) {
        dateBarPager.setCurrentItem(
            if (thisWeek in 1..22) {
                thisWeek - 1
            } else {
                0
            }, false
        )

        mainTable.setCurrentItem(
            if (thisWeek in 1..22) {
                thisWeek - 1
            } else {
                0
            }, false
        )
    }
}