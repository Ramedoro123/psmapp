package com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.AdapterClass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.ModelClass.StockAvailableModel

class Stock_availableAdapter(private val Productavialable:List<StockAvailableModel>, var Stocklist: Context?):RecyclerView.Adapter<Stock_availableAdapter.ViewHolder>() {
    inner class ViewHolder(ItemView:View):RecyclerView.ViewHolder(ItemView){
        var ProductId: TextView = ItemView.findViewById(R.id.ProductId)
        var TextValue: TextView = ItemView.findViewById(R.id.TextValue)
        var PackedQty: TextView = ItemView.findViewById(R.id.PackedQty)
        var AvailableQty: TextView = ItemView.findViewById(R.id.AvailableQty)
        var RequiredQty: TextView = ItemView.findViewById(R.id.RequiredQty)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val view=LayoutInflater.from(parent.context).inflate(R.layout.stock_listview, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Productavialable1=Productavialable[position]
        holder.ProductId.text=Productavialable1.getProductId().toString()
        holder.TextValue.text=Productavialable1.getProductName().toString()
        holder.AvailableQty.text=Productavialable1.getAvilQty().toString()
        holder.RequiredQty.text=Productavialable1.getRequiredQty().toString()
        holder.PackedQty.text=Productavialable1.getQtyShip().toString()
    }

    override fun getItemCount(): Int {


       return Productavialable.size
    }
}