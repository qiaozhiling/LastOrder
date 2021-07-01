package com.qzl.lun6.logic

import androidx.lifecycle.liveData
import com.qzl.lun6.logic.model.course.Course
import com.qzl.lun6.logic.network.AnalysisUtils
import com.qzl.lun6.logic.network.NetUtils
import java.util.*

object Repository {

    ///
    ///查询课表需要三个其他参数 是课表页面的隐藏数据
    //__VIEWSTATE
    //__EVENTVALIDATION
    //__VIEWSTATEGENERATOR

    private var courseParameter: Map<String, String>? = null

    fun getNewCourse(yearCode: String) = liveData {
        val result = try {
            /*val oP = courseParameter
            val formMap = mutableMapOf<String, String>().apply {
                //表单参数
                put("ctl00\$ContentPlaceHolder1\$DDL_xnxq", year)
                put("ctl00\$ContentPlaceHolder1\$BT_submit", "确定")
                putAll(oP)
            }*/
            // TODO: 2021/6/29 formMap
            val html = NetUtils.getCourseList(mapOf())
            val courses = AnalysisUtils.getCourseFromHtml(html)
            courses
        } catch (e: Exception) {
            throw e
        }

        emit(result)
    }

    fun getNewCalendar(yearCode: String) = liveData {
        val result = try {
            // TODO: 2021/6/29
            val html = NetUtils.getSchoolCalendar()
            val dates = AnalysisUtils.getCalendarFromHtml(html)
            dates
        } catch (e: Exception) {
            throw e
        }

        emit(result)

    }

}