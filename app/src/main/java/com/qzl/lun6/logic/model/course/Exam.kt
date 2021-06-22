package com.qzl.lun6.logic.model.course

import com.qzl.lun6.utils.getCalendar
import java.util.*

/**
 *  examTimeAndPlace
 *  2021年05月09日 09:00-10:00 铜盘A508
 */
class Exam(
    val origin: String
) {

    private var tp: TP? = null

    fun getTP(firstDayOfTerm: Calendar): TP? {
        if (origin == "") {
            return null
        } else {
            if (tp == null) {

                //2021年05月09日 09:00-10:00 铜盘A508
                val or = origin.split(" ")

                val year = String(charArrayOf(or[0][0], or[0][1], or[0][2], or[0][3])).toInt()
                val month = String(charArrayOf(or[0][5], or[0][6])).toInt() - 1
                val date = String(charArrayOf(or[0][8], or[0][9])).toInt()

                val eD = getCalendar(year, month, date)

                val week = (eD.get(Calendar.DAY_OF_WEEK) - 1).let {
                    if (it <= 0) {
                        7
                    } else {
                        it
                    }
                }

                val startWeek =
                    eD.get(Calendar.WEEK_OF_YEAR) - firstDayOfTerm.get(Calendar.WEEK_OF_YEAR) + if (week == 7) 0 else 1

                val startHour = String(charArrayOf(or[1][0], or[1][1])).toInt()
                val startMinute = String(charArrayOf(or[1][3], or[1][4])).toInt()
                val start = startHour + startMinute * 0.01
                val startSection = when {
                    0 <= start && start < 9.05 -> 1
                    9.05 <= start && start < 10.00 -> 2
                    10.00 <= start && start < 11.05 -> 3
                    11.05 <= start && start < 12.00 -> 4
                    12.00 <= start && start < 14.45 -> 5
                    14.45 <= start && start < 15.40 -> 6
                    15.40 <= start && start < 16.35 -> 7
                    16.35 <= start && start < 17.30 -> 8
                    17.30 <= start && start < 19.45 -> 9
                    19.45 <= start && start < 20.40 -> 10
                    20.40 <= start && start < 24 -> 11
                    else -> throw Exception("startSection ERROR")
                }

                val endHour = String(charArrayOf(or[1][6], or[1][7])).toInt()
                val endMinute = String(charArrayOf(or[1][9], or[1][10])).toInt()
                val end = endHour + endMinute * 0.01
                val endSection = when {
                    0.0 < end && end <= 9.05 -> 1
                    9.05 < end && end <= 10.00 -> 2
                    10.00 < end && end <= 11.05 -> 3
                    11.05 < end && end <= 12.00 -> 4
                    12.00 < end && end <= 14.45 -> 5
                    14.45 < end && end <= 15.40 -> 6
                    15.40 < end && end <= 16.35 -> 7
                    16.35 < end && end <= 17.30 -> 8
                    17.30 < end && end <= 19.45 -> 9
                    19.45 < end && end <= 20.40 -> 10
                    20.40 < end && end <= 24 -> 11
                    else -> throw Exception("startSection ERROR")
                }

                val place = or[2]
                tp = TP(startWeek, startWeek, week, 2, startSection, endSection, place)
                return tp
            } else {
                return tp
            }
        }

    }
}