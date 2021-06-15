package com.qzl.lun6.logic.model.course

/**
 * TAP TIME AND PLACE
 *
 * 01-16 星期2:1-2节 铜盘A315
 * @param startWeek 起始周
 * @param endWeek   结束周
 * @param week 星期 1 2 3 4 5 6 7
 * @param  sDW Single and Double Week 1单0双周 2全周 4%2=0双周  5%2=1单周
 * @param startSection 开始节数
 * @param endSection   结束节数
 * @param place 地点
 */
class TP(
    val startWeek: Int,
    val endWeek: Int,
    val week: Int,
    val sDW: Int,
    val startSection: Int,
    val endSection: Int,
    val place: String?
)