package com.example.whm.ui.Sales_Person

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.MainActivity2
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.CartListAdapterClass
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.CartListModleClass
import org.json.JSONArray

class CartViewActicity : AppCompatActivity() {
    var responseResultData = JSONArray()
    var getCartListDetails: MutableList<CartListModleClass> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_view_acticity)
        var btnBackarrow=findViewById<TextView>(R.id.btnBackarrow)
        var CustomerInformation=findViewById<TextView>(R.id.CustomerInformation)
        btnBackarrow.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        val preferences = PreferenceManager.getDefaultSharedPreferences(this@CartViewActicity)
        var accessToken = preferences.getString("accessToken", "")
        var empautoid = preferences.getString("EmpAutoId", "")
        var  customerName = preferences.getString("CustomerName", "")
        var customerId = preferences.getString("customerId", "")

        CustomerInformation.setOnClickListener(View.OnClickListener {
            var dilog=Dialog(this@CartViewActicity)
            dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dilog.setCancelable(false)
            dilog.setContentView(R.layout.success_message_popup)
            dilog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dilog.window!!.setGravity(Gravity.CENTER)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dilog.getWindow()!!.getAttributes())
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            var customerID=dilog.findViewById<TextView>(R.id.Title)
            var customername=dilog.findViewById<TextView>(R.id.messagetitle)
            customerID.visibility=View.VISIBLE
            var btnOk=dilog.findViewById<TextView>(R.id.btnOk)
            customerID.setText(customerId)
            customername.setText(customerName)
            btnOk.setOnClickListener(View.OnClickListener {
                dilog.dismiss()
            })
            dilog.show()
            dilog.getWindow()!!.setAttributes(lp);
            //  Toast.makeText(this,customerName.toString(),Toast.LENGTH_LONG).show()
        })
        if (AppPreferences.internetConnectionCheck(this)){
            val recyclerview = findViewById<RecyclerView>(R.id.cartDetailsRecyclerView)
            val layoutManager = LinearLayoutManager(this)
            recyclerview.layoutManager = layoutManager

            val users = ArrayList<CartListModleClass>()
            users.add(CartListModleClass("Eagle Torch Lighter 3 Tier Display 55Ct","21107","$18.48/Case X 1","$18.48"))
            users.add(CartListModleClass("Bag 1/6 White 600ct - 8051","90001","$17.52/piece X 2","$35.4"))
            users.add(CartListModleClass("Bag 1/8 Recycle Green 250CT","90014","$12.5/Box X 1","$12.5"))
            users.add(CartListModleClass("Bag 1/10 Recycle Green 350CT","90015","$12.48/Case X 2","$24.96"))
            users.add(CartListModleClass("Rain-X Windshield Washer Deicer 1 GAL *","51066","$28.50/Box X 1","$28.50"))
            val obj_adapter = CartListAdapterClass(users)
           var recyclerView=findViewById<RecyclerView>(R.id.cartDetailsRecyclerView)
               recyclerview.adapter=obj_adapter
        }
        else {
            val dialog = this?.let { Dialog(it) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
            val btDismiss =
                dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
            btDismiss?.setOnClickListener {
                dialog.dismiss()
                startActivity(Intent(this, MainActivity2::class.java))
                finish()
            }
            dialog?.show()
        }

    }


}