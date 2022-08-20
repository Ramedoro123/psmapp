package com.example.whm.ui.Piker_And_Packer_OrderList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.AdapterClass.Stock_availableAdapter
import com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.ModelClass.StockAvailableModel
import org.json.JSONArray
import org.json.JSONException


class StockNotAvailableList : AppCompatActivity() {
    lateinit var message1:TextView
    lateinit var btnOk1:Button
    var StockData: ArrayList<StockAvailableModel> = arrayListOf()
    lateinit var StockAdapter: Stock_availableAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_not_available_list)
        findbyid()
        btnOk1.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,StartPackingOrderList::class.java));
            (this as Activity).finish();
        })
        val intent = intent
        val jsonArray = intent.getStringExtra("jsonArray")
        var message=intent.getStringExtra("message")
        message1.setText(message.toString())
        try {
            if (jsonArray!=null && jsonArray!=" ") {
                val array = JSONArray(jsonArray)
            val recyclerview = findViewById<RecyclerView>(R.id.StockRecyclerview)
            val layoutManager = LinearLayoutManager(this)
            recyclerview.layoutManager = layoutManager
            for (i in 0 until array.length()) {
                var ProductId = array.getJSONObject(i).getString("ProductId")
                var ProductName = array.getJSONObject(i).getString("ProductName")
                var AvilQty = array.getJSONObject(i).getString("AvilQty")
                var QtyShip = array.getJSONObject(i).getString("QtyShip")
                var RequiredQty = array.getJSONObject(i).getString("RequiredQty")
                StockNotAvaliable(ProductId, ProductName, AvilQty, QtyShip, RequiredQty)
                Log.e("ProductId", ProductId.toString())
                Log.e("ProductName", ProductName.toString())
                Log.e("AvilQty", AvilQty.toString())
            }
        }else{
                Toast.makeText(this,"hello",Toast.LENGTH_LONG).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun StockNotAvaliable(ProductId: String, ProductName: String, AvilQty: String, QtyShip: String, RequiredQty: String) {
        var stockModel=StockAvailableModel(ProductId,ProductName,AvilQty,QtyShip,RequiredQty)
       val recyclerview1 = findViewById<RecyclerView>(R.id.StockRecyclerview)
        StockData.add(stockModel)
        StockAdapter= Stock_availableAdapter(StockData,this)
       recyclerview1.adapter=StockAdapter
        StockAdapter.notifyDataSetChanged()
   }

    fun findbyid(){
        message1=findViewById(R.id.message)
        btnOk1=findViewById(R.id.btnOk1)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}
