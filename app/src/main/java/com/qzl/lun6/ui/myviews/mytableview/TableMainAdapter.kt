package com.qzl.lun6.ui.myviews.mytableview

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.qzl.lun6.R
import com.qzl.lun6.logic.model.course.Course
import com.qzl.lun6.logic.model.course.TP
import com.qzl.lun6.ui.myviews.InfoDialog
import java.util.*

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

    var courseList: List<Course> = listOf()

    private val courseCode = 0
    private val examCode = 1
    private val transferCode = 2

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
        Color.rgb(210, 105, 180),
        Color.rgb(230, 140, 0),
        Color.rgb(160, 82, 45),
        Color.rgb(204, 153, 204),
        Color.rgb(68, 118, 216),
        Color.rgb(95, 158, 160),
        Color.rgb(175, 238, 173),
        Color.rgb(230, 220, 140)
    )
    private val whiteColor = Color.rgb(255, 255, 255)
    private val lightGrayColor = Color.rgb(220, 220, 220)
    private val deepGrayColor = Color.rgb(128, 128, 128)

    //?????????????????? ?????????????????????
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
        val weeks = position + 1 //????????????

        holder.week.iterator().forEach {
            it.removeAllViews()
        }

        //TODO ?????? ????????? ???????????? ??????
        val coursesForWeek = mutableListOf<Pair<Int, TP>>()//???
        val coursesNotThisWeek = mutableListOf<Pair<Int, TP>>()//????????????
        val examForWeek = mutableListOf<Pair<Int, TP>>()//??????
        val transferForWeek = mutableListOf<Pair<Int, TP>>()//??????
        val userCourse = mutableListOf<Pair<Int, TP>>()//???????????????

        //???tp ?????????
        for (index in courseList.indices) {
            //???????????????????????????/??????
            val course = courseList[index]

            //???????????????????????????
            coursesForWeek.addAll(course.TPs.filter {
                it.isThisWeek(weeks)
            }.map {
                Pair(index, it)
            })

            //????????????????????????
            coursesNotThisWeek.addAll(course.TPs.filter { !it.isThisWeek(weeks) }
                .map { Pair(index, it) })

            //????????????????????????
            course.exam.getTP(dates[0])?.let {
                if (it.isThisWeek(weeks)) {
                    examForWeek.add(Pair(index, it))
                }
            }

            //????????????????????????
            course.transferInfo.getTP()?.let {
                if (it.isThisWeek(weeks)) {
                    transferForWeek.add(Pair(index, it))
                }
            }
        }

        val toDelete = mutableListOf<Pair<Int, TP>>()

        //???????????????????????????????????????
        for (courseNot in coursesNotThisWeek) {
            //??????????????????
            for (courseIs in coursesForWeek) {
                if (courseIs.second.conflict(courseNot.second)) {
                    //???????????? ???????????? ????????????????????????courseNot
                    toDelete.add(courseNot)
                }
            }
        }
        coursesNotThisWeek.removeAll(toDelete)
        toDelete.clear()

        //????????????????????????
        for (courseIs in coursesForWeek) {
            for (b in examForWeek) {
                if (courseIs.second.conflict(b.second) && courseIs.second != b.second) {
                    //????????? ????????????
                    toDelete.add(courseIs)
                }
            }
        }
        coursesForWeek.removeAll(toDelete)
        toDelete.clear()

        //???????????????????????????
        for (courseNot in coursesNotThisWeek) {
            for (b in examForWeek) {
                if (courseNot.second.conflict(b.second) && courseNot.second != b.second) {
                    //????????? ????????????
                    toDelete.add(courseNot)
                }
            }
        }
        coursesNotThisWeek.removeAll(toDelete)
        toDelete.clear()

        //????????????????????????
        for (courseIs in coursesForWeek) {
            for (b in transferForWeek) {
                if (courseIs.second.conflict(b.second) && courseIs.second != b.second) {
                    //?????????  ????????????
                    toDelete.add(courseIs)
                }
            }
        }
        coursesForWeek.removeAll(toDelete)
        toDelete.clear()

        //????????????????????????
        for (courseNot in coursesNotThisWeek) {
            for (b in transferForWeek) {
                if (courseNot.second.conflict(b.second) && courseNot.second != b.second) {
                    //?????????  ????????????
                    toDelete.add(courseNot)
                }
            }
        }
        coursesNotThisWeek.removeAll(toDelete)


        /////////////////////////////////?????????
        //?????????
        for (c in coursesForWeek) {
            val course = courseList[c.first]
            val color = colorList[c.first % colorList.size]
            draw(
                holder,
                course,
                c.second,
                courseCode,
                color,
                whiteColor
            )

        }

        //????????????
        if (showAll) {
            for (c in coursesNotThisWeek) {
                val course = courseList[c.first]
                draw(
                    holder,
                    course,
                    c.second,
                    courseCode,
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
                    holder,
                    course,
                    c.second,
                    examCode,
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
                    holder,
                    course,
                    c.second,
                    transferCode,
                    color,
                    whiteColor
                )
            }
        }
    }

    /**
     * ????????????????????????item ??????????????????????????????
     * @see Holder.week
     * @param backgroundColor ???????????? Color.rgb()
     * @param textColor ???????????? Color.rgb()
     */
    private fun draw(
        holder: Holder,
        course: Course,
        tp: TP,
        code: Int,
        backgroundColor: Int,
        textColor: Int
    ) {

        val courseName = when (code) {
            examCode -> "[??????]${course.courseName}"
            transferCode -> "[??????]${course.courseName}"
            else -> course.courseName
        }

        /*  val teacherName = course.teacherName
          val remark = when (code) {
              examCode -> course.exam.origin
              transferCode -> course.transferInfo.origin
              else -> course.remark
          }*/

        val week = tp.week - 1 //????????? ?????? 0-6
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
                //TODO dialog
                InfoDialog(
                    holder.context,
                    tp,
                    course,
                    code,
                    this@TableMainAdapter
                ).show()
            }
        }

        holder.week[week].apply {
            addView(cardView)
        }

    }


}
