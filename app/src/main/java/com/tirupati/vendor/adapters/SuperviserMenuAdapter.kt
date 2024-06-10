package com.tirupati.vendor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tirupati.vendor.R
import com.tirupati.vendor.helper.interfaces.OnItemClickListSupervisor
import com.tirupati.vendor.model.GateRESPONSEDATA


class SuperviserMenuAdapter(
        val context: Context,
        val list: ArrayList<GateRESPONSEDATA>?,
        val onItemClick: OnItemClickListSupervisor
    ) :
        RecyclerView.Adapter<SuperviserMenuAdapter.ViewHolder>()
    {
        var selectedPosition = -1

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.supervisir_menu, p0, false)
            )
        }

        override fun getItemCount(): Int {
            return list!!.size
        }

        override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            var click = false
            p0.companyName.setText(list!!.get(p1).NAME)
            p0.vehicleNumber.setText(list!!.get(p1).VEHICLE_NO)
            p0.gatenumber.setText(list!!.get(p1).GE_NO)
            p0.grossweight.setText(list!!.get(p1).GROSS_WEIGHT)
            p0.tareweight.setText(list!!.get(p1).TARE_WEIGHT)
            p0.netWeight.setText(list!!.get(p1).NET_WEIGHT)






            p0.llItemSV.setOnClickListener {
                onItemClick.onItemClicked(p1, list[p1])
            }


        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val companyName = itemView.findViewById<TextView>(R.id.compnyname )
            val vehicleNumber = itemView.findViewById<TextView>(R.id.vehicleNumber )
            val gatenumber = itemView.findViewById<TextView>(R.id.gatenumber )
            val grossweight = itemView.findViewById<TextView>(R.id.grossweight )
            val netWeight = itemView.findViewById<TextView>(R.id.netweight )
            val tareweight = itemView.findViewById<TextView>(R.id.tareweight )
            val llItemSV = itemView.findViewById<LinearLayout>(R.id.llItemSuperVisor)

        }

    }

