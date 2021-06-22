package com.qzl.lun6.ui.myviews.qzltableview

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
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.qzl.lun6.R
import com.qzl.lun6.logic.model.course.Course
import com.qzl.lun6.logic.model.course.TP
import com.qzl.lun6.utils.log
import java.util.*


/**
 * TODO
 * @param count 周数
 */
class TableMainAdapter :
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

    private var dates: List<Calendar> = listOf()

    private var courseList: List<Course> = listOf()

    fun setData(dates: List<Calendar>, courseList: List<Course>) {
        this.dates = dates
        this.courseList = courseList
        notifyDataSetChanged()
    }

    private val colorList = listOf(
        Color.rgb(210, 99, 71),
        Color.rgb(135, 206, 250),
        Color.rgb(139, 69, 19),
        Color.rgb(147, 112, 219),
        Color.rgb(230, 140, 0),
        Color.rgb(160, 82, 45),
        Color.rgb(204, 153, 204),
        Color.rgb(68, 118, 216),
        Color.rgb(95, 158, 160),
        Color.rgb(175, 238, 173)
    )
    private val whiteColor = Color.rgb(255, 255, 255)
    private val lightGrayColor = Color.rgb(220, 220, 220)
    private val deepGrayColor = Color.rgb(128, 128, 128)

    //显示所有课程 包括本周不上的
    private var showAll = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = dates.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.table_main_item_layout, parent, false)

        return Holder(view, parent.context)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val weeks = position + 1 //当前周数

        holder.week.iterator().forEach {
            it.removeAllViews()
        }

        //TODO 挑出 这一周 要显示的 所有
        val courseForWeek = mutableListOf<Pair<Int, TP>>()//课
        val examForWeek = mutableListOf<Pair<Int, TP>>()//考试
        val transferForWeek = mutableListOf<Pair<Int, TP>>()//调课
        val userCourse = mutableListOf<Pair<Int, TP>>()//用户自加课 todo

        //按tp 挑出课
        for (index in courseList.indices) {
            //遍历所有的课（单位/门）
            val course = courseList[index]

            courseForWeek.addAll(course.TP.map {
                Pair(index, it)
            })

            //抓出所有本周考试
            course.exam.getTP(dates[0])?.let {
                if (it.isThisWeek(weeks)) {
                    examForWeek.add(Pair(index, it))
                }
            }

            course.transferInfo.getTP()?.let {
                if (it.isThisWeek(weeks)) {
                    transferForWeek.add(Pair(index, it))
                }
            }
        }

        val toDelete = mutableListOf<Pair<Int, TP>>()
        //删除重复的
        for (c in courseForWeek) {
            if (!c.second.isThisWeek(weeks)) {
                //不是这周的课
                for (b in courseForWeek) {
                    if (c.second.conflict(b.second) && c.second != b.second) {
                        //与其他课 存在冲突
                        toDelete.add(c)
                    }
                }
            }
        }

        for (c in courseForWeek) {
            for (b in examForWeek) {
                if (c.second.conflict(b.second) && c.second != b.second) {
                    //与考试 存在冲突
                    toDelete.add(c)
                }
            }
        }

        for (c in courseForWeek) {
            if (!c.second.isThisWeek(weeks)) {
                //不是这周的课
                for (b in transferForWeek) {
                    if (c.second.conflict(b.second) && c.second != b.second) {
                        //与调课  存在冲突
                        toDelete.add(c)
                    }
                }
            }
        }

        courseForWeek.removeAll(toDelete)
        /////////////////////////////////开始画
        for (c in courseForWeek) {
            val course = courseList[c.first]
            val color = colorList[c.first % colorList.size]

            if (c.second.isThisWeek(weeks)) {
                draw(
                    c.second,
                    holder,
                    course.courseName,
                    course.teacherName,
                    course.remark,
                    color,
                    whiteColor
                )
            } else {
                draw(
                    c.second,
                    holder,
                    course.courseName,
                    course.teacherName,
                    course.remark,
                    lightGrayColor,
                    deepGrayColor
                )
            }
        }

        for (c in examForWeek) {
            val course = courseList[c.first]
            val color = colorList[c.first % colorList.size]
            if (c.second.isThisWeek(weeks)) {
                draw(
                    c.second,
                    holder,
                    "[考试]${course.courseName}",
                    course.teacherName,
                    course.remark,
                    color,
                    whiteColor
                )
            }
        }

        for (c in transferForWeek) {
            val course = courseList[c.first]
            val color = colorList[c.first % colorList.size]
            if (c.second.isThisWeek(weeks)) {
                draw(
                    c.second,
                    holder,
                    "[调课]${course.courseName}",
                    course.teacherName,
                    course.remark,
                    color,
                    whiteColor
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
        holder: Holder,
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
                //TODO diolog
                Toast.makeText(holder.context, "$courseName\n$remark", Toast.LENGTH_SHORT).show()
            }
        }

        holder.week[week].apply {
            addView(cardView)
        }

    }


}
