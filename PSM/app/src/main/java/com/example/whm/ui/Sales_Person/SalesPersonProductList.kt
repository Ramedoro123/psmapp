package com.example.whm.ui.Sales_Person

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.SalesPersonProductAdapterClass
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.SalesPersonProductModel
import org.json.JSONObject


class SalesPersonProductList : AppCompatActivity() {
    var accessToken:String?=null
    var empautoid:String?=null
    var customerName:String?=null
    var customerId:String?=null
    var username:String?=null
    var draftAutoId:Int?=null
    var ProductItemList:String?=null
    lateinit var productTitle:TextView
    lateinit var cart_badge:TextView
    lateinit var orderTotalValue:TextView
    var responseMessage:String?=null
    var pDialog:SweetAlertDialog?=null

    var productListModelClass: ArrayList<SalesPersonProductModel> = arrayListOf()
    lateinit var productAdapterClass: SalesPersonProductAdapterClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_person_product_list)
        var userInformation=findViewById<TextView>(R.id.information)
        var ScanBarcode=findViewById<EditText>(R.id.ScanBarcodeEditeTextView)
         cart_badge=findViewById<TextView>(R.id.cart_badge)
         orderTotalValue=findViewById<TextView>(R.id.cart2)

          productTitle=findViewById<TextView>(R.id.productTitle)
        var syncProduct=findViewById<TextView>(R.id.syncProduct)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@SalesPersonProductList)
        accessToken = preferences.getString("accessToken", "")
        empautoid = preferences.getString("EmpAutoId", "")
        customerName = preferences.getString("CustomerName", "")
        customerId = preferences.getString("customerId", "")
        ProductItemList = preferences.getString("ProductItemList", "")
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastReceiver, IntentFilter("USER_NAME_CHANGED_ACTION"))
        userInformation.setOnClickListener(View.OnClickListener {
            var dilog=Dialog(this@SalesPersonProductList)
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
            pDialog!!.dismiss()
          //  Toast.makeText(this,customerName.toString(),Toast.LENGTH_LONG).show()
        })
        syncProduct.setOnClickListener(View.OnClickListener {
            if (AppPreferences.internetConnectionCheck(this)) {
                val recyclerview = findViewById<RecyclerView>(R.id.ProductListRecyclerView)
                val layoutManager = LinearLayoutManager(this)
                recyclerview.layoutManager = layoutManager
                productListApiCall("","","")
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
        })
        var btnBackarrow1=findViewById<TextView>(R.id.btnSalesPersonBack)
        var SearchProduct=findViewById<TextView>(R.id.SearchProduct)
        var cartview=findViewById<ImageView>(R.id.cartview)
         btnBackarrow1.setOnClickListener(View.OnClickListener {
            finish();
        })
         SearchProduct.setOnClickListener(View.OnClickListener {
            dialogCustom()
        })
        cartview.setOnClickListener(View.OnClickListener {
            var intent=Intent(this,CartViewActicity::class.java)
               intent.putExtra("draftAutoId",draftAutoId)
               startActivity(intent)
               finish()
        })
        if (AppPreferences.internetConnectionCheck(this)) {
            ScanBarcode!!.requestFocus()
            ScanBarcode!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                    var scanbarcodeproduct= ScanBarcode!!.text.toString()
                    if (scanbarcodeproduct.trim().isEmpty()) {
                        ScanBarcode.text.clear()
                        ScanBarcode.setText("")
                        ScanBarcode.requestFocus()
                    } else {
                        // Scan barcode function calling here
                        productListModelClass.clear()
                        productListApiCall("","",barcode=ScanBarcode.text.toString())
                        ScanBarcode.requestFocus()
                        ScanBarcode.text.clear()
                        // btnPrevious.isEnabled=true
                    }
                }

                false

            })
        }
if (AppPreferences.internetConnectionCheck(this)){
    val recyclerview = findViewById<RecyclerView>(R.id.ProductListRecyclerView)
    val layoutManager = LinearLayoutManager(this)
    recyclerview.layoutManager = layoutManager
    productListApiCall("","","")
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

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            if (intent!=null) {
                username = intent.getStringExtra("username");
                var total = intent.getStringExtra("Total");
                draftAutoId = intent.getIntExtra("draftAutoId",0);
                var totalValue=total.toString().toDouble()
                cart_badge.setText(username)
                orderTotalValue.setText("Order Total : %.2f".format(totalValue))
            }
        }
    }
    private fun productListApiCall(productId: String, productName: String,barcode:String){
        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog!!.titleText = "Fetching ..."
        pDialog!!.setCancelable(false)
        pDialog!!.show()
        val sendRequestObject=JSONObject()
        val requestContainer=JSONObject()
        val pObj=JSONObject()
        sendRequestObject.put("requestContainer",requestContainer.put("appVersion",AppPreferences.AppVersion))
        sendRequestObject.put("requestContainer",requestContainer.put("deviceID", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)))
        sendRequestObject.put("requestContainer", requestContainer.put("deviceVersion", AppPreferences.versionRelease))
        sendRequestObject.put("requestContainer", requestContainer.put("deviceName", AppPreferences.DeviceName))
        sendRequestObject.put("requestContainer",requestContainer.put("accessToken",accessToken))
        sendRequestObject.put("requestContainer",requestContainer.put("userAutoId",empautoid))
        sendRequestObject.put("pObj",pObj.put("productName",productName))
        sendRequestObject.put("pObj",pObj.put("productId",productId))
        sendRequestObject.put("pObj",pObj.put("customerId",customerId))
        sendRequestObject.put("pObj",pObj.put("barcode",barcode))
        //send request queue in vally
        val queue=Volley.newRequestQueue(this)
        val JsonObjectRequest=JsonObjectRequest(Request.Method.POST,AppPreferences.getProductListApi,sendRequestObject,
            { response->
                val responseResult=JSONObject(response.toString())
                val responsedData=JSONObject(responseResult.getString("d"))
                 responseMessage=responsedData.getString("responseMessage")
                val responseCode=responsedData.getString("responseCode")
                productListModelClass.clear()
                if (responseCode=="201")
                {
                    val responseResultData=responsedData.getJSONArray("responseData")
                    if (responseResultData.length()!=null && responseResultData.length()>0)
                    {
                   for (i in 0 until responseResultData.length())
                   {
                       var PId=responseResultData.getJSONObject(i).getString("PId")
                       var PName=responseResultData.getJSONObject(i).getString("PName")
                       var ImageUrl=responseResultData.getJSONObject(i).getString("ImageUrl")
                       var CStock=responseResultData.getJSONObject(i).getString("CStock")
                       var balencePrice=responseResultData.getJSONObject(i).getDouble("BP")
                       var UnitType=responseResultData.getJSONObject(i).getString("UnitType")
                       var BP:Float=balencePrice.toFloat()
                       addProductdataModelClass(PId,PName,ImageUrl,CStock,BP,UnitType)
                   }
                        var totalSize=productListModelClass.size
                         productTitle.setText("Product("+totalSize+")").toString()
                         pDialog!!.dismiss()
                    }
                    else
                    {
                        var popUp=SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                        popUp.setContentText(responseMessage)
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
                    pDialog!!.dismiss()
                }
                else{
                    massagePopupAlert()
                }
        },Response.ErrorListener { pDialog!!.dismiss() })
        JsonObjectRequest.retryPolicy=DefaultRetryPolicy(
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
    private fun addProductdataModelClass(PId:String,PName:String,ImageUrl:String,CStock:String,BP:Float,UnitType:String) {
        var productModelClass = SalesPersonProductModel(PId,PName, ImageUrl,CStock,BP,UnitType,0,"","","",0)
        productListModelClass.add(productModelClass)
        val recyclerview = findViewById<RecyclerView>(R.id.ProductListRecyclerView)
        productAdapterClass = SalesPersonProductAdapterClass(productListModelClass, this)
        recyclerview.adapter = productAdapterClass
    }
    private  fun dialogCustom(){
        var dilog=Dialog(this@SalesPersonProductList)
        dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dilog.setCancelable(false)
        dilog.setContentView(R.layout.activity_search_customer)
        dilog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dilog.window!!.setGravity(Gravity.CENTER)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dilog.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        var productId=dilog.findViewById<EditText>(R.id.AmountP)
        var productName=dilog.findViewById<EditText>(R.id.CustomerName)
        productId.setHint("Product ID ")
        productName.setHint("Product Name ")
        var btnSearch=dilog.findViewById<Button>(R.id.btnSearch)
        var btnCancle=dilog.findViewById<TextView>(R.id.CancleBtn)
        var spinner =dilog.findViewById<View>(R.id.Spinnervalue) as Spinner
        spinner.visibility=View.GONE
        btnCancle.setOnClickListener(View.OnClickListener {
            dilog.dismiss()
        })
        btnSearch.setOnClickListener(View.OnClickListener {
            productListModelClass.clear()
            productListApiCall(productId = productId.text.toString(), productName = productName.text.toString(),"")
            dilog.dismiss()
        })
        dilog.show()
        dilog.getWindow()!!.setAttributes(lp);
    }
    private fun massagePopupAlert(){
        var popUp=SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
        popUp.setContentText(responseMessage)
        popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
        popUp.setConfirmClickListener()
        {
                sDialog -> sDialog.dismissWithAnimation()
                productListApiCall("", "","")
        }
        popUp.show()
        popUp.setCanceledOnTouchOutside(false)
        pDialog!!.dismiss()
    }
}

