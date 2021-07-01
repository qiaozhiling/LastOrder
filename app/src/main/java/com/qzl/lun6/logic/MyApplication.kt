package com.qzl.lun6.logic

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApplication : Application() {

    var name: String = ""//姓名
    var number: String = ""//学号
    var academy: String = ""//学院
    var term: String = ""//学期

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}