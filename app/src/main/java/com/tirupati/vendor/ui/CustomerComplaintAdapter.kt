package com.tirupati.vendor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tirupati.vendor.R
import com.tirupati.vendor.model.Ticket

class CustomerComplaintAdapter(private val complaints: List<Ticket>) :
    RecyclerView.Adapter<CustomerComplaintAdapter.ComplaintViewHolder>() {

    // ViewHolder class to hold the layout for each item
    class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val status: TextView = itemView.findViewById(R.id.status)
        val date: TextView = itemView.findViewById(R.id.date)
        val docNo: TextView = itemView.findViewById(R.id.doc_no) // Renamed to camelCase
        val serialNumber: TextView = itemView.findViewById(R.id.serial_number) // Renamed to camelCase
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.complaintcardview, parent, false)
        return ComplaintViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        // Safely handling potential null values from the `Ticket` model
        val complaint = complaints[position]
        holder.status.text = complaint.STATUS ?: "N/A" // Default value if null
        holder.date.text = complaint.CREATE_DATE ?: "Unknown" // Default value if null
        holder.docNo.text = complaint.TICKET_NO ?: "No Ticket" // Default value if null
        holder.serialNumber.text = complaint.BATCH_NO ?: "No Batch" // Default value if null
    }

    override fun getItemCount(): Int = complaints.size
}
