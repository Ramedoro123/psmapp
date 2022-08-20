package com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.AdapterClass

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.ModelClass.ModelClassOrderLIst
import com.example.whm.ui.OrderDetailsList.OrderDetailsActicity

class Add_OnPackedAdapter (private val OderList: List<ModelClassOrderLIst>, var activity: Context?) :
    RecyclerView.Adapter<Add_OnPackedAdapter.ViewHolder>() {

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val OrderList = OderList[position]
        holder.OrderAutoid=OrderList.getAId()
        holder.OrderNo.text = OrderList.getOrderNo()
        holder.Customer.text = OrderList.getCustomer()
        holder.salesPerson.text = OrderList.getsalesPerson()
        holder.ShippingType.text = OrderList.getShippingType()
        holder.OrderDate.text = OrderList.getOrderDate()
        holder.Status.text = OrderList.getStatus()
        holder.StausView.setTextColor(Color.parseColor(OrderList.getColorCode()))
        holder.TotalItemss.text = OrderList.getTotalNoOfItem().toString()
        var PrintURL=OrderList.getPrintURL()
        holder.StatusAutoId=OrderList.getStatusAutoId()
        // holder.View.setBackgroundColor(Color.parseColor(OrderList.getColorCode()))
        holder.Start.setText("Print");
        holder.Start.visibility=View.VISIBLE
        holder.Start.setOnClickListener(View.OnClickListener {

            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse(PrintURL)
            activity?.startActivity(openURL)
        })

        holder.ViewButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, OrderDetailsActicity::class.java)
            activity?.startActivity(intent)
            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val sharedLoadOrderPage=sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putString("OrderAutoid",holder.OrderAutoid.toString())
            sharedLoadOrderPage.putString("OrderNo",holder.OrderNo.text.toString())
            sharedLoadOrderPage.putString("StatusAutoId",holder.StatusAutoId.toString())
            sharedLoadOrderPage.apply()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        // inflates the card_view_design view
        // that is used to hold list item
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.orderlist_view, parent, false)

        return ViewHolder(view)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        if(OderList.size!=0){
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Add-On Packed("+OderList.size+")"
        }else{
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Add-On Packed("+OderList.size+")"
        }
        return OderList.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var OrderNo: TextView = ItemView.findViewById(R.id.OrderNo)
        var Customer: TextView = ItemView.findViewById(R.id.Customer)
        var salesPerson: TextView = ItemView.findViewById(R.id.SalesPerson)
        var ShippingType: TextView = ItemView.findViewById(R.id.ShippingTypeView)
        var Status: TextView = ItemView.findViewById(R.id.StatusView)
        var OrderDate: TextView = ItemView.findViewById(R.id.OrderDate)
        var StausView: TextView = ItemView.findViewById(R.id.StatusView)
        var TotalItemss: TextView =ItemView.findViewById(R.id.TotalItemOrderViewPecker)
        var Start: TextView = ItemView.findViewById(R.id.btnStart)
        var ViewButton: TextView = ItemView.findViewById(R.id.btnView1)
        var OrderAutoid:Int?=null
        var StatusAutoId: Int? = null
    }


    // create new views

}
