package com.qzl.lun6.ui.myviews.qzltableview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qzl.lun6.R
import java.util.*


class TableDateAdapter :
    RecyclerView.Adapter<TableDateAdapter.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val month: TextView = itemView.findViewById(R.id.month_date_item)
        val mon: TextView = itemView.findViewById(R.id.mon_date_item)
        val tue: TextView = itemView.findViewById(R.id.tue_date_item)
        val wed: TextView = itemView.findViewById(R.id.wed_date_item)
        val thur: TextView = itemView.findViewById(R.id.thur_date_item)
        val fri: TextView = itemView.findViewById(R.id.fri_date_item)
        val sat: TextView = itemView.findViewById(R.id.sat_date_item)
        val sun: TextView = itemView.findViewById(R.id.sun_date_item)
        //val week = listOf(mon, tue, wed, thur, fri, sat, sun)

    }


    // dates:List<这周的第一天>
    var dates: List<Calendar> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.table_date_item_layout, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.apply {

            val m = "${dates[position].get(Calendar.MONTH) + 1}\n月"
            month.text = m

            mon.text = (dates[position].get(Calendar.DATE)).toString()
            tue.text =
                (dates[position].mAdd(Calendar.DATE, 1).get(Calendar.DATE)).toString()
            wed.text =
                (dates[position].mAdd(Calendar.DATE, 2).get(Calendar.DATE)).toString()
            thur.text =
                (dates[position].mAdd(Calendar.DATE, 3).get(Calendar.DATE)).toString()
            fri.text =
                (dates[position].mAdd(Calendar.DATE, 4).get(Calendar.DATE)).toString()
            sat.text =
                (dates[position].mAdd(Calendar.DATE, 5).get(Calendar.DATE)).toString()
            sun.text =
                (dates[position].mAdd(Calendar.DATE, 6).get(Calendar.DATE)).toString()
        }


    }

    override fun getItemCount(): Int {
        return dates.size

    }
}