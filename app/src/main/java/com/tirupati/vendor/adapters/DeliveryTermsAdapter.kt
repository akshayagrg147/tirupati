package com.tirupati.vendor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.tirupati.vendor.R
import com.tirupati.vendor.model.ResponseDataItem
import com.tirupati.vendor.model.UOMData

class DeliveryTermsAdapter(
        context: Context,
        dataSource: List<UOMData>
    ) : ArrayAdapter<UOMData>(context, R.layout.item_spinner_row, dataSource) {

        private val inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(R.layout.item_spinner_row, parent, false)
            val label: TextView = view.findViewById(android.R.id.text1)

            label.setPadding(40, 0, 30, 0)
            label.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_spinner_arrow_new,
                0
            )
            label.setTextColor(context.resources.getColor(R.color.gun_metal))
            label.textSize = 16.0f
            label.text = getItem(position)?.UOMCODE
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomView(position, convertView, parent)
        }

        private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(R.layout.item_spinner_row, parent, false)
            val textView: TextView = view.findViewById(android.R.id.text1)

            textView.setPadding(20, 20, 20, 20)
            textView.setTextColor(context.resources.getColor(R.color.gun_metal))
            textView.text = getItem(position)?.UOMCODE
            return view
        }
    }