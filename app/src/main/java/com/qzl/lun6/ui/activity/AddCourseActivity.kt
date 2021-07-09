package com.qzl.lun6.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.qzl.lun6.R
import com.qzl.lun6.databinding.ActivityAddCourseBinding
import com.qzl.lun6.utils.setDarkStatusBarTextColor
import com.qzl.lun6.utils.toast
import jsc.kit.wheel.base.WheelItem
import jsc.kit.wheel.dialog.ColumnWheelDialog

class AddCourseActivity : BaseActivity<ActivityAddCourseBinding>() {
    private var sDW = 2//1单0双周 2全周
    private var startSection = 1
    private var endSection = 1
    private var startWeek = 1
    private var endWeek = 1
    private var week = 1

    private val map = mapOf(
        1 to "星期一",
        2 to "星期二",
        3 to "星期三",
        4 to "星期四",
        5 to "星期五",
        6 to "星期六",
        7 to "星期日",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDarkStatusBarTextColor()

        binding.toolbarAddCourse.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.radioGroupAddCourse.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_all_weeks_add_course -> {
                    sDW = 2
                }
                R.id.radio_odd_weeks_add_course -> {
                    sDW = 1
                }
                R.id.radio_even_weeks_add_course -> {
                    sDW = 0
                }
            }
        }

        binding.tvWeekAddCourse.setOnClickListener {
            showWeekWheel()
        }

        binding.relativeLayoutSectionsAddCourse.setOnClickListener {
            showSectionWheel()
        }

        binding.relativeLayoutWeekAddCourse.setOnClickListener {
            showWeeksWheel()
        }

        binding.btmConfirmAddCourse.setOnClickListener {

            val courseName = binding.editCourseNameAddCourse.text.toString()
            val teacher = binding.editTeacherAddCourse.text.toString()
            val place = binding.editPlaceAddCourse.text.toString()
            val remark = binding.editRemarkAddCourse.text.toString()

            if (courseName != "" && place != "") {
                val intent = Intent()
                intent.apply {
                    putExtra("sDW", sDW)
                    putExtra("startSection", startSection)
                    putExtra("endSection", endSection)
                    putExtra("startWeek", startWeek)
                    putExtra("endWeek", endWeek)
                    putExtra("week", week)
                    putExtra("courseName", courseName)
                    putExtra("teacher", teacher)
                    putExtra("place", place)
                    putExtra("remark", remark)
                }
                setResult(RESULT_OK, intent)
                finish()
            } else {
                "请填写课程名称和上课地点".toast()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSectionWheel() {

        val dialog: ColumnWheelDialog<WheelItem, WheelItem, WheelItem, WheelItem, WheelItem> =
            ColumnWheelDialog(this)
        dialog.show()
        dialog.setTitle("选择上课节数")
        dialog.setCancelButton("取消", null)
        dialog.setOKButton("确定") { _, item0, _, item2, _, _ ->
            val start = item0!!.showText.replace(Regex("[第节]"), "").toInt()
            val end = item2!!.showText.replace(Regex("[第节]"), "").toInt().let {
                if (it < start) start else it
            }

            startSection = start
            endSection = end

            binding.tvSectionFromAddCourse.text = "第${startSection}节"
            binding.tvSectionToAddCourse.text = "第${endSection}节"

            false
        }

        dialog.setItems(
            initSectionItems(),
            Array(1) { WheelItem("至") },
            initSectionItems(),
            null,
            null
        )
    }

    private fun showWeeksWheel() {

        val dialog: ColumnWheelDialog<WheelItem, WheelItem, WheelItem, WheelItem, WheelItem> =
            ColumnWheelDialog(this)
        dialog.show()
        dialog.setTitle("选择上课周数")
        dialog.setCancelButton("取消", null)
        dialog.setOKButton("确定") { _, item0, _, item2, _, _ ->
            val start = item0!!.showText.replace(Regex("[第周]"), "").toInt()
            val end = item2!!.showText.replace(Regex("[第周]"), "").toInt().let {
                if (it < start) start else it
            }

            startWeek = start
            endWeek = end

            binding.tvWeekFromAddCourse.text = "第${startWeek}周"
            binding.tvWeekToAddCourse.text = "第${endWeek}周"

            false
        }

        dialog.setItems(
            initWeeksItems(),
            Array(1) { WheelItem("至") },
            initWeeksItems(),
            null,
            null
        )
    }

    private fun showWeekWheel() {

        val dialog: ColumnWheelDialog<WheelItem, WheelItem, WheelItem, WheelItem, WheelItem> =
            ColumnWheelDialog(this)
        dialog.show()
        dialog.setTitle("选择星期")
        dialog.setCancelButton("取消", null)
        dialog.setOKButton("确定") { _, item0, _, _, _, _ ->
            week = item0!!.showText.replace(Regex("[星期]"), "").toInt()

            binding.tvWeekAddCourse.text = map[week]

            false
        }

        dialog.setItems(
            initWeekItems(),
            null,
            null,
            null,
            null
        )
    }

    private fun initSectionItems(): Array<WheelItem?> {
        val items = arrayOfNulls<WheelItem>(11)
        for (i in 0..10) {
            items[i] = WheelItem("第${i + 1}节")
        }
        return items
    }

    private fun initWeeksItems(): Array<WheelItem?> {
        val items = arrayOfNulls<WheelItem>(22)
        for (i in 0..21) {
            items[i] = WheelItem("第${i + 1}周")
        }
        return items
    }

    private fun initWeekItems(): Array<WheelItem?> {
        val items = arrayOfNulls<WheelItem>(7)
        for (i in 0..6) {
            items[i] = WheelItem("星期${i + 1}")
        }
        return items
    }

}