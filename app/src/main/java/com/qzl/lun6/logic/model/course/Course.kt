package com.qzl.lun6.logic.model.course

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.qzl.lun6.logic.data.database.CourseConverter

/**
 * @param courseName 课程名
 * @param teacherName 教师名
 * @param TPs 上课时间地点 可有多个
 * @param exam 考试信息
 * @param transferInfo 调课信息
 * @param remark 备注
 * @param type 0教务处课程 1用户自加课程
 */
@Entity
@TypeConverters(CourseConverter::class)
class Course(
    val courseName: String,
    val teacherName: String,
    val TPs: List<TP>,
    val exam: Exam,
    val transferInfo: Transfer,
    val remark: String,
    val type: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}