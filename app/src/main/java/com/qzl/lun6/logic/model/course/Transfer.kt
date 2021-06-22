package com.qzl.lun6.logic.model.course

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @param origin 原数据
 */
class Transfer(
    val origin: String
) {

   private var tp: TP? = null

    fun getTP(): TP? = if (origin == "") {
        null
    } else {
        if (tp == null) {
            val or = origin.split(" ")
            val startWeek = String(charArrayOf(or[3][0], or[3][1])).toInt()
            val endWeek = startWeek
            val week = String(charArrayOf(or[4][2])).toInt()
            val sDW = 2
            val startSection = getBetween(or[4], ":", "-").toInt()
            val endSection = getBetween(or[4], "-", "节").toInt()
            val place = or[5]
            tp = TP(startWeek, endWeek, week, sDW, startSection, endSection, place)
            tp
        }else{
            tp
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
}