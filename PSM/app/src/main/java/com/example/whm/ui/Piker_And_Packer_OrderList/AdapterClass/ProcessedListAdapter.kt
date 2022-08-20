package com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.AdapterClass

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.ModelClass.ModelClassOrderLIst
import com.example.myapplication.com.example.whm.ui.UpdateLocation.setCanceledOnTouchOutside
import com.example.whm.ui.OrderDetailsList.OrderDetailsActicity
import com.example.whm.ui.Piker_And_Packer_OrderList.StartPackingOrderList

class ProcessedListAdapter(
    private val OderList: List<ModelClassOrderLIst>, var activity: Context?) :
    RecyclerView.Adapter<ProcessedListAdapter.ViewHolder>() {

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var OrderNo: TextView = ItemView.findViewById(R.id.OrderNo)
        var Customer: TextView = ItemView.findViewById(R.id.Customer)
        var salesPerson: TextView = ItemView.findViewById(R.id.SalesPerson)
        var ShippingType: TextView = ItemView.findViewById(R.id.ShippingTypeView)
        var Status: TextView = ItemView.findViewById(R.id.StatusView)
        var OrderDate: TextView = ItemView.findViewById(R.id.OrderDate)
        var StausView: TextView = ItemView.findViewById(R.id.StatusView)
        var TotalItemss: TextView = ItemView.findViewById(R.id.TotalItemOrderViewPecker)
        var OrderAutoid: Int? = null
        var ResumeO: Int? = null
        var StatusAutoId: Int? = null

        //val btnProcessedOrder=mView.findViewById<Button>(com.example.myapplication.R.id.nav_Processed_order_list_Picker)
        var Start: TextView = ItemView.findViewById(R.id.btnStart)
        var ViewButton1: TextView = ItemView.findViewById(R.id.btnView1)
    }

    // binds the list items to a view
    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val OrderList = OderList[position]
        holder.OrderAutoid = OrderList.getAId()
        holder.OrderNo.text = OrderList.getOrderNo()
        holder.Customer.text = OrderList.getCustomer()
        holder.salesPerson.text = OrderList.getsalesPerson()
        holder.ShippingType.text = OrderList.getShippingType()
        holder.OrderDate.text = OrderList.getOrderDate()
        holder.Status.text = OrderList.getStatus()
        holder.StausView.setTextColor(Color.parseColor(OrderList.getColorCode()))
        holder.TotalItemss.text = OrderList.getTotalNoOfItem().toString()
        holder.ResumeO = OrderList.getResumeO()
        holder.StatusAutoId=OrderList.getStatusAutoId()
        holder.Start.visibility = View.VISIBLE
        holder.ViewButton1.visibility = View.VISIBLE
        val pDialog = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        var resnsg = holder.Customer.text.toString()
        var resmsg = holder.OrderNo.text.toString()
        if (holder.ResumeO ==0) {
            //holder.Start.setBackgroundColor(Color.MAGENTA)
            holder.Start.setBackgroundColor(Color.parseColor("#079320"))
            holder.Start.setText("Start Packing")
        } else {
            var stusts=-1
            holder.Start.setBackgroundColor(Color.parseColor("#fd5602"))
            holder.Start.setText("Resume Packing")
            val intent = Intent(activity, StartPackingOrderList::class.java)
//            activity?.startActivity(intent)
            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putString("stusts",stusts.toString())
            sharedLoadOrderPage.apply()
        }
        //  holder.Start

        holder.ViewButton1.setOnClickListener(View.OnClickListener {

            val intent = Intent(activity, OrderDetailsActicity::class.java)
            activity?.startActivity(intent)
            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putString("OrderAutoid", holder.OrderAutoid.toString())
            sharedLoadOrderPage.putString("OrderNo", holder.OrderNo.text.toString())
            sharedLoadOrderPage.putString("StatusAutoId", holder.StatusAutoId.toString())
            sharedLoadOrderPage.apply()
        })

        holder.Start.setOnClickListener(View.OnClickListener {
//            SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE).setContentText(resnsg+""+resmsg)
//                .show()
            val Dilogview = View.inflate(activity, R.layout.popupbox_picker, null)
            val builder = AlertDialog.Builder(activity).setView(Dilogview)
            builder.setCancelable(false);
            builder.setCanceledOnTouchOutside(false);
            val mSilog = builder.show()

            var StartAndResumPacking = Dilogview.findViewById<TextView>(R.id.StartAndResumPacking)
            if (holder.ResumeO == 0) {
                StartAndResumPacking.setText("Start Packing")
            } else {
                StartAndResumPacking.setText("Resume Packing")
            }
            var CustomerName = Dilogview.findViewById<TextView>(R.id.CustomerName)
            var OrderNo = Dilogview.findViewById<TextView>(R.id.OrderNo)
            CustomerName.setText(holder.Customer.text.toString())
            OrderNo.setText("Order No : " + holder.OrderNo.text.toString())
            var btnYesPack = Dilogview.findViewById<Button>(R.id.btnYesPack)
            btnYesPack.visibility = View.VISIBLE

            btnYesPack.setOnClickListener(View.OnClickListener {
                val intent = Intent(activity, StartPackingOrderList::class.java)
                activity?.startActivity(intent)
                val sharedLoadOrderPreferences =
                    PreferenceManager.getDefaultSharedPreferences(activity)
                val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
                sharedLoadOrderPage.putString("OrderAutoid", holder.OrderAutoid.toString())
                sharedLoadOrderPage.putString("OrderNo", holder.OrderNo.text.toString())
                sharedLoadOrderPage.putString("StatusAutoId", holder.StatusAutoId.toString())
                sharedLoadOrderPage.apply()
                mSilog.dismiss()
            })
            var btnNoCancle = Dilogview.findViewById<Button>(R.id.btnNoCancle)
            btnNoCancle.setOnClickListener(View.OnClickListener { mSilog.dismiss() })
        })

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.orderlist_view, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (OderList.size != 0) {
            var t: String? = ""
            t = OderList.size.toString()
            (activity as AppCompatActivity).supportActionBar?.setTitle("Processed($t)                 ")
            (activity as AppCompatActivity).getActionBar()?.setDisplayShowTitleEnabled(false);
            (activity as AppCompatActivity).getActionBar()?.setDisplayShowTitleEnabled(true);

        } else {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title =
                "Processed(" + OderList.size + ")"
        }

        return OderList.size
    }

}

