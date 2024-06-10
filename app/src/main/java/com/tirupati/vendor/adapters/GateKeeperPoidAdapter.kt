package com.tirupati.vendor.adapters
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import android.widget.TextView
import com.tirupati.vendor.model.POIDRESPONSEDATA
import java.util.ArrayList


class GateKeeperPoidAdapter(context: Context,
                            @LayoutRes private val layoutResource: Int,
                            private val values: ArrayList<POIDRESPONSEDATA>)
    : ArrayAdapter<POIDRESPONSEDATA>(context, layoutResource, values) {

    override fun getItem(position: Int): POIDRESPONSEDATA = values[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): TextView {
        val view = createViewFromResource(convertView, parent, layoutResource)
        return bindData(getItem(position), view as TextView)
    }


    private fun createViewFromResource(convertView: View?, parent: ViewGroup, layoutResource: Int): View {
        val context = parent.context
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)
        return view
    }

    private fun bindData(value: POIDRESPONSEDATA, view: TextView): TextView {
        view.text = value.PO_NO
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView

        label.setTextColor(Color.BLACK)
        label.setText(this.getItem(position).PO_NO)
        label.setPadding(
            20,
            30,
            0,
            20
        )
        return label
    }
}