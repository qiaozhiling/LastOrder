package com.qzl.lun6.utils


import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.qzl.lun6.ui.activity.BaseActivity
import com.qzl.lun6.ui.fragment.BaseFragment

fun BaseActivity<*>.setDarkStatusBarTextColor() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val insetsController = window.insetsController
        insetsController!!.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        // API30以下
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

fun BaseFragment<*>.setStatusBarColor(colorId: Int) {
    (context as Activity).window.statusBarColor =
        ContextCompat.getColor(context as Activity, colorId)

}
