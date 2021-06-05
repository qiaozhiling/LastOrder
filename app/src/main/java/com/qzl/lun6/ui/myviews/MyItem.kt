package com.qzl.lun6.ui.myviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.qzl.lun6.R


/**
 * 我的界面的选项item
 */
class MyItem(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    private val iconIV: ImageView
    private val itemTV: TextView

    init {

        val view =
            LayoutInflater.from(context).inflate(R.layout.myitem_layout, this, true)

        iconIV = view.findViewById(R.id.iv_icon_myitem)
        itemTV = findViewById(R.id.tv_text_myitem)

        context.obtainStyledAttributes(attrs, R.styleable.MyItem).apply {

            getDrawable(R.styleable.MyItem_iconPic)?.let {
                iconIV.setImageDrawable(it)
            }

            getString(R.styleable.MyItem_infoText).let {
                itemTV.text = it

            }

            recycle()
        }

    }
}