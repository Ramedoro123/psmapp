package com.example.whm.ui.Sales_Person

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.CartListAdapterClass
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.CartListModleClass
import org.json.JSONArray
import org.json.JSONObject

class CartViewActicity : AppCompatActivity() {
    var responseResultData = JSONArray()
    var getCartListDetails: MutableList<CartListModleClass> = ArrayList()
    var  accessToken: String? = null
    var  empautoid: String? = null
    var  draftAutoId: Int?=null
    lateinit var CustomerName:TextView
    lateinit var cartListAdapter:CartListAdapterClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_view_acticity)
        var btnBackCart=findViewById<TextView>(R.id.btnBackCart)
        var OrderSummary=findViewById<TextView>(R.id.OrderSummary)
         CustomerName=findViewById<TextView>(R.id.CustomerName)
        var CustomerInformation=findViewById<TextView>(R.id.CustomerInformation)
        btnBackCart.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@CartViewActicity,SalesPersonProductList::class.java))
            finish()
        })
        OrderSummary.setOnClickListener(View.OnClickListener {
           startActivity(Intent(this@CartViewActicity,OrderSummaryActivity::class.java))
            finish()
        })

//        var bundle :Bundle ?=intent.extras
//        draftAutoId= bundle!!.getInt("draftAutoId")
//
//        Log.e("",draftAutoId.toString())
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@CartViewActicity)
       accessToken = preferences.getString("accessToken", "")
       empautoid = preferences.getString("EmpAutoId", "")
        var  customerName = preferences.getString("CustomerName", "")
        var customerId = preferences.getString("customerId", "")
           var draft= preferences.getString("OrderAutoid", "")
        if (draft=="" || draft==null){
             draftAutoId=0
        }else{
            draftAutoId=draft.toString().toInt()
        }
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
                 cartDetailsListFunction()
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

    private fun cartDetailsListFunction() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(false)
        pDialog.show()
        //We Create Json object to send request for server
        val requestContainer= JSONObject()
        val ContainerObject= JSONObject()
        val pObj=JSONObject()
        ContainerObject.put("requestContainer",requestContainer.put("appVersion",AppPreferences.AppVersion))
        ContainerObject.put("requestContainer",requestContainer.put("deviceID", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)))
        ContainerObject.put("requestContainer", requestContainer.put("deviceVersion", AppPreferences.versionRelease))
        ContainerObject.put("requestContainer", requestContainer.put("deviceName", AppPreferences.DeviceName))
        ContainerObject.put("requestContainer",requestContainer.put("accessToken",accessToken))
        ContainerObject.put("requestContainer",requestContainer.put("userAutoId",empautoid))
        ContainerObject.put("pObj",pObj.put("draftAutoId",draftAutoId))
        // send request Queue in vally
        val queue= Volley.newRequestQueue(this)
        val JsonObjectRequest=
            JsonObjectRequest(Request.Method.POST,AppPreferences.cartListApi,ContainerObject,
            { response->
                val Response = (response.toString())
                var responseResultData = JSONObject(Response.toString())
                var ResponseResult= JSONObject(responseResultData.getString("d"))
                val ResponseMessage=ResponseResult.getString("responseMessage")
                val responseStatus=ResponseResult.getString("responseStatus")
                if (responseStatus=="200")
                {
                   var responseData=ResponseResult.getJSONArray("responseData")
                    if (responseData.length()!=null && responseData.length()>0) {
                        for (i in 0 until responseData.length()) {
                            var PId = responseData.getJSONObject(i).getInt("PId")
                            var PName = responseData.getJSONObject(i).getString("PName")
                            var UnitType = responseData.getJSONObject(i).getString("UnitType")
                            var QtyPerUnit = responseData.getJSONObject(i).getInt("QtyPerUnit")
                            var UnitPrice = responseData.getJSONObject(i).getInt("UnitPrice")
                            var ReqQty = responseData.getJSONObject(i).getInt("ReqQty")
                            var NetPrice = responseData.getJSONObject(i).getInt("NetPrice")
                            var Free = responseData.getJSONObject(i).getInt("Free")
                            var Exchange = responseData.getJSONObject(i).getInt("Exchange")
                            var Tax = responseData.getJSONObject(i).getInt("Tax")
                            var UnitAutoId = responseData.getJSONObject(i).getInt("UnitAutoId")
                            var ImgPath = responseData.getJSONObject(i).getString("ImgPath")
                            Log.e("PId", PId.toString())
                            addCartDetailsModelClass(
                                PId,
                                PName,
                                UnitType,
                                QtyPerUnit,
                                UnitPrice,
                                ReqQty,
                                NetPrice,
                                Free,
                                Exchange,
                                Tax,
                                UnitAutoId,
                                ImgPath
                            )
                        }
                        var totalSize = getCartListDetails.size
                        CustomerName.setText("Cart(" + totalSize + ")").toString()
                        pDialog.dismiss()
                    }else{
                        var popUp=SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                        popUp.setContentText(ResponseMessage)
                        popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                        popUp.setConfirmClickListener()
                        {
                                sDialog -> sDialog.dismissWithAnimation()
                            popUp.dismiss()
                        }
                        popUp.show()
                        popUp.setCanceledOnTouchOutside(false)
                        pDialog!!.dismiss()
                    }
                    pDialog.dismiss()
                }
                else
                {
                    var popUp=SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                    popUp.setContentText(ResponseMessage)
                    popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                    popUp.setConfirmClickListener()
                    {
                            sDialog -> sDialog.dismissWithAnimation()
                             popUp.dismiss()
                    }
                    popUp.show()
                    popUp.setCanceledOnTouchOutside(false)
                    pDialog!!.dismiss()
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
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
    private fun addCartDetailsModelClass(
        pId: Int,
        pName: String,
        unitType: String,
        qtyPerUnit: Int,
        unitPrice: Int,
        reqQty: Int,
        netPrice: Int,
        free: Int,
        exchange: Int,
        tax: Int,
        unitAutoId: Int,
        ImgPath:String
    )
    {
        var ModelClassCustomer1 = CartListModleClass(pId,pName,unitType,qtyPerUnit,unitPrice,reqQty,netPrice,free,exchange,tax,unitAutoId,ImgPath)
        getCartListDetails.add(ModelClassCustomer1)
        val recyclerview = findViewById<RecyclerView>(R.id.cartDetailsRecyclerView)
        cartListAdapter = CartListAdapterClass(getCartListDetails, this)
        recyclerview.adapter = cartListAdapter
    }


}