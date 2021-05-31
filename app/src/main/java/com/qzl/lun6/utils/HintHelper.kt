package com.qzl.lun6.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

fun String.log() {
    Log.i("LOG_UTIL", this)
}

fun String.toast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}