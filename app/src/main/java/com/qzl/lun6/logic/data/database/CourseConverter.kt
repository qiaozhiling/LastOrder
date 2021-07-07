package com.qzl.lun6.logic.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qzl.lun6.logic.model.course.Exam
import com.qzl.lun6.logic.model.course.TP
import com.qzl.lun6.logic.model.course.Transfer


class CourseConverter {

    @TypeConverter
    fun stringToExam(value: String): Exam = Exam(value)


    @TypeConverter
    fun examToString(date: Exam): String = date.origin

    @TypeConverter
    fun stringToTransfer(value: String): Transfer = Transfer(value)


    @TypeConverter
    fun transferToString(date: Transfer): String = date.origin


    @TypeConverter
    fun tpsToString(date: List<TP>): String = Gson().toJson(date)


    // TODO: 2021/7/1
    @TypeConverter
    fun stringToTPs(value: String): List<TP> =
        Gson().fromJson(value, object : TypeToken<List<TP>?>() {}.type)

}