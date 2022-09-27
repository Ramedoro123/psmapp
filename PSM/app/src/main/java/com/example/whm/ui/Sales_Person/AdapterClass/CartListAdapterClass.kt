package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.CartListModleClass

class CartListAdapterClass(
    private val CartList: List<CartListModleClass>,
    var cartContext: Context?
) : RecyclerView.Adapter<CartListAdapterClass.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cartPname=itemView.findViewById<TextView>(R.id.cartPname)
        val cartPid=itemView.findViewById<TextView>(R.id.cartPid)
        val cartPpiece=itemView.findViewById<TextView>(R.id.cartPpiece)
        val cartTotalAmount=itemView.findViewById<TextView>(R.id.cartTotalAmount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cartlist_viewdetails, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {

      return CartList.size
    }
}