package com.qzl.lun6.ui.activity.loginactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.qzl.lun6.logic.Repository

class LoginViewModel : ViewModel() {

    private val verifyCode: MutableLiveData<String> =
        MutableLiveData<String>().apply { value = "" }

    val verifyCodeLiveData= Transformations.switchMap(verifyCode) {
        Repository.requestVerifyCode()
    }

    fun getNewVerifyCode() {
        verifyCode.value=verifyCode.value
    }

}