package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassCustomerList
import de.hdodenhof.circleimageview.CircleImageView

class SalseP_OrderListAdapter(private val OrderListData:List<ModelClassCustomerList>, var Listdata: Context?
): RecyclerView.Adapter<SalseP_OrderListAdapter.ViewHolder>()
{
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var customername: TextView = itemView.findViewById(R.id.customername)
        var imageFarword: CircleImageView = itemView.findViewById(R.id.imageFarword)
        var orderType: TextView = itemView.findViewById(R.id.orderType)
        var itemsAndAmount: TextView = itemView.findViewById(R.id.itemsAndAmount)
        var orderNoAndDate: TextView = itemView.findViewById(R.id.orderNoAndDate)
        var colorSide: TextView = itemView.findViewById(R.id.colorSide)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.salseperson_orderlist_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
  return OrderListData.size
    }
}