package com.qzl.lun6.logic.data.database

import android.content.Context
import androidx.room.*
import com.qzl.lun6.logic.model.course.Course

@Database(version = 1, entities = [Course::class])
@TypeConverters(CourseConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getCourseDao(): CourseDao

    companion object {

        private var instance: AppDatabase? = null//缓存AppDatabase

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {

            instance?.let {
                //instance不为null返回instance
                return it
            }

            /*val appDatabase = Room.databaseBuilder(//构建AppDatabase实例
                context.applicationContext,
                AppDatabase::class.java,//AppDatabase的Class类型
                "user_Database"//数据库名
            ).build()
            return appDatabase*/

            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build().apply { instance = this }

        }
    }
}