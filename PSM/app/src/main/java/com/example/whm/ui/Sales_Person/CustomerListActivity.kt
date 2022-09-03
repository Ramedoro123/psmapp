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
import android.widget.*
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
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.AdapterClassCustomerList
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassCustomerList
import org.json.JSONArray
import org.json.JSONObject


class CustomerListActivity : AppCompatActivity() {

    var ModelClassCustomer: ArrayList<ModelClassCustomerList> = arrayListOf()
    lateinit var CustomerAdapter: AdapterClassCustomerList
    var accessToken: String? = null
    var empautoid: String? = null
    var CustomerlistSize: String? = null
    var CustomerType: String? = null
    var CustomerType1: JSONArray? = null
    lateinit var spinner:Spinner
    lateinit var SearchCustomer1:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list)
        var backbtn=findViewById<TextView>(R.id.btnBackarrow)
           SearchCustomer1=findViewById<TextView>(R.id.SearchCustomer)
        var CustomerTitle=findViewById<TextView>(R.id.CustomerTitle)

        backbtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,MainActivity2::class.java))
            finish()
        })




        val preferences = PreferenceManager.getDefaultSharedPreferences(this@CustomerListActivity)
        accessToken = preferences.getString("accessToken", "")
        empautoid = preferences.getString("EmpAutoId", "")
        CustomerlistSize = preferences.getString("CustomerlistSize", "")
        CustomerTitle.setText("Customer List"+"("+CustomerlistSize+")")

       // Toast.makeText(this,CustomerlistSize.toString(),Toast.LENGTH_LONG).show()
        if (AppPreferences.internetConnectionCheck(this)) {
            Log.e("accessToken", accessToken.toString())
            Log.e("empautoid", empautoid.toString())
            val recyclerview = findViewById<RecyclerView>(R.id.CustomerView)
            val layoutManager = LinearLayoutManager(this)
            recyclerview.layoutManager = layoutManager
            CustomerList()
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

    private fun getCustomerType() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(false)
        pDialog.show()

        //We Create Json object to send request for server

        val requestContainer=JSONObject()
        val ContainerObject=JSONObject()
        ContainerObject.put("requestContainer",requestContainer.put("appVersion",AppPreferences.AppVersion))
        ContainerObject.put("requestContainer",requestContainer.put("deviceID", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)))
        ContainerObject.put("requestContainer", requestContainer.put("deviceVersion", AppPreferences.versionRelease))
        ContainerObject.put("requestContainer", requestContainer.put("deviceName", AppPreferences.DeviceName))
        ContainerObject.put("requestContainer",requestContainer.put("accessToken",accessToken))
        ContainerObject.put("requestContainer",requestContainer.put("userAutoId",empautoid))
        // send request Queue in vally
        val queue=Volley.newRequestQueue(this)
        val JsonObjectRequest=JsonObjectRequest(Request.Method.POST,AppPreferences.getCustomerType,ContainerObject,
            { response->
                val Response = (response.toString())
                val responseResultData = JSONObject(Response.toString())
                val ResponseResult=JSONObject(responseResultData.getString("d"))
                val ResponseMessage=ResponseResult.getString("responseMessage")
                val responseCode=ResponseResult.getString("responseCode")
                if (responseCode=="201")
                {
                  var  CustomerType1=ResponseResult.getJSONArray("responseData")
                    SearchCustomer1.setOnClickListener(View.OnClickListener {
                        if (CustomerType1.length()>0) {
                            DilogCustom()
                            Log.e("CustomerType",CustomerType1.toString())
                            val arrayList = ArrayList<String>()
                            spinner!!.prompt = "Select your favorite Planet!"
                            // Create an ArrayAdapter
                            val adapter = ArrayAdapter.createFromResource(
                                this,
                                R.array.city_list, android.R.layout.simple_spinner_item
                            )
                            // Specify the layout to use when the list of choices appears
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            // Apply the adapter to the spinner
                            spinner!!.adapter = adapter

                        }else {
                            getCustomerType()
                            Log.e("CustomerType",CustomerType1.toString())
                        }

                    })

                        pDialog.dismiss()
                }

        },Response.ErrorListener { pDialog.dismiss() })
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

    private fun CustomerList() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(false)
        pDialog.show()

        //We Create Json object to send request for server

        val requestContainer=JSONObject()
        val ContainerObject=JSONObject()
        ContainerObject.put("requestContainer",requestContainer.put("appVersion",AppPreferences.AppVersion))
        ContainerObject.put("requestContainer",requestContainer.put("deviceID", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)))
        ContainerObject.put("requestContainer", requestContainer.put("deviceVersion", AppPreferences.versionRelease))
        ContainerObject.put("requestContainer", requestContainer.put("deviceName", AppPreferences.DeviceName))
        ContainerObject.put("requestContainer",requestContainer.put("accessToken",accessToken))
        ContainerObject.put("requestContainer",requestContainer.put("userAutoId",empautoid))
        // Send request Queue in vally
        val queue=Volley.newRequestQueue(this)

        // Request a string response from the provided URL.
        val JsonObjectRequest=JsonObjectRequest(Request.Method.POST,AppPreferences.Customer_ListUrl,ContainerObject,
            {response ->
                val Response = (response.toString())
                val responseResultData = JSONObject(Response.toString())
                val ResponseResult=JSONObject(responseResultData.getString("d"))
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
                  //    Log.e("DueBalance",twoDigitValue.)
                        ModelClassCustomer(AI,CN,CId,CT,DueBalances)
                    }
                    pDialog.dismiss()
                }else{
                    Toast.makeText(this,ResponseMessage.toString(),Toast.LENGTH_LONG).show()
                    pDialog.dismiss()
                }

        }, Response.ErrorListener { pDialog.dismiss() })
        JsonObjectRequest.retryPolicy=DefaultRetryPolicy(
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

    private fun ModelClassCustomer(AI:Int,CN: String, CId: String,CT:String,DueBalances:Float) {
        var ModelClassCustomer1 = ModelClassCustomerList(AI,CN, CId,CT,DueBalances)

        ModelClassCustomer.add(ModelClassCustomer1)
        val recyclerview = findViewById<RecyclerView>(R.id.CustomerView)
        CustomerAdapter = AdapterClassCustomerList(ModelClassCustomer, this)
        recyclerview.adapter = CustomerAdapter
        // DetailsAdapter.notifyDataSetChanged()
    }

    private  fun DilogCustom(){
        var dilog=Dialog(this@CustomerListActivity)
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
        })
        dilog.show()
        dilog.getWindow()!!.setAttributes(lp);
    }
}




