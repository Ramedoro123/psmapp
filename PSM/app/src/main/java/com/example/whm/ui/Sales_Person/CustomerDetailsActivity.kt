package com.example.whm.ui.Sales_Person

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import com.example.myapplication.R

class CustomerDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_details)
        var btnBackarrow=findViewById<TextView>(R.id.btnBackarrow)
        var CustomerName=findViewById<TextView>(R.id.CustomerName)
        var moreOption=findViewById<ImageView>(R.id.moreOption)

        btnBackarrow.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,CustomerListActivity::class.java))
        })
        val preferences= PreferenceManager.getDefaultSharedPreferences(this@CustomerDetailsActivity)
              var CustomerNames=preferences.getString("CustomerName","")
        moreOption.setOnClickListener(View.OnClickListener {
        val popupMenu=PopupMenu(this,it)
            popupMenu.setOnMenuItemClickListener { item->
                when(item.itemId){
                R.id.NewOrder->{
                    startActivity(Intent(this, SalesPersonProductList::class.java))
                 true
                }
                    else->false
                }
            }
            popupMenu.inflate(R.menu.more_optionmenu)
            popupMenu.show()
        })
    }
}