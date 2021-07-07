package com.qzl.lun6.logic.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.SharedPreferencesCompat
import com.qzl.lun6.logic.MyApplication
import com.qzl.lun6.utils.getCalendar
import com.qzl.lun6.utils.mAdd
import java.util.*

object SpfUtil {
    //todo 放Repository里

    private val FILE_NAME = "shared_data"
    val IS_LOGIN = "is_login"//已登录
    val USER_NAME = "user_name"//用姓名
    val USER_NUMBER = "user_number"//用学号
    val SCHOOL_CALENDAR = "school_calendar"//本学期校历
    val COOKIE = "cookie"//cookie
    val ID = "user_id"//ID

    val spf: SharedPreferences =
        MyApplication.context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = spf.edit()

    fun putValue(key: String, value: Any) {
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