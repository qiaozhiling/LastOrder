package com.qzl.lun6.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun String.log() {
    Log.i("LOG_UTIL", this)
}

fun String.toast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

suspend fun String.toastInScope(context: Context) {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, this@toastInScope, Toast.LENGTH_SHORT).show()
    }
}