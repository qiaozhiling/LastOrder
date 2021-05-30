package com.qzl.lun6.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.qzl.lun6.R
import java.lang.Exception

class LoginEditGroup(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {

    private val type: TextView
    private val user: EditText
    private val paswd: EditText
    private val code: EditText
    private val codeImageView: ImageView

    //登入类型 true本科生 false研究生
    var typeFlag = true

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.login_edit_group, this, true)

        type = view.findViewById(R.id.tv1_login_group)
        user = view.findViewById(R.id.edit_user_login_group)
        paswd = view.findViewById(R.id.edit_paswd_login_group)
        code = view.findViewById(R.id.edit_code_login_group)
        codeImageView = view.findViewById(R.id.iv_code_group)
    }

    fun setCodImage(image: Bitmap) {
        codeImageView.setImageBitmap(image)
    }

    fun changeType() {

        typeFlag = !typeFlag

        if (typeFlag) {
            type.text = "本科生登入"
            code.visibility = View.VISIBLE
            codeImageView.visibility = View.VISIBLE

        } else {
            type.text = "研究生登入"
            code.visibility = View.GONE
            codeImageView.visibility = View.GONE

        }
    }

    /**
     * @throws Exception 登入数据错误
     */
    fun getEditData(): Map<String, String> {
        val dUser = user.text.toString()
        val dPaswd = paswd.text.toString()
        val dCode = code.text.toString()

        when {
            dUser == "" -> {
                throw Exception("学号为空")
            }
            dPaswd == "" -> {
                throw Exception("密码为空")
            }
            dCode == "" -> {
                throw Exception("验证码为空")
            }
            else -> {
                return mapOf(
                    "user" to dUser,
                    "paswd" to dPaswd,
                    "code" to dCode
                )
            }
        }
    }


}