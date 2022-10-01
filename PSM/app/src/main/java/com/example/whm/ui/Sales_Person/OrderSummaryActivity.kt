package com.example.whm.ui.Sales_Person

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.myapplication.R

class OrderSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)
        var btnBackOrderSummary=findViewById<TextView>(R.id.btnBackOrderSummary)
        btnBackOrderSummary.setOnClickListener(View.OnClickListener {
            //onBackPressed()
            finish()
        })
    }
}