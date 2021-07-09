package com.qzl.lun6.ui.myviews

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.qzl.lun6.R
import com.qzl.lun6.logic.model.course.Course
import com.qzl.lun6.logic.model.course.TP
import com.qzl.lun6.ui.myviews.qzltableview.TableMainAdapter
import com.qzl.lun6.utils.toast


class InfoDialog(
    context: Context,
    tp: TP,
    private val course: Course,
    code: Int,//类型 课程 考试 调课
    private val adapter: TableMainAdapter
) :
    AlertDialog(context) {

    private val courseName: TextView
    private val place: TextView
    private val teacher: TextView
    private val sections: TextView
    private val weeks: TextView
    private val remark: TextView
    private val delete: TextView
    private val edit: ImageView
    private val exit: ImageView

    private val courseCode = 0
    private val examCode = 1
    private val transferCode = 2

    init {
        val view = View.inflate(context, R.layout.info_layout, null).apply {
            courseName = findViewById(R.id.textview_coursename_infodialog)
            place = findViewById(R.id.textview_place_infodialog)
            teacher = findViewById(R.id.textview_teacher_infodialog)
            sections = findViewById(R.id.textview_sections_infodialog)
            weeks = findViewById(R.id.textview_weeks_infodialog)
            remark = findViewById(R.id.textview_remark_infodialog)
            delete = findViewById(R.id.textview_delete_infodialog)
            edit = findViewById(R.id.imageView_edit_infodialog)
            exit = findViewById(R.id.imageView_exit_infodialog)
        }

        val courseName = when (code) {
            examCode -> "[考试]${course.courseName}"
            transferCode -> "[调课]${course.courseName}"
            else -> course.courseName
        }
        val teacherName = course.teacherName
        val remark = when (code) {
            examCode -> course.exam.origin
            transferCode -> course.transferInfo.origin
            else -> course.remark
        }
        val startSection = tp.startSection
        val endSection = tp.endSection //start>=end
        val startWeeks = tp.startWeeks
        val endWeeks = tp.endWeeks //start>=end
        val place = tp.place

/////////////////////////
        this.place.text = place
        this.courseName.text = courseName
        this.teacher.text = teacherName
        sections.text = "${startSection}-${endSection}"
        weeks.text = "${startWeeks}-${endWeeks}"
        this.remark.text = remark

        this.setView(view)

        exit.setOnClickListener {
            dismiss()
        }

        if (course.type == 1) {
            setEditAble()
        }


    }


    private fun setEditAble() {
        //ui
        delete.background = context.getDrawable(R.drawable.red_stroke)
        delete.setTextColor(context.getColor(R.color.ap_red))
        val drawable = context.getDrawable(R.drawable.dustbin_red_layer_list)
        drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        delete.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        edit.setImageResource(R.mipmap.edit_blue)

        //logic
        delete.setOnClickListener {
            (adapter.courseList as ArrayList<Course>).remove(course)
            adapter.notifyDataSetChanged()
            dismiss()
        }

        edit.setOnClickListener {
            "edit".toast()
        }
    }
}

