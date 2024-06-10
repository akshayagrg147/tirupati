package com.tirupati.vendor.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.tirupati.vendor.R
import com.tirupati.vendor.model.ResponseDataItem


class PaymentTermsAdapter(val context: Context, var dataSource: List<ResponseDataItem?>?) : BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val label = TextView(context)
        label.setPadding(40, 0, 30, 0)
        label.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_spinner_arrow_new,
            0
        )
        label.setTextColor(context.resources.getColor(R.color.gun_metal))
        label.textSize = 16.0f
//        val typeface = ResourcesCompat.getFont(context, R.font.avenirltstd_medium)
//        label.setTypeface(typeface)
        label.setText(dataSource?.get(position)?.NAME)
        return label

    }

    override fun getItem(position: Int): Any? {
        return dataSource?.get(position);
    }

    override fun getCount(): Int {
        return dataSource!!.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomView(position, convertView, parent)!!
    }
    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val textView = TextView(context)
        textView.setPadding(20, 20, 20, 20)
        textView.setTextColor(context.resources.getColor(R.color.gun_metal))
        textView.setText(dataSource?.get(position)?.NAME)
        return textView
    }



}
