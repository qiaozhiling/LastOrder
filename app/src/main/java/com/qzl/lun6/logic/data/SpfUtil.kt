package com.qzl.lun6.logic.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.SharedPreferencesCompat
import com.qzl.lun6.logic.MyApplication
import com.qzl.lun6.utils.getCalendar
import com.qzl.lun6.utils.mAdd
import java.util.*

object SpfUtil {
    //todo 放viewmodel里? 怎么调用
    private val FILE_NAME = "share_data"
    private val IS_LOGIN = "is_login"//已登录
    private val USER_NAME = "user_name"//用姓名
    private val USER_NUMBER = "user_number"//用学号
    private val SCHOOL_CALENDAR = "school_calendar"//本学期校历
    private val COOKIE = "cookie"//cookie

    private val spf: SharedPreferences =
        MyApplication.context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = spf.edit()

    var isLogin: Boolean 
        get() = spf.getBoolean(IS_LOGIN, false)
        set(value) {
            putValue(IS_LOGIN, value)
        }

    var userName: String
        get() = spf.getString(USER_NAME, "")!!
        set(value) {
            putValue(USER_NAME, value)
        }

    var userNumber: String
        get() = spf.getString(USER_NUMBER, "")!!
        set(value) {
            putValue(USER_NUMBER, value)
        }

    //只储存学期第一天
    var calendar: List<Calendar>
        get() {
            //e.g 2020-1-3
            val or = spf.getString(SCHOOL_CALENDAR, "1970-1-1")!!.split("-")
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

            return dates
        }
        set(value) {
            val year = value[0].get(Calendar.YEAR)
            val month = value[0].get(Calendar.MONTH)
            val date = value[0].get(Calendar.DATE)
            putValue(SCHOOL_CALENDAR, "$year-$month-$date")
        }

    private fun putValue(key: String, value: Any) {
        when (value) {
            is Int -> {
                editor.putInt(key, value.toInt())
            }
            is Long -> {
                editor.putLong(key, value.toLong())
            }
            is String -> {
                editor.putString(key, value.toString())
            }
            is Float -> {
                editor.putFloat(key, value.toFloat())
            }
            is Boolean -> {
                editor.putBoolean(key, value)
            }
            else -> throw Exception("spf 数据种类错误")
        }
        editor.apply()
    }

    fun clearAll() {
        editor.clear()
        editor.apply()
    }

}