package com.qzl.lun6.logic

import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qzl.lun6.logic.data.SpfUtil
import com.qzl.lun6.logic.data.database.AppDatabase
import com.qzl.lun6.logic.model.MyData
import com.qzl.lun6.logic.model.course.Course
import com.qzl.lun6.logic.network.AnalysisUtils
import com.qzl.lun6.logic.network.NetUtils
import com.qzl.lun6.utils.exception.NetException
import com.qzl.lun6.utils.getCalendar
import com.qzl.lun6.utils.log
import com.qzl.lun6.utils.mAdd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Cookie
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


object Repository {

    ///网络获取
    fun requestVerifyCode() = liveData {
        val result = try {
            val pic = NetUtils.getVerifyCode().byteStream()
            Result.success(BitmapFactory.decodeStream(pic))
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException -> Result.failure(NetException("请检查网络"))
                is SocketTimeoutException -> Result.failure(NetException("教务处连接超时"))
                else -> Result.failure(e)
            }
        }
        emit(result)
    }


///查询课表需要三个其他参数 是课表页面的隐藏数据
//__VIEWSTATE
//__EVENTVALIDATION
//__VIEWSTATEGENERATOR

    private var courseParameter: Map<String, String>? = null

    fun requestData(yearCode: String) = liveData<Result<MyData>> {
        //yearCode 202001 202002
        val result = try {
            //请求选课
            /*val oP = coursyearCodeLiveDataeParameter
            val formMap = mutableMapOf<String, String>().apply {
                //表单参数
                put("ctl00\$ContentPlaceHolder1\$DDL_xnxq", year)
                put("ctl00\$ContentPlaceHolder1\$BT_submit", "确定")
                putAll(oP)
            }*/
            // TODO: 2021/6/29 formMap
            val courseHtml = NetUtils.getCourseList(mapOf())

            val courses = if (courseHtml.contains("无当前登录用户，请重新登录")) {
                throw IOException("登入信息过期")
            } else {
                AnalysisUtils.getCourseFromHtml(courseHtml)
            }

            val currentTerm = AnalysisUtils.getCurrentTerm(courseHtml)

            //请求校历
            val dateHtml = NetUtils.getSchoolCalendar()
            val dates = AnalysisUtils.getCalendarFromHtml(dateHtml, currentTerm)
            Result.success(MyData(courses as ArrayList<Course>, dates as ArrayList<Calendar>))

        } catch (e: Exception) {
            Result.failure(e)
        }

        emit(result)
    }

/* @Synchronized
 fun requestCourse(yearCode: String) = liveData {

     "发起课程请求".log()
     val result = try {
         /*val oP = coursyearCodeLiveDataeParameter
         val formMap = mutableMapOf<String, String>().apply {
             //表单参数
             put("ctl00\$ContentPlaceHolder1\$DDL_xnxq", year)
             put("ctl00\$ContentPlaceHolder1\$BT_submit", "确定")
             putAll(oP)
         }*/
         // TODO: 2021/6/29 formMap
         val html = NetUtils.getCourseList(mapOf())

         if (html.contains("无当前登录用户，请重新登录")) {
             throw IOException("登入信息过期")
         } else {
             val courses = AnalysisUtils.getCourseFromHtml(html)

             Result.success(courses)
         }

     } catch (e: Exception) {
         Result.failure(e)
     }
     emit(result)
 }


 fun requestCalendar(yearCode: String): LiveData<Result<List<Calendar>>> = liveData {
     "发起校历请求".log()
     val result = try {
         // TODO: 2021/6/29
         val html = NetUtils.getSchoolCalendar()
         val dates = AnalysisUtils.getCalendarFromHtml(html,)
         Result.success(dates)
     } catch (e: Exception) {
         Result.failure(e)
     }

     emit(result)
 }*/


    /////////////////////////////////////////本地获取
//登入状态
    var isLogin: Boolean
        get() = SpfUtil.spf.getBoolean(SpfUtil.IS_LOGIN, false)
        set(value) {
            SpfUtil.putValue(SpfUtil.IS_LOGIN, value)
        }

    //名字
    var userName: String
        get() = SpfUtil.spf.getString(SpfUtil.USER_NAME, "")!!
        set(value) {
            SpfUtil.putValue(SpfUtil.USER_NAME, value)
        }

    //学号
    var userNumber: String
        get() = SpfUtil.spf.getString(SpfUtil.USER_NUMBER, "")!!
        set(value) {
            SpfUtil.putValue(SpfUtil.USER_NUMBER, value)
        }

    ///真id
    var id: String
        get() = SpfUtil.spf.getString(SpfUtil.ID, "")!!
        set(value) {
            SpfUtil.putValue(SpfUtil.ID, value)
        }


    /**
     * 只储存学期第一天 推出22周
     */
    suspend fun loadCalendar(): List<Calendar> = withContext(Dispatchers.IO) {
        try {
            //e.g 2020-1-3
            val or = SpfUtil.spf.getString(SpfUtil.SCHOOL_CALENDAR, "1970-1-1")!!.split("-")
            val year = or[0].toInt()
            val month = or[1].toInt()
            val date = or[2].toInt()
            val first = getCalendar(year, month, date)

            val dates = mutableListOf<Calendar>()
            dates.add(first)
            val weekCount = 22

            for (i in 1 until weekCount) {
                val d1 = first.mAdd(Calendar.DATE, 7 * i)
                dates.add(d1)
            }

            dates

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

    }

    fun saveCalendar(calendars: List<Calendar>) {
        val year = calendars[0].get(Calendar.YEAR)
        val month = calendars[0].get(Calendar.MONTH)
        val date = calendars[0].get(Calendar.DATE)
        SpfUtil.putValue(SpfUtil.SCHOOL_CALENDAR, "$year-$month-$date")
    }

    fun saveCookie(map: Map<String, List<Cookie>>) {
        try {
            /*val jsonArray = JsonArray()

            val keys = map.keys
            for (key in keys) {
                val sb = StringBuffer()
                sb.append(key)
                for (cookie in map[key]!!) {
                    sb.append("-MyCookieSet-$cookie")
                }
                jsonArray.add(sb.toString())
            }*/

            val json = Gson().toJson(map)

            SpfUtil.putValue(SpfUtil.COOKIE, json.toString())


        } catch (e: java.lang.Exception) {
            e.printStackTrace()

        }
    }

    fun loadCookie(): HashMap<String, MutableList<Cookie>> {
        val json = SpfUtil.spf.getString(SpfUtil.COOKIE, "")!!
        return if (json != "") {
            val a = Gson().fromJson<HashMap<String, MutableList<Cookie>>>(
                json,
                object : TypeToken<HashMap<String, MutableList<Cookie>>>() {}.type
            )
            a

        } else {
            HashMap()
        }
    }

/* fun <V> getHashMapData(clsV: Class<V>?): HashMap<String, V> {
     val sp: SharedPreferences =
         MyApplication.context.getSharedPreferences("SIGN", Context.MODE_PRIVATE)
     val json = sp.getString("recognizeUserMap", "")
     val map: HashMap<String, V> = HashMap()
     val gson = Gson()
     val jsonParser = JsonParser()
     val obj = jsonParser.parse(json).asJsonObject
     map["recognizeUserMap"] = gson.fromJson(obj, clsV)
     Log.e(TAG, "getHashMapData-------------------$obj")
     return map
 }*/

    //数据库操作
    private val courseDao by lazy { AppDatabase.getDatabase(MyApplication.context).getCourseDao() }

    suspend fun loadCourse(): List<Course> = withContext(Dispatchers.IO) {
        try {
            // TODO: 2021/7/6
            val c = courseDao.loadAllCourse()
            c
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun deleteAllCourse() {
        try {
            courseDao.deleteAllCourse()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveCourse(courses: List<Course>) {
        try {
            courses.forEach {
                val a = courseDao.insertCourse(it)
                a
            }
            "存课成功".log()
        } catch (e: Exception) {
            "存课失败".log()
            e.printStackTrace()
        }


    }


}