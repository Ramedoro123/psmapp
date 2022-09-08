package com.example.whm.ui.Sales_Person

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.*
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
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.CustmerTypeModel
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassCustomerList
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
class CustomerListActivity : AppCompatActivity() {

    var ModelClassCustomer: ArrayList<ModelClassCustomerList> = arrayListOf()
    var CustmerTypeModelList: ArrayList<CustmerTypeModel> = arrayListOf()
    lateinit var CustomerAdapter: AdapterClassCustomerList
    var accessToken: String? = null
    var empautoid: String? = null
    var CustomerlistSize: String? = null
    var autoId: Int? = null
    @Inject
   lateinit var spinner:Spinner
   lateinit var CustomerID:EditText
   lateinit var CustomerName:EditText
   lateinit var SearchCustomer1:TextView

    var ResponseResult=JSONObject()
    var responseData=JSONArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list)
        var backbtn=findViewById<TextView>(R.id.btnBackarrow)
           SearchCustomer1=findViewById<TextView>(R.id.SearchCustomer)
        var CustomerTitle=findViewById<TextView>(R.id.CustomerName)
        backbtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,MainActivity2::class.java))
            finish()
        })
        SearchCustomer1.setOnClickListener(View.OnClickListener {
        if (CustmerTypeModelList.isEmpty())
        {
            DilogCustom()
            if (AppPreferences.internetConnectionCheck(this)) {
                getCustomerType()
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
        else {
            DilogCustom()
            SpinnerValue()
        }
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
            CustomerList("","")
            CustmerTypeModelList.clear()
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
        Log.e("deviceID",Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID).toString())
        Log.e("deviceVersion",AppPreferences.versionRelease.toString())
        Log.e("deviceName",AppPreferences.DeviceName.toString())
        // send request Queue in vally
        val queue=Volley.newRequestQueue(this)
        val JsonObjectRequest=JsonObjectRequest(Request.Method.POST,AppPreferences.getCustomerType,ContainerObject,
            { response->
                val Response = (response.toString())
                 var responseResultData = JSONObject(Response.toString())
                ResponseResult=JSONObject(responseResultData.getString("d"))
                val ResponseMessage=ResponseResult.getString("responseMessage")
                val responseCode=ResponseResult.getString("responseCode")
                if (responseCode=="201")
                {
                     responseData=ResponseResult.getJSONArray("responseData")
                      for (i in 0 until responseData.length())
                      {
                      var CustomerType=responseData.getJSONObject(i).getString("CustomerType")
                       autoId=responseData.getJSONObject(i).getInt("autoId")
                        CustomerTypeFunction(CustomerType, autoId!!)
                      }
                      SpinnerValue()
                        pDialog.dismiss()
                }else{

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
    private fun CustomerTypeFunction(CustomerType:String,autoId:Int) {
        var customerdata=CustmerTypeModel(CustomerType,autoId)
        CustmerTypeModelList.add(customerdata)

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
        CustomerID=dilog.findViewById<EditText>(R.id.CustomerID)
        CustomerName=dilog.findViewById<EditText>(R.id.CustomerName)
        var btnSearch=dilog.findViewById<Button>(R.id.btnSearch)

        var btnCancle=dilog.findViewById<TextView>(R.id.CancleBtn)
        spinner =dilog.findViewById<View>(R.id.Spinnervalue) as Spinner
        btnCancle.setOnClickListener(View.OnClickListener {
            dilog.dismiss()
        })
        btnSearch.setOnClickListener(View.OnClickListener {
            ModelClassCustomer.clear()
            CustomerList(customerId = CustomerID.text.toString(), customerName = CustomerName.text.toString())
            dilog.dismiss()
        })
        dilog.show()
        dilog.getWindow()!!.setAttributes(lp);
    }

    private fun CustomerList(customerId: String, customerName: String) {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(false)
        pDialog.show()
        //We Create Json object to send request for server
        val requestContainer=JSONObject()
        val searchcustomer=JSONObject()
        val ContainerObject=JSONObject()
        ContainerObject.put("requestContainer",requestContainer.put("appVersion",AppPreferences.AppVersion))
        ContainerObject.put("requestContainer",requestContainer.put("deviceID", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)))
        ContainerObject.put("requestContainer", requestContainer.put("deviceVersion", AppPreferences.versionRelease))
        ContainerObject.put("requestContainer", requestContainer.put("deviceName", AppPreferences.DeviceName))
        ContainerObject.put("requestContainer",requestContainer.put("accessToken",accessToken))
        ContainerObject.put("requestContainer",requestContainer.put("userAutoId",empautoid))

        ContainerObject.put("searchcustomer", searchcustomer.put("customerId",customerId ))
        ContainerObject.put("searchcustomer", searchcustomer.put("customerName",customerName ))
        ContainerObject.put("searchcustomer", searchcustomer.put("customerType",autoId ))
        Log.e("customerType",autoId.toString())
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
                ModelClassCustomer.clear()
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
                }else
                {

                   var popUp=SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                    popUp.setContentText(ResponseMessage)
                    popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                    popUp.setConfirmClickListener()
                    {
                            sDialog -> sDialog.dismissWithAnimation()
                            CustomerList("","",)
                    }
                    popUp.show()
                    popUp.setCanceledOnTouchOutside(false)
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

    private fun SpinnerValue(){

        Log.e("ResponseResult", responseData.toString())
        val adapter: ArrayAdapter<String?> = object :
            ArrayAdapter<String?>(this, android.R.layout.simple_spinner_dropdown_item) {
            override fun getView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val v = super.getView(position, convertView, parent)
                if (position == count) {
                    (v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                    (v.findViewById<View>(android.R.id.text1) as TextView).hint = getItem(
                        count
                    ) //"Hint to be displayed"
                }
                return v
            }

            override fun getCount(): Int {
                return super.getCount() - 1 // you dont display last item. It is used as hint.
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        for (i in 0 until responseData.length()) {
            var Customername = responseData.getJSONObject(i).getString("CustomerType")
            Log.e("  adapter.add(Customername)", Customername)
            var Customer = Customername
            // adapter.addAll(Customername)
            adapter.add(Customer)
        }
        adapter.add("All Customer Type")
        spinner.adapter = adapter
        spinner.setSelection(adapter.count) //set the hint the default selection so it appears on launch.
    }
}





