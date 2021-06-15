package com.qzl.lun6.ui.myviews.qzltableview

import  android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.qzl.lun6.R
import com.qzl.lun6.logic.model.course.Course
import com.qzl.mapplicationdiyview.qzltableview.TableMainAdapter
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

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.tableview_layout, this, true)

        dateBarPager = view.findViewById(R.id.viewpager_date_bar_tableview)
        mainTable = view.findViewById(R.id.viewpager_main_table_tableview)
        dateBarPager.adapter = dateBarAdapter
        mainTable.adapter = mainAdapter

        mainTable.setPageTransformer(MarginPageTransformer(resources.getDimensionPixelOffset(R.dimen.table_main_table_margin)))
        //绑定两pager
        dateBarPager.registerOnPageChangeCallback(MyOnPageChangeCallback(dateBarPager, mainTable))
        mainTable.registerOnPageChangeCallback(MyOnPageChangeCallback(mainTable, dateBarPager))
    }

    /**
     *   @param dates:List<这周的第一天>
     */
    fun setData(dates: List<Calendar>, course: List<Course>) {
        dateBarAdapter.dates = dates
        mainAdapter.setData(dates.size, course)
    }
}