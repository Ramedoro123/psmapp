package com.example.whm.ui.Sales_Person

import android.accounts.AccountManager.get
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.AdapterClassCustomerList
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassCustomerList
import java.io.ObjectStreamClass

class CustomerListActivity : AppCompatActivity() {

    var ModelClassCustomer: ArrayList<ModelClassCustomerList> = arrayListOf()
    lateinit var CustomerAdapter: AdapterClassCustomerList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list)

        val preferences= PreferenceManager.getDefaultSharedPreferences(this@CustomerListActivity)
        var accessToken = preferences.getString("accessToken", "")
        var empautoid = preferences.getString("EmpAutoId", "")
              Log.e("accessToken",accessToken.toString())
              Log.e("empautoid",empautoid.toString())
        val recyclerview = findViewById<RecyclerView>(R.id.CustomerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        val marks = arrayOf("7 Eleven (Toms River Rt 37 West)",
            "7 ELEVEN (MANNY TOMS RIVER ALL STORES)","Country Food Market (Bayville)","Robins Deli & Convenience (Beachwood)",
            "Hometown Food Market (Forked River)","Sawyers Liquor (Beachwood)")
        val marks1 = arrayOf("CST10213",
            "CST10214","CST10215","CST10216",
            "CST10217","CST10218")
        var d:Int=6
        val data = HashMap<String, String>()
        data["CN"] = "7 Eleven (Toms River Rt 37 West)"
        data["CN"] = "7 ELEVEN (MANNY TOMS RIVER ALL STORES)"
        data["CN"] = "Country Food Market (Bayville)"
        data["CN"] = "Robins Deli & Convenience (Beachwood))"
        data["CN"] = "Sawyers Liquor (Beachwood)"
        data["CN"] = "Hometown Food Market (Forked River)"
        data["CId"] = "CST10213"
        data["CId"] = "CST10214"
        data["CId"] = "CST10215"
        data["CId"] = "CST10216"
        data["CId"] = "CST10217"
        data["CId"] = "CST10218"

        for (i in 0 until marks.size) {
            var CN=marks.get(i)
            var CId=marks1.get(i)
                    ModelClassCustomer(
                        CN.toString(),
                        CId.toString()
                    )
        }
    }
    private fun ModelClassCustomer( CN: String,CId:String)    {
        var ModelClassCustomer1= ModelClassCustomerList(CN,CId)

        ModelClassCustomer.add(ModelClassCustomer1)
        val recyclerview = findViewById<RecyclerView>(R.id.CustomerView)
        CustomerAdapter= AdapterClassCustomerList(ModelClassCustomer, this)
        recyclerview.adapter = CustomerAdapter
        // DetailsAdapter.notifyDataSetChanged()
    }
}


