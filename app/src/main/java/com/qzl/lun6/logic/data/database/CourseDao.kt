package com.qzl.lun6.logic.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.qzl.lun6.logic.model.course.Course

@Dao
interface CourseDao {
    //增
    @Insert
    suspend fun insertCourse(course: Course): Long//返回生成的主键

    //查全部
    @Query("select * from Course")//Course
    suspend fun loadAllCourse(): List<Course>//返回所有Course的列表

    //删全部
    @Query("delete from Course")
    suspend fun deleteAllCourse()

    //
    @Delete
    suspend fun deleteCourse(course: Course)
}