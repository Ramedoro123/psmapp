package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.MainActivity2
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.CartListModleClass
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.SalesPersonProductModel
import com.example.whm.ui.Sales_Person.CartViewActicity
import com.squareup.picasso.Picasso
import org.json.JSONObject

class CartListAdapterClass(
    private val CartList: List<CartListModleClass>,
    var cartViewActicity: Context
) : RecyclerView.Adapter<CartListAdapterClass.ViewHolder>() {
    var removedPosition : Int ? = null
    var getCartListDetails: MutableList<CartListModleClass> = ArrayList()
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var cartPname=itemView.findViewById<TextView>(R.id.cartPname)
        val cartPid=itemView.findViewById<TextView>(R.id.cartPid)
        val cartPpiece=itemView.findViewById<TextView>(R.id.cartPpiece)
        val cartTotalAmount=itemView.findViewById<TextView>(R.id.cartTotalAmount)
        val ProductImageCart=itemView.findViewById<ImageView>(R.id.ProductImageCart)
        val btnDeleteCartList=itemView.findViewById<TextView>(R.id.btnDeleteCartList)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cartlist_viewdetails, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var cartListData=CartList[position]
        holder.cartPname.text=cartListData.getPName()
        holder.cartPid.text=cartListData.getPId().toString()
        var priceProduct = cartListData.getpUnitPrice()!!.toFloat()
        var UnitType = cartListData.getUnitType().toString()
        var ReqQty = cartListData.getReqQty().toString()
        var NetPrice = cartListData.getNetPrice()!!.toFloat()
        holder.cartTotalAmount.setText("$" + "%.2f".format(NetPrice))
        holder.cartPpiece.setText("$" + "%.2f".format(priceProduct)+"/" +UnitType+ "x" +ReqQty)
        Picasso.get().load(cartListData.getImgPath()).error(R.drawable.default_pic).into(holder.ProductImageCart);

        holder.btnDeleteCartList.setOnClickListener {
           deleteItem(index = position)
        }

    }
    fun deleteItem(index: Int){
        getCartListDetails.removeAt(index)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {

      return CartList.size
    }
}
