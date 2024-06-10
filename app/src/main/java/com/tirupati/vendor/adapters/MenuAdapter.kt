package com.tirupati.vendor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.tirupati.vendor.R
import com.tirupati.vendor.helper.interfaces.OnItemClickListGateKeeper

import com.tirupati.vendor.model.VendorRESPONSEDATAX


    class MenuAdapter(
        val context: Context,
        val list: ArrayList<VendorRESPONSEDATAX>?,
        val onItemClick: OnItemClickListGateKeeper
    ) :
        RecyclerView.Adapter<MenuAdapter.ViewHolder>()
    {
        var selectedPosition = -1

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.gatekeeper_menu_drawer, p0, false)
            )
        }

        override fun getItemCount(): Int {
            return list!!.size
        }

        override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            var click = false
            p0.tv_category.setText(list!!.get(p1).NAME)
            p0.llItem.setOnClickListener {
                onItemClick.onItemClicked(p1, list[p1])
            }


        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tv_category = itemView.findViewById<TextView>(R.id.nameTitle )
            val llItem = itemView.findViewById<RelativeLayout>(R.id.llItem)

        }

    }

