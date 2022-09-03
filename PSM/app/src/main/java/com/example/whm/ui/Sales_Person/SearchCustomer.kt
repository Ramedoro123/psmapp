package com.example.whm.ui.Sales_Person

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.myapplication.R
import kotlin.Array as Array1

class SearchCustomer : AppCompatActivity() {
    lateinit var spinner:Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DilogCustom()
        spinner!!.prompt = "Select your favorite Planet!"
        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.city_list, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner!!.adapter=adapter
    }
    private  fun DilogCustom(){
        var dilog= Dialog(this@SearchCustomer)
        dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dilog.setCancelable(false)
        dilog.setContentView(R.layout.activity_search_customer)
        dilog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dilog.window!!.setGravity(Gravity.CENTER)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dilog.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        var btnCancle=dilog.findViewById<TextView>(R.id.CancleBtn)
        spinner =dilog.findViewById<View>(R.id.textView30) as Spinner
        btnCancle.setOnClickListener(View.OnClickListener {
            dilog.dismiss()
            finish()
        })
        dilog.show()
        dilog.getWindow()!!.setAttributes(lp);
    }
}