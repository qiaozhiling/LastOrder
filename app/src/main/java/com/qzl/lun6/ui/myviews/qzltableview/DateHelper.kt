package com.qzl.lun6.ui.myviews.qzltableview

import java.util.*

fun getCalendar(
    year: Int = 1970, month: Int = 0, date: Int = 1, hourOfDay: Int = 0, minute: Int = 0,
    second: Int = 0
): Calendar = Calendar.getInstance().apply {
    set(year, month, date, hourOfDay, minute, second)
}

fun Calendar.copy(): Calendar {
    val year = this.get(Calendar.YEAR)
    val month = this.get(Calendar.MONTH)
    val date = this.get(Calendar.DATE)
    val hourOfDay = this.get(Calendar.HOUR_OF_DAY)
    val minute = this.get(Calendar.MINUTE)
    val second = this.get(Calendar.SECOND)
    return getCalendar(year, month, date, hourOfDay, minute, second)
}

fun Calendar.mAdd(field: Int, amount: Int): Calendar {
    return this.copy().apply { add(field, amount) }
}