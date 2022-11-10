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
import androidx.recyclerview.widget.ItemTouchHelper
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
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.DraftOrderListAdapter
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassDraftOrderList
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.SalesPersonProductModel
import com.example.myapplication.com.example.whm.ui.Sales_Person.SwipeGesture
import org.json.JSONObject

class DraftOrderList : AppCompatActivity(),View.OnClickListener,DraftOrderListAdapter.OnItemClickListeners {
    var modelClassDraftOrder: ArrayList<ModelClassDraftOrderList> = arrayListOf()
    lateinit var customerOrderAdapter: DraftOrderListAdapter
    lateinit var SalesDraftOrder: TextView
    lateinit var btnBackDraftOrder: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft_order_list)
        SalesDraftOrder=findViewById(R.id.SalesDraftOrder)
        btnBackDraftOrder=findViewById(R.id.btnBackDraftOrder)
        btnBackDraftOrder.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


        if (AppPreferences.internetConnectionCheck(this)) {
            val recyclerview = findViewById<RecyclerView>(R.id.OrderCustomerRecyclerView)
            val layoutManager = LinearLayoutManager(this)
            recyclerview.layoutManager = layoutManager
            DraftListfunctionCall()
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
    private fun deleteDraftOrder(draftAutoid:Int) {
        if (AppPreferences.internetConnectionCheck(this)) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            var accessToken = preferences.getString("accessToken", "")
            var empautoid = preferences.getString("EmpAutoId", "")
            var pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog!!.titleText = "Fetching ..."
            pDialog!!.setCancelable(false)
            pDialog!!.show()
            val sendRequestObject = JSONObject()
            val requestContainer = JSONObject()
            val pObj = JSONObject()
            sendRequestObject.put(
                "requestContainer",
                requestContainer.put("appVersion", AppPreferences.AppVersion)
            )
            sendRequestObject.put(
                "requestContainer", requestContainer.put(
                    "deviceID",
                    Settings.Secure.getString(
                        this!!.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                )
            )
            sendRequestObject.put(
                "requestContainer",
                requestContainer.put("deviceVersion", AppPreferences.versionRelease)
            )
            sendRequestObject.put(
                "requestContainer",
                requestContainer.put("deviceName", AppPreferences.DeviceName)
            )
            sendRequestObject.put(
                "requestContainer",
                requestContainer.put("accessToken", accessToken)
            )
            sendRequestObject.put("requestContainer", requestContainer.put("userAutoId", empautoid))
            if (draftAutoid!=0) {
                sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoid))
            }

            Log.e("sendRequestObject order ordersubmit", sendRequestObject.toString())

            //send request queue in vally
            val queue = Volley.newRequestQueue(this)
            val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
                AppPreferences.DeleteDraftOrder, sendRequestObject,
                { response ->
                    val responseResult = JSONObject(response.toString())
                    val responsedData = JSONObject(responseResult.getString("d"))
                    var responseMessage2 = responsedData.getString("responseMessage")
                    val responseStatus = responsedData.getInt("responseStatus")
                    if (responseStatus == 200) {

                        var popUp = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        popUp.setContentText(responseMessage2.toString())
                        popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                        popUp.setConfirmClickListener()
                        { sDialog ->
                            sDialog.dismissWithAnimation()
//                            startActivity(Intent(this,MainActivity2::class.java))
                            SalesDraftOrder.setText(" Draft Order List"+"("+modelClassDraftOrder.size+")")
                            customerOrderAdapter.notifyDataSetChanged()
//                             finish()
                            popUp.dismiss()
                            pDialog.dismiss()
                        }
                        popUp.show()
                        popUp.setCanceledOnTouchOutside(false)
                        popUp.setCancelable(false)
                        pDialog.setCancelable(false)

                    } else {
                        var popUp = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        popUp.setContentText(responseMessage2.toString())
                        popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                        popUp.setConfirmClickListener()
                        { sDialog ->
                            sDialog.dismissWithAnimation()
                            popUp.dismiss()
                            pDialog.dismiss()
                        }
                        popUp.show()
                        popUp.setCanceledOnTouchOutside(false)
                        popUp.setCancelable(false)
                        pDialog.setCancelable(false)
//                                        warningMessage(message = responseMessage1.toString())
//                                        Log.e("message",responseMessage1.toString())
                        //  pDialog!!.dismiss()
                    }
                },
                Response.ErrorListener { pDialog!!.dismiss() })
            JsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            try {
                queue.add(JsonObjectRequest)
            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        } else {
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
    private fun DraftListfunctionCall() {
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
            ContainerObject.put("pObj", searchcustomer.put("CustomerName","" ))
            ContainerObject.put("pObj", searchcustomer.put("FromDate","" ))
            ContainerObject.put("pObj", searchcustomer.put("ToDate","" ))
//        ContainerObject.put("searchcustomer", searchcustomer.put("customerName",customerName ))
//        ContainerObject.put("searchcustomer", searchcustomer.put("customerType",autoId ))
            Log.e("ContainerObject Order list request",ContainerObject.toString())
            // Send request Queue in vally
            val queue = Volley.newRequestQueue(this)

            // Request a string response from the provided URL.
            val JsonObjectRequest =
                JsonObjectRequest(Request.Method.POST,
                    AppPreferences.DraftOrderList,
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
                                var DraftAutoId = responseData.getJSONObject(i).getInt("DraftAutoId")
                                var OrderDate = responseData.getJSONObject(i).getString("OrderDate")
                                var CustomerName = responseData.getJSONObject(i).getString("CustomerName")
                                var Status = responseData.getJSONObject(i).getString("Status")
                                var NoOfItems = responseData.getJSONObject(i).getString("NoOfItems")
                                var ColorCode = responseData.getJSONObject(i).getString("ColorCode")
                                var grandTotal = responseData.getJSONObject(i).getString("GrandAmount")
                                Log.e("ColorCode1",ColorCode.toString())
                                //    Log.e("DueBalance",twoDigitValue.)
                                SalsePModelClassList(DraftAutoId,OrderDate,CustomerName,Status
                                    ,NoOfItems,ColorCode, grandTotal)
                            }
                            var totalOrder=modelClassDraftOrder.size
                            SalesDraftOrder.setText(" Draft Order List"+"("+totalOrder+")")
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
        DraftAutoId: Int,
        orderDate: String,
        customerName: String,
        status: String,
        noOfItems: String,
        colorCode: String,
        grandTotal: String,
    ) {
        var ModelClassCustomer1 = ModelClassDraftOrderList(DraftAutoId,orderDate,customerName,status,noOfItems,colorCode,grandTotal)
        modelClassDraftOrder.add(ModelClassCustomer1)
        val recyclerview = findViewById<RecyclerView>(R.id.OrderCustomerRecyclerView)
        customerOrderAdapter = DraftOrderListAdapter(modelClassDraftOrder, this,this@DraftOrderList)

        val SwipeGesture=object :SwipeGesture(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val ClickedItem: ModelClassDraftOrderList=modelClassDraftOrder[direction]
                when(direction){
                    ItemTouchHelper.LEFT ->{
                        customerOrderAdapter.deleteItem(viewHolder.adapterPosition)
//                        Toast.makeText(this@DraftOrderList,customerOrderAdapter.toString(),Toast.LENGTH_LONG).show()
                        deleteDraftOrder(draftAutoid=ClickedItem.getDraftAutoId()!!.toInt())
                    }
                    ItemTouchHelper.RIGHT->{
                        val ClickedItem: ModelClassDraftOrderList=modelClassDraftOrder[direction]
                        var intent:Intent=Intent(this@DraftOrderList,SalesPersonProductList::class.java)
                        intent.putExtra("draftAutoIdd",ClickedItem.getDraftAutoId().toString())
                        intent.putExtra("grandTotal",ClickedItem.getgrandTotal().toString())
                        intent.putExtra("noOfItems",ClickedItem.getnoOfItems().toString())
                        Toast.makeText(this@DraftOrderList,ClickedItem.getDraftAutoId().toString(),Toast.LENGTH_LONG).show()
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        val tochHelper=ItemTouchHelper(SwipeGesture)
        tochHelper.attachToRecyclerView(recyclerview)
        recyclerview.adapter = customerOrderAdapter
    }

    override fun onClick(v: View?) {

    }

    override fun OnItemsClick(position: Int)
    {
        val ClickedItem: ModelClassDraftOrderList=modelClassDraftOrder[position]
        var intent:Intent=Intent(this,SalesPersonProductList::class.java)
        intent.putExtra("draftAutoIdd",ClickedItem.getDraftAutoId().toString())
        intent.putExtra("grandTotal",ClickedItem.getgrandTotal().toString())
        intent.putExtra("noOfItems",ClickedItem.getnoOfItems().toString())
        startActivity(intent)
        finish()
    }

    override fun OnDeleteClicks(position: Int) {

    }
}