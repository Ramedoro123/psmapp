package com.example.whm.ui.Sales_Person

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.myapplication.R

class SalesPersonProductList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_list)
        var btnBackarrow=findViewById<TextView>(R.id.btnBackarrow)
        btnBackarrow.setOnClickListener(View.OnClickListener {
            finish();
        })

    }
}