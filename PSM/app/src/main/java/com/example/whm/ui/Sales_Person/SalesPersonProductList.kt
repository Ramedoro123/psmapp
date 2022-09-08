package com.example.whm.ui.Sales_Person

import android.app.Dialog
import android.app.DownloadManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
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
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.AdapterClassCustomerList
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.SalesPersonProductAdapterClass
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.CustmerTypeModel
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassCustomerList
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.SalesPersonProductModel
import org.json.JSONObject

class SalesPersonProductList : AppCompatActivity() {
    var accessToken:String?=null
    var empautoid:String?=null
    var customerName:String?=null
    var responseMessage:String?=null
    var pDialog:SweetAlertDialog?=null
    var productListModelClass: ArrayList<SalesPersonProductModel> = arrayListOf()
    lateinit var productAdapterClass: SalesPersonProductAdapterClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_person_product_list)
        var UserInformation=findViewById<TextView>(R.id.information)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@SalesPersonProductList)
        accessToken = preferences.getString("accessToken", "")
        empautoid = preferences.getString("EmpAutoId", "")
        customerName = preferences.getString("CustomerName", "")
        UserInformation.setOnClickListener(View.OnClickListener {
            Toast.makeText(this,customerName.toString(),Toast.LENGTH_LONG).show()
        })
        var btnBackarrow=findViewById<TextView>(R.id.btnBackarrow)
        var SearchProduct=findViewById<TextView>(R.id.SearchProduct)
        var cartview=findViewById<ImageView>(R.id.cartview)
        btnBackarrow.setOnClickListener(View.OnClickListener {
            finish();
        })
        SearchProduct.setOnClickListener(View.OnClickListener {
            dialogCustom()
        })
        cartview.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,CartViewActicity::class.java))
        })
if (AppPreferences.internetConnectionCheck(this)){
    val recyclerview = findViewById<RecyclerView>(R.id.ProductListRecyclerView)
    val layoutManager = LinearLayoutManager(this)
    recyclerview.layoutManager = layoutManager
    productListApiCall()
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
    private fun productListApiCall(){
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
        sendRequestObject.put("pObj",pObj.put("productName",""))
        sendRequestObject.put("pObj",pObj.put("productId",""))
        sendRequestObject.put("pObj",pObj.put("customerId",""))
        //send request queue in vally
        val queue=Volley.newRequestQueue(this)
        val JsonObjectRequest=JsonObjectRequest(Request.Method.POST,AppPreferences.getProductListApi,sendRequestObject,
            { response->
                val responseResult=JSONObject(response.toString())
                val responsedData=JSONObject(responseResult.getString("d"))
                 responseMessage=responsedData.getString("responseMessage")
                val responseCode=responsedData.getString("responseCode")
                val responseResultData=responsedData.getJSONArray("responseData")
                if (responseCode=="201")
                {

                    if (responseResultData.length()!=null)
                    {
                   for (i in 0 until responseResultData.length())
                   {
                       var PId=responseResultData.getJSONObject(i).getString("PId")
                       var PName=responseResultData.getJSONObject(i).getString("PName")
                       var ImageUrl=responseResultData.getJSONObject(i).getString("ImageUrl")
                       var CStock=responseResultData.getJSONObject(i).getInt("CStock")
                       var balencePrice=responseResultData.getJSONObject(i).getDouble("BP")
                       var UnitType=responseResultData.getJSONObject(i).getString("UnitType")
                       var BP:Float=balencePrice.toFloat()
                       addProductdataModelClass(PId,PName,ImageUrl,CStock,BP,UnitType)
                   }

                         pDialog!!.dismiss()
                    }
                    else
                    {
                        massagePopupAlert()
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


    private fun addProductdataModelClass(PId:String,PName:String,ImageUrl:String,CStock:Int,BP:Float,UnitType:String) {
        var productModelClass = SalesPersonProductModel(PId,PName, ImageUrl,CStock,BP,UnitType)
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
        var CustomerID=dilog.findViewById<EditText>(R.id.CustomerID)
        var CustomerName=dilog.findViewById<EditText>(R.id.CustomerName)
        CustomerID.setText("Product ID ")
        CustomerName.setText("Product Name ")
        var btnSearch=dilog.findViewById<Button>(R.id.btnSearch)
        var btnCancle=dilog.findViewById<TextView>(R.id.CancleBtn)
        var spinner =dilog.findViewById<View>(R.id.Spinnervalue) as Spinner
        spinner.visibility=View.GONE
        btnCancle.setOnClickListener(View.OnClickListener {
            dilog.dismiss()
        })
        btnSearch.setOnClickListener(View.OnClickListener {
//            ModelClassCustomer.clear()
//            CustomerList(customerId = CustomerID.text.toString(), customerName = CustomerName.text.toString())
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
        }
        popUp.show()
        popUp.setCanceledOnTouchOutside(false)
        pDialog!!.dismiss()
    }
}