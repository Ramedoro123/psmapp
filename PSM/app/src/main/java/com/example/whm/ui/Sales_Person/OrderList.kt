package com.example.whm.ui.Sales_Person

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import org.json.JSONObject

class OrderList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
         var btnBackOrderCustomerOrder:TextView=findViewById(R.id.btnBackOrderCustomerOrder)
        btnBackOrderCustomerOrder.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        OrderListfunctionCall()



    }

    private fun OrderListfunctionCall() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(false)
        pDialog.show()
        //We Create Json object to send request for server
        val requestContainer= JSONObject()
        val searchcustomer= JSONObject()
        val ContainerObject= JSONObject()
        ContainerObject.put("requestContainer",requestContainer.put("appVersion", AppPreferences.AppVersion))
        ContainerObject.put("requestContainer",requestContainer.put("deviceID", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)))
        ContainerObject.put("requestContainer", requestContainer.put("deviceVersion", AppPreferences.versionRelease))
        ContainerObject.put("requestContainer", requestContainer.put("deviceName", AppPreferences.DeviceName))
//        ContainerObject.put("requestContainer",requestContainer.put("accessToken",accessToken))
//        ContainerObject.put("requestContainer",requestContainer.put("userAutoId",empautoid))
//        ContainerObject.put("searchcustomer", searchcustomer.put("customerId",customerId ))
//        ContainerObject.put("searchcustomer", searchcustomer.put("customerName",customerName ))
//        ContainerObject.put("searchcustomer", searchcustomer.put("customerType",autoId ))
//        Log.e("customerType",autoId.toString())
        // Send request Queue in vally
        val queue= Volley.newRequestQueue(this)

        // Request a string response from the provided URL.
        val JsonObjectRequest=
            JsonObjectRequest(Request.Method.POST, AppPreferences.Customer_ListUrl,ContainerObject,
            {response ->
                val Response = (response.toString())
                val responseResultData = JSONObject(Response.toString())
                val ResponseResult= JSONObject(responseResultData.getString("d"))
                val ResponseMessage=ResponseResult.getString("responseMessage")
                val responseCode=ResponseResult.getString("responseCode")
                if (responseCode=="201"){
                    val responseData=ResponseResult.getJSONArray("responseData")
                    for (i in 0 until responseData.length())
                    {
                        var AI=responseData.getJSONObject(i).getInt("AI")
                        var CId=responseData.getJSONObject(i).getString("CId")
                        var CN=responseData.getJSONObject(i).getString("CN")
                        var CT=responseData.getJSONObject(i).getString("CT")
                        var DueBalance=responseData.getJSONObject(i).getString("DBA")
                        val DueBalances : Float = DueBalance.toFloat()
                        var SAdd=responseData.getJSONObject(i).getString("SAdd")
                        var BAdd=responseData.getJSONObject(i).getString("BAdd")
                        var CPN=responseData.getJSONObject(i).getString("CPN")
                        var Email=responseData.getJSONObject(i).getString("E")
                        var C1=responseData.getJSONObject(i).getString("C1")
                        var MN=responseData.getJSONObject(i).getString("MN")
                        var SCA=responseData.getJSONObject(i).getString("SCA")
                        var PLN=responseData.getJSONObject(i).getString("PLN")
                        var TD=responseData.getJSONObject(i).getString("TD")
                        var ctype=responseData.getJSONObject(i).getString("ctype")
                        var OnRoute=responseData.getJSONObject(i).getString("OnRoute")

                        //    Log.e("DueBalance",twoDigitValue.)
//                        SalsePModelClassList(AI,CN,CId,CT,DueBalances,SAdd,BAdd,CPN,Email,C1,MN,SCA,PLN,TD,ctype,OnRoute)
                    }
//                    var totalCustomer=SalsePModelClassList.size
//                    CustomerTitle.setText("Customer List"+"("+totalCustomer+")")
                    pDialog.dismiss()
                }
                else
                {

                    var popUp= SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    popUp.setContentText(ResponseMessage)
                    popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                    popUp.setConfirmClickListener()
                    {
                            sDialog -> sDialog.dismissWithAnimation()
//                        CustomerList("","",)
                    }
                    popUp.show()
                    popUp.setCanceledOnTouchOutside(false)
                    pDialog.dismiss()
                }
            }, Response.ErrorListener { pDialog.dismiss() })
        JsonObjectRequest.retryPolicy= DefaultRetryPolicy(
            10000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        try {
            queue.add(JsonObjectRequest)
        }catch (e:Exception){
            Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
        }
    }
}