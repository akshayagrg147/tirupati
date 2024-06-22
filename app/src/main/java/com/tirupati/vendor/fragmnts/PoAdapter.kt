package com.tirupati.vendor.fragmnts



import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.tirupati.vendor.R

import com.tirupati.vendor.model.ResponseData


class PoAdapter(cntx: Context, private val itemList: List<ResponseData>) : RecyclerView.Adapter<PoAdapter.ViewHolder>() {
    private val context: Context = cntx
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusText: TextView = itemView.findViewById(R.id.status_text)
        val dateText: TextView = itemView.findViewById(R.id.date_text)
        val weightText: TextView = itemView.findViewById(R.id.weight_text)
        val priceText: TextView = itemView.findViewById(R.id.price_text)
        val totalText: TextView = itemView.findViewById(R.id.total_text)
        val poNumber: TextView = itemView.findViewById(R.id.po_vslue)
        val vendorName: TextView = itemView.findViewById(R.id.vendor_value)
        val itemName: TextView = itemView.findViewById(R.id.item_value)
        val statusCircle: View = itemView.findViewById(R.id.status_circle)
        val actionButton: TextView = itemView.findViewById(R.id.action_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_approval, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.statusText.text = item.STATUS
        holder.dateText.text = item.PO_DT
        holder.weightText.text = item.PO_QTY
        holder.priceText.text = item.RATEP_UOM
        holder.totalText.text = item.NET_TOTAL
        holder.poNumber.text = "${item.PO_NO}"
        holder.vendorName.text = "${item.VENDOR_NAME}"
        holder.itemName.text = "${item.ITEM_NAME}"
        holder.itemView.setOnClickListener{
            when(item.STATUS){
                "Approved"->{
                    val bundle = Bundle()
                    bundle.putParcelable("myObjectKey", item);
                    Navigation.findNavController(holder.itemView).navigate(R.id.currentt_to_dispatchFragment,bundle);
                }
                "Counter"->{
                    val bundle = Bundle()
                    bundle.putString("id",item.POID)
                    Navigation.findNavController(holder.itemView).navigate(R.id.polistfragmentToCounterFragment,bundle);
                }
                else->{
                    val bundle = Bundle()
                    bundle.putParcelable("myObjectKey", item);
                    Navigation.findNavController(holder.itemView).navigate(R.id.currentt_to_purchaseOrderFragmentClick, bundle);
                }



            }


        }

        when (item.STATUS) {
            "Pending" -> {
                holder.statusCircle.setBackgroundResource(R.drawable.pending)
                holder.actionButton.visibility = View.GONE
                val color = ContextCompat.getColor(context, com.tirupati.vendor.R.color.blue_00A0FA)
                holder.statusText.setTextColor(color)
            }
            "Approved" -> {
                holder.statusCircle.setBackgroundResource(R.drawable.approved)
                holder.actionButton.visibility = View.VISIBLE
                holder.actionButton.text = "Dispatch"
                val color = ContextCompat.getColor(context, com.tirupati.vendor.R.color.green_18BE46)
                holder.statusText.setTextColor(color)
            }
            "Cancel" -> {
                holder.statusCircle.setBackgroundResource(R.drawable.approved)
                holder.actionButton.visibility = View.VISIBLE
                holder.actionButton.text = "Dispatch"
                val color = ContextCompat.getColor(context, com.tirupati.vendor.R.color.green_18BE46)
                holder.statusText.setTextColor(color)
            }
            "Dispatched" -> {
                holder.statusCircle.setBackgroundResource(R.drawable.dispatched)
                holder.actionButton.visibility = View.GONE
                val color = ContextCompat.getColor(context, com.tirupati.vendor.R.color.orange)
                holder.statusText.setTextColor(color)
            }
            "Rejected" -> {
                holder.statusCircle.setBackgroundResource(R.drawable.rejected)
                holder.actionButton.visibility = View.GONE
                val color = ContextCompat.getColor(context, com.tirupati.vendor.R.color.red_DA3A36)
                holder.statusText.setTextColor(color)
            }
            "Collector" -> {
                holder.statusCircle.setBackgroundResource(R.drawable.collecter)
                holder.actionButton.visibility = View.GONE
                val color = ContextCompat.getColor(context, com.tirupati.vendor.R.color.yellow_FFB800)
                holder.statusText.setTextColor(color)
            }
            "Counter" -> {
                holder.statusCircle.setBackgroundResource(R.drawable.collecter)
                holder.actionButton.visibility = View.GONE
                val color = ContextCompat.getColor(context, com.tirupati.vendor.R.color.yellow_FFB800)
                holder.statusText.setTextColor(color)
            }
        }
    }

    override fun getItemCount() = itemList.size
}
