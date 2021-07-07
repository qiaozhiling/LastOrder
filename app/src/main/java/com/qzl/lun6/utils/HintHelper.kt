package com.qzl.lun6.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.qzl.lun6.logic.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun String.log() {
    Log.i("LOG_UTIL", this)
}

fun String.toast() {
    Toast.makeText(MyApplication.context, this, Toast.LENGTH_SHORT).show()
}

suspend fun String.toastInScope() {
    withContext(Dispatchers.Main) {
        Toast.makeText(MyApplication.context, this@toastInScope, Toast.LENGTH_SHORT).show()
    }
}