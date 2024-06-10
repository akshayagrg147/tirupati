package com.tirupati.vendor.adapters
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import android.widget.TextView
import com.tirupati.vendor.model.ResponseDataPo
import java.util.ArrayList



class StateAdapter(context: Context,
                   @LayoutRes private val layoutResource: Int,
                   private val values: ArrayList<ResponseDataPo>)
    : ArrayAdapter<ResponseDataPo>(context, layoutResource, values) {

    override fun getItem(position: Int): ResponseDataPo = values[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): TextView {
        val view = createViewFromResource(convertView, parent, layoutResource)
        return bindData(getItem(position), view as TextView)
    }


    private fun createViewFromResource(convertView: View?, parent: ViewGroup, layoutResource: Int): View {
        val context = parent.context
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)
        return view
    }

    private fun bindData(value: ResponseDataPo, view: TextView): TextView {
        view.text = value.NAME
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        /*if (position==0){
           label.setTextColor(getColor(context, R.color.grey))
            label.setPadding(
                20,
                20,
                0,
                20
            )
        }
        else{*/
        label.setTextColor(Color.BLACK)
        label.setText(getItem(position).NAME)
        label.setPadding(
            20,
            30,
            0,
            20
        )
//    }
        return label
    }
}
