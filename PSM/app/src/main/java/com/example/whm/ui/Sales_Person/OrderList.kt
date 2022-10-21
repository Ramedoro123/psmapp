package com.example.whm.ui.Sales_Person

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.SalseP_OrderListAdapter
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassOrderCustomerList
import org.json.JSONObject

class OrderList : AppCompatActivity(),View.OnClickListener,SalseP_OrderListAdapter.OnItemClickListener{
    var modelClassCustomerOrder: ArrayList<ModelClassOrderCustomerList> = arrayListOf()
    lateinit var customerOrderAdapter: SalseP_OrderListAdapter
    lateinit var customerOrderHeader:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
         var btnBackOrderCustomerOrder:TextView=findViewById(R.id.btnBackOrderCustomerOrder)
        customerOrderHeader=findViewById(R.id.customerOrderHeader)
        btnBackOrderCustomerOrder.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        if (AppPreferences.internetConnectionCheck(this)) {
            val recyclerview = findViewById<RecyclerView>(R.id.OrderCustomerRecyclerView)
            val layoutManager = LinearLayoutManager(this)
            recyclerview.layoutManager = layoutManager
            OrderListfunctionCall()
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

    private fun OrderListfunctionCall() {
        if (AppPreferences.internetConnectionCheck(this)) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            var accessToken = preferences.getString("accessToken", "")
            var empautoid = preferences.getString("EmpAutoId", "")
            var customerAutoId = preferences.getString("customerAutoId", "")
            val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog.titleText = "Fetching ..."
            pDialog.setCancelable(false)
            pDialog.show()
            //We Create Json object to send request for server
            val requestContainer = JSONObject()
            val searchcustomer = JSONObject()
            val ContainerObject = JSONObject()
            ContainerObject.put("requestContainer",
                requestContainer.put("appVersion", AppPreferences.AppVersion))
            ContainerObject.put("requestContainer",
                requestContainer.put("deviceID",
                    Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)))
            ContainerObject.put("requestContainer",
                requestContainer.put("deviceVersion", AppPreferences.versionRelease))
            ContainerObject.put("requestContainer",
                requestContainer.put("deviceName", AppPreferences.DeviceName))
        ContainerObject.put("requestContainer",requestContainer.put("accessToken",accessToken))
        ContainerObject.put("requestContainer",requestContainer.put("userAutoId",empautoid))
        ContainerObject.put("pObj", searchcustomer.put("CustomerAutoid",0 ))
//        ContainerObject.put("searchcustomer", searchcustomer.put("customerName",customerName ))
//        ContainerObject.put("searchcustomer", searchcustomer.put("customerType",autoId ))
        Log.e("ContainerObject Order list request",ContainerObject.toString())
            // Send request Queue in vally
            val queue = Volley.newRequestQueue(this)

            // Request a string response from the provided URL.
            val JsonObjectRequest =
                JsonObjectRequest(Request.Method.POST,
                    AppPreferences.OrderListSalesPerson,
                    ContainerObject,
                    { response ->
                        val Response = (response.toString())
                        val responseResultData = JSONObject(Response.toString())
                        val ResponseResult = JSONObject(responseResultData.getString("d"))
                        val ResponseMessage = ResponseResult.getString("responseMessage")
                        val responseCode = ResponseResult.getString("responseStatus")
                        if (responseCode == "200") {
                            val responseData = ResponseResult.getJSONArray("responseData")
                            Log.e("responseData",responseData.toString())
                            for (i in 0 until responseData.length()) {
                                var AutoId = responseData.getJSONObject(i).getInt("AutoId")
                                var OrderNo = responseData.getJSONObject(i).getString("OrderNo")
                                var OrderDate = responseData.getJSONObject(i).getString("OrderDate")
                                var CustomerName = responseData.getJSONObject(i).getString("CustomerName")
                                var Status = responseData.getJSONObject(i).getString("Status")
                                var StatusCode = responseData.getJSONObject(i).getString("StatusCode")
                                var GrandTotal = responseData.getJSONObject(i).getString("GrandTotal")
                                var SalesPerson = responseData.getJSONObject(i).getString("SalesPerson")
                                var ShippingType = responseData.getJSONObject(i).getString("ShippingType")
                                var ShipId = responseData.getJSONObject(i).getString("ShipId")
                                var NoOfItems = responseData.getJSONObject(i).getString("NoOfItems")
                                var CreditMemo = responseData.getJSONObject(i).getString("CreditMemo")
                                var ColorCode = responseData.getJSONObject(i).getString("ColorCode")
                                Log.e("ColorCode1",ColorCode.toString())
                                //    Log.e("DueBalance",twoDigitValue.)
                        SalsePModelClassList(AutoId,OrderNo,OrderDate,CustomerName,Status,StatusCode,GrandTotal,SalesPerson,ShippingType,ShipId
                            ,NoOfItems,CreditMemo,ColorCode)
                            }
                    var totalOrder=modelClassCustomerOrder.size
                    customerOrderHeader.setText("Order List"+"("+totalOrder+")")
                            pDialog.dismiss()
                        } else {

                            var popUp = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            popUp.setContentText(ResponseMessage)
                            popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                            popUp.setConfirmClickListener()
                            { sDialog ->
                                sDialog.dismissWithAnimation()
//                        CustomerList("","",)
                            }
                            popUp.show()
                            popUp.setCanceledOnTouchOutside(false)
                            pDialog.dismiss()
                        }
                    },
                    Response.ErrorListener { pDialog.dismiss() })
            JsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            try {
                queue.add(JsonObjectRequest)
            } catch (e: Exception) {
                Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
            }
        }
        else{
            val dialog = this?.let { Dialog(it) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
            val btDismiss =
                dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
            btDismiss?.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(this, MainActivity2::class.java)
                this?.startActivity(intent)
                finish()

            }
            dialog?.show()
        }
    }

    private fun SalsePModelClassList(
        autoId: Int,
        orderNo: String,
        orderDate: String,
        customerName: String,
        status: String,
        statusCode: String,
        grandTotal: String,
        salesPerson: String,
        shippingType: String,
        shipId: String,
        noOfItems: String,
        creditMemo: String,
        colorCode: String
    ) {
        var ModelClassCustomer1 = ModelClassOrderCustomerList(autoId,orderNo,orderDate,customerName,status,statusCode,grandTotal,salesPerson,shippingType,shipId,noOfItems
        ,creditMemo,colorCode)
        modelClassCustomerOrder.add(ModelClassCustomer1)
        val recyclerview = findViewById<RecyclerView>(R.id.OrderCustomerRecyclerView)
        customerOrderAdapter = SalseP_OrderListAdapter(modelClassCustomerOrder, this,this@OrderList)
        recyclerview.adapter = customerOrderAdapter

    }

    override fun onClick(v: View?) {

    }

    override fun OnItemsClick(position: Int) {
        var productOrderDetals=modelClassCustomerOrder[position]
        val intent:Intent=Intent(this,SalesPersonOrderDetailsActivity::class.java)
            intent.putExtra("orderAutoId",productOrderDetals.getautoId())
           startActivity(intent)
           finish()


    }

    override fun OnDeleteClicks(position: Int) {

    }

}