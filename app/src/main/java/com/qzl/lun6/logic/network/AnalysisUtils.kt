package com.qzl.lun6.logic.network

import com.qzl.lun6.logic.Repository
import com.qzl.lun6.logic.model.course.Course
import com.qzl.lun6.logic.model.course.Exam
import com.qzl.lun6.logic.model.course.TP
import com.qzl.lun6.logic.model.course.Transfer
import com.qzl.lun6.utils.mAdd
import org.jsoup.Jsoup
import retrofit2.Response
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList

object AnalysisUtils {

    /**
     * 保存目标id
     */
    fun saveIDFromResponse(response: Response<String>) {
        val responseInfo = response.raw().toString()
        val id = getBetween(responseInfo, "id=", "&hosturl")
        Repository.id = id
    }

    /**
     * 提取token
     * var token = "09f7528e-4e79-4993-85e6-2916a9b0c5f3";
     */
    fun getTokenFromHtml(str: String): String =
        getBetween(str, "var[ ]token[ ]=[ ]\"", "\"")

    /**
     *  提取Query参数
     *  id: String, num: String, ssourl: String, hosturl: String
     */
    fun getQueryInfoFromHtml(str: String): Map<String, String> {
        return hashMapOf(
            "id" to getBetween(str, "id=", "&num"),//此处id为临时id
            "num" to getBetween(str, "num=", "&ssourl"),
            "ssourl" to "https://jwcjwxt2.fzu.edu.cn",
            "hosturl" to "https://jwcjwxt2.fzu.edu.cn:81"
        )
    }

    // 从选课html中获得该学期
    fun getCurrentTerm(html: String): String {
        val dom = Jsoup.parse(html)
        val options = dom.getElementsByTag("option")
        //202101 202001 202001
        return try {
            options.filter {
                it.attr("selected") == "selected"
            }[0].`val`()
        } catch (e: IndexOutOfBoundsException) {
            options[0].`val`()
        }

    }

    /**
     * 返回A B串中间内容
     */
    private fun getBetween(str: String, left: String, right: String): String {
        val strs: MutableList<String> = ArrayList()
        val pID: Pattern = Pattern.compile("(?<=$left).*?(?=$right)")
        val m: Matcher = pID.matcher(str)
        while (m.find()) {
            strs.add(m.group())
        }
        return strs[0].replace(left, "").replace(right, "")
    }

    fun getCourseFromHtml(html: String): List<Course> {
        //解析课程
        val dom = Jsoup.parse(html)
        val table = dom.getElementById("ContentPlaceHolder1_DataList_xxk")
        val trs = table.getElementsByTag("tr").filter {
            it.attr("style") == "height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;"
        }

        val courses = mutableListOf<Course>()
        for (tr in trs) {
            val td = tr.getElementsByTag("td")

            val courseName = td[1].text()
            val teacherName = td[7].text()

            val courseTimePlace = td[8].textNodes().dropLast(1).map {
                it.text().let { origin ->
                    val or = origin.split(" ")
                    val startWeek = String(charArrayOf(or[0][0], or[0][1])).toInt()
                    val endWeek = String(charArrayOf(or[0][3], or[0][4])).toInt()
                    val week = String(charArrayOf(or[1][2])).toInt()
                    val sDW = when {
                        origin.contains("节(单)") -> 1
                        origin.contains("节(双)") -> 0
                        else -> 2
                    }
                    val startSection = getBetween(or[1], ":", "-").toInt()
                    val endSection = getBetween(or[1], "-", "节").toInt()
                    val place = or[2]
                    TP(startWeek, endWeek, week, sDW, startSection, endSection, place)
                }
            }

            val exam = Exam(td[9].text())

            val remark = td[10].text()

            val transferInfo = Transfer(td[11].text())

            val c = Course(courseName, teacherName, courseTimePlace, exam, transferInfo, remark)

            courses.add(c)

        }

        return courses
    }

    /**
     * 从html的结果
     * 返回用户可选学期列表
     */
    fun getUserTernsFromHtm(html: String) {
        val list = mutableListOf<String>().apply {
            val dom = Jsoup.parse(html)
            val elements = dom.select("option")
            addAll(elements.map {
                it.text()
            })
        }

        NetUtils.setUserTerms(list)
    }


    /**
     * 从html结果
     * 获得校历可选学期列表
     */
    fun getSchoolScheduleFromHtm(html: String) {
        val dom = Jsoup.parse(html)

    }

    /**
     * 从html结果
     * 获得学期校历
     */
    fun getCalendarFromHtml(html: String, yearCode: String): List<Calendar> {
        val dom = Jsoup.parse(html)
        //yearCode 202001 202002
        val options = dom.getElementsByTag("option")

        val tern =  //201802 20190225 20190705
            try {
                options.filter {
                    it.text() == yearCode
                }[0].`val`()
            } catch (e: IndexOutOfBoundsException) {
                options[0].`val`()
            }

        val year = tern.substring(6, 10).toInt()
        val month = tern.substring(10, 12).toInt() - 1
        val date = tern.substring(12, 14).toInt()

        val d = com.qzl.lun6.utils.getCalendar(year, month, date)
        val dates = mutableListOf<Calendar>()
        dates.add(d)
        val weekCount = 22/*dom.getElementsByTag("tr").filter {
            it.text().startsWith("第")
        }.size*/

        for (i in 1 until weekCount) {
            val d1 = d.mAdd(Calendar.DATE, 7 * i)
            dates.add(d1)
        }

        return dates
    }

    /**
     * 从get结果获得课表查询参数
     * __EVENTVALIDATION
     * __VIEWSTATEGENERATOR
     * __VIEWSTATE
     */
    fun getParasFromHtml(html: String): Map<String, String> {

        val map = mutableMapOf<String, String>()

        val dom = Jsoup.parse(html)
        val elements = dom.getElementsByTag("input")

        for (e in elements) {
            when (e.id()) {
                "__VIEWSTATE" -> map["__VIEWSTATE"] = e.`val`()
                "__VIEWSTATEGENERATOR" -> map["__VIEWSTATEGENERATOR"] = e.`val`()
                "__EVENTVALIDATION" -> map["__EVENTVALIDATION"] = e.`val`()
            }
        }

       return map
    }
}