package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.whm.ui.Sales_Person.remarks
class RemarkAdapterClass(private val OrderItemListData: ArrayList<remarks>,var Listdata: Context?,
): RecyclerView.Adapter<RemarkAdapterClass.ViewHolder>() {
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var remarksLevel: TextView = itemView.findViewById(R.id.remarksLevel)
        var remarksvalue: TextView = itemView.findViewById(R.id.remarksvalue)
        var view17:View = itemView.findViewById(R.id.view17)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RemarkAdapterClass.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.remarks_list, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: RemarkAdapterClass.ViewHolder, position: Int) {
        var orderItemPosition=OrderItemListData[position]
        var employType=orderItemPosition.getempType().toString()
        var employremarks=orderItemPosition.getremarks().toString()
        var employName=orderItemPosition.getempName().toString()
        if (employType!=null&&employType!=""&&employremarks!=null&&employremarks!=""&&employName!=null&&employName!="")
        {
            holder.remarksLevel.visibility=View.VISIBLE
            holder.remarksvalue.visibility=View.VISIBLE
            holder.view17.visibility=View.VISIBLE
            holder.remarksLevel.setText(employType.toString() + " \n(" + employName.toString() +") :")
            holder.remarksvalue.setText(employremarks.toString())

        }
        else{
            holder.remarksLevel.visibility=View.GONE
            holder.remarksvalue.visibility=View.GONE
            holder.view17.visibility=View.GONE
        }
    }

    override fun getItemCount(): Int {
        return OrderItemListData.size
    }
}