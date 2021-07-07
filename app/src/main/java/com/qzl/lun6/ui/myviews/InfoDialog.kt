package com.qzl.lun6.ui.myviews

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.qzl.lun6.R
import com.qzl.lun6.logic.model.course.TP
import com.qzl.lun6.ui.myviews.qzltableview.TableMainAdapter


class InfoDialog(
    context: Context,
    tpT: TP,
    courseNameT: String,
    teacherNameT: String?,
    remarkT: String?,
    val style: Int = 3
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

        place.text = tpT.place
        courseName.text = courseNameT
        teacher.text = teacherNameT
        sections.text = "${tpT.startSection}-${tpT.endSection}"
        weeks.text = "${tpT.startWeeks}-${tpT.endWeeks}"
        remark.text = remarkT

        this.setView(view)

        exit.setOnClickListener {
            dismiss()
        }

        if (style == 1) {
            setEditAble()
        }


    }

    private val EXAM = 1
    private val TRANSFER = 2
    private val COURSE = 3

    private fun setEditAble() {
        delete.background = context.getDrawable(R.drawable.red_stroke)
        delete.setTextColor(context.getColor(R.color.ap_red))
        val drawable = context.getDrawable(R.drawable.dustbin_red_layer_list)
        drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        delete.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        edit.setImageResource(R.mipmap.edit_blue)
    }
}

