package com.qzl.mapplicationdiyview.qzltableview

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.qzl.lun6.R
import com.qzl.lun6.logic.model.course.Course
import com.qzl.lun6.logic.model.course.TP


/**
 * TODO
 * @param count 周数
 */
class TableMainAdapter() :
    RecyclerView.Adapter<TableMainAdapter.Holder>() {

    inner class Holder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        private val mon: RelativeLayout = itemView.findViewById(R.id.line1_table_item)
        private val tue: RelativeLayout = itemView.findViewById(R.id.line2_table_item)
        private val wed: RelativeLayout = itemView.findViewById(R.id.line3_table_item)
        private val thur: RelativeLayout = itemView.findViewById(R.id.line4_table_item)
        private val fri: RelativeLayout = itemView.findViewById(R.id.line5_table_item)
        private val sat: RelativeLayout = itemView.findViewById(R.id.line6_table_item)
        private val sun: RelativeLayout = itemView.findViewById(R.id.line7_table_item)
        val week = listOf(mon, tue, wed, thur, fri, sat, sun)

        val tableCellHeight: Int =
            context.resources.getDimensionPixelOffset(R.dimen.table_cell_height)
        val tableMainTextSize: Float = context.resources.getDimension(R.dimen.table_main_text)
    }

    private var count: Int = 0

    private var courseList: List<Course>? = null

    fun setData(count: Int, courseList: List<Course>?) {
        this.count = count
        this.courseList = courseList
        notifyDataSetChanged()
    }

    private val colorList = listOf(
        Color.rgb(139, 69, 19),
        Color.rgb(255, 99, 71),
        Color.rgb(135, 206, 250),
        Color.rgb(147, 112, 219),
        Color.rgb(255, 222, 219),
        Color.rgb(175, 238, 173)
    )

    //显示所有课程 包括本周不上的
    private var showAll = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = count

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.table_main_item_layout, parent, false)

        return Holder(view, parent.context)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //TODO
        val weeks = position + 1//周数
        courseList?.let { courseList ->

            for ((index, c) in courseList.withIndex()) {

                val size: Int = colorList.size
                val backgroundColor = colorList[(courseList.size - index) % size]
                drawCourse(holder, c, weeks, backgroundColor)
            }

        }


    }

    /**
     * TODO dialog to make
     * @param weeks 当前是第几周
     */
    private fun drawCourse(
        holder: TableMainAdapter.Holder,
        course: Course,
        weeks: Int,
        backgroundColor: Int
    ) {

        val courseName = course.courseName
        val teacherName = course.teacherName
        val exam = course.exam
        val remark = course.remark
        val transferInfo = course.transferInfo

        //正常上课表
        course.TP?.let { tps ->
            for (tp in tps) {

                val sDW = tp.sDW//单双周

                if ((weeks % 2 == sDW || sDW == 2) && weeks in tp.startWeek..tp.endWeek) {

                    draw(
                        tp,
                        holder,
                        courseName,
                        teacherName,
                        remark,
                        backgroundColor,
                        Color.rgb(255, 255, 255)
                    )

                } else if (showAll) {
                    draw(
                        tp,
                        holder,
                        courseName,
                        teacherName,
                        remark,
                        Color.rgb(220, 220, 220),
                        Color.rgb(128, 128, 128)
                    )
                }

            }

        }

        exam?.let {
            val tp = it.data
            val sDW = tp.sDW//单双周
            if ((weeks % 2 == sDW || sDW == 2) && weeks in tp.startWeek..tp.endWeek) {

                draw(
                    tp,
                    holder,
                    "[考试]$courseName",
                    teacherName,
                    it.detailTime,
                    backgroundColor,
                    Color.rgb(255, 255, 255)
                )
            }
        }

        transferInfo?.let { tp ->
            val sDW = tp.sDW//单双周
            if ((weeks % 2 == sDW || sDW == 2) && weeks in tp.startWeek..tp.endWeek) {
                draw(
                    tp,
                    holder,
                    "[调课]$courseName",
                    teacherName,
                    "从哪来到哪去",
                    backgroundColor,
                    Color.rgb(255, 255, 255)
                )
            }
        }
    }

    /**
     * @see Holder.week
     * @param backgroundColor 背景颜色 Color.rgb()
     * @param textColor 字体颜色 Color.rgb()
     */
    private fun draw(
        tp: TP,
        holder: TableMainAdapter.Holder,
        courseName: String,
        teacherName: String?,
        remark: String?,
        backgroundColor: Int,
        textColor: Int
    ) {

        val week = tp.week - 1 //星期几 脚标 0-6
        val start = tp.startSection
        val end = tp.endSection //start>=end
        val place = tp.place

        val textViewParam = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.CENTER_IN_PARENT)
        }

        val textView = TextView(holder.context).apply {
            val text = "$courseName\n$place"
            this.text = text
            setTextColor(context.getColor(R.color.white))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, holder.tableMainTextSize)
            gravity = Gravity.CENTER
            setPadding(5, 0, 5, 0)
            setTextColor(textColor)
            layoutParams = textViewParam
        }

        val relativeParam =
            RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            ).apply {
                addRule(RelativeLayout.CENTER_IN_PARENT)
            }

        val relativeLayout = RelativeLayout(holder.context).apply {
            removeAllViews()
            addView(textView)
            layoutParams = relativeParam
        }

        val cardParam =
            RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (end - start + 1) * holder.tableCellHeight - 10
            ).apply {
                setMargins(0, (start - 1) * holder.tableCellHeight + 7, 0, 7)
            }

        val cardView = CardView(holder.context).apply {
            layoutParams = cardParam
            setCardBackgroundColor(backgroundColor)
            radius = 15f
            cardElevation = 0f
            maxCardElevation = 0f
            removeAllViews()
            addView(relativeLayout)
            setOnClickListener {
                //TODO
                Toast.makeText(holder.context, courseName, Toast.LENGTH_SHORT).show()
            }
        }

        holder.week[week].apply {
            addView(cardView)
        }

    }
}
