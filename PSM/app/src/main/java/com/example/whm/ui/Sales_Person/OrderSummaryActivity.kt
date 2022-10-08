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
import android.view.WindowManager
import android.widget.*
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.MainActivity2
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.getCartdetailsModle
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.getShippingTypeDataModel
import com.example.myapplication.com.example.whm.ui.inventoryreceive.DatePickerFragment
import org.json.JSONObject
import java.lang.reflect.Field

class OrderSummaryActivity : AppCompatActivity() {
    var shippingTypeList = ArrayList<String>()
    var AutoID1 = ArrayList<Int>()
    var scrollView2:ScrollView?=null
    var spinner4:Spinner?=null
    var AutoID:Int?=null
    var EnabledTax:Int?=null
    var shippingType:String?=null
    var getShipType: MutableList<getShippingTypeDataModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)
        var btnBackOrderSummary8=findViewById<TextView>(R.id.btnBackOrderSummaryDetails)
        var deliveryDate=findViewById<TextView>(R.id.deliveryDate)
         scrollView2=findViewById<ScrollView>(R.id.scrollView2)
        spinner4=findViewById<Spinner>(R.id.spinner4)
        this.scrollView2!!.visibility=View.GONE
              getShippingType()
        btnBackOrderSummary8.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,CartViewActicity::class.java))
            finish()
        })

        deliveryDate?.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = this.supportFragmentManager
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                this
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    deliveryDate?.text = date
                    deliveryDate!!.requestFocus()
                    //  spvendor!!.requestFocus()
                }

            }
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            datePickerFragment.enterTransition

        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    private fun getShippingType()
    {
        if (AppPreferences.internetConnectionCheck(this)){
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            var accessToken = preferences.getString("accessToken", "")
            var empautoid = preferences.getString("EmpAutoId", "")
            var customerId = preferences.getString("customerId", "")
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
                "requestContainer",
                requestContainer.put(
                    "deviceID",
                    Settings.Secure.getString(this!!.contentResolver, Settings.Secure.ANDROID_ID)
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
            //send request queue in vally
            val queue = Volley.newRequestQueue(this)
            val JsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                AppPreferences.getShippingType, sendRequestObject,
                { response ->
                    val responseResult = JSONObject(response.toString())
                    val responsedData = JSONObject(responseResult.getString("d"))
                    var responseMessage = responsedData.getString("responseMessage")
                    val responseCode = responsedData.getString("responseCode")
                    if (responseCode == "201") {
                       var responseResultData = responsedData.getJSONArray("responseData")
                        // array.clear()
                        if (responseResultData.length() != null && responseResultData.length() > 0) {

                            for (i in 0 until responseResultData.length()) {
                                AutoID = responseResultData.getJSONObject(i).getInt("AutoID")
                                shippingType = responseResultData.getJSONObject(i).getString("shippingType")
                                EnabledTax = responseResultData.getJSONObject(i).getInt("EnabledTax")
                                var cartdata = getShippingTypeDataModel(
                                    AutoID!!,
                                    shippingType!!,
                                    EnabledTax!!
                                )
                                getShipType.add(cartdata)
                            }
                            Log.e("AutoID",AutoID.toString())
                            this.scrollView2!!.visibility=View.VISIBLE

                            for (n in 0..getShipType.size - 1) {

                                var shippingType = getShipType[n].getshippingType()
                                var AutoID = getShipType[n].getAutoID()
                                shippingTypeList.add(shippingType.toString())
                                AutoID1.add(AutoID!!.toInt())
                            }
                            var adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, shippingTypeList)
                            spinner4!!.adapter = adapter
                            for (i in 0..AutoID1.size - 1){
                                Log.e("isdefault", AutoID1[i].toString())
                                var number = AutoID1[i]
                                if (number == 1) {
                                    spinner4!!.setSelection(i);
                                } else {

                                }
                            }
                            spinner4!!.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val item2: String = shippingTypeList[position]
                                    }
                                }
                            listDropdown(spinner4!!)
                            pDialog.dismiss()
                        }

                        else {
                            warningMessage(message =responseMessage.toString() )
                            pDialog!!.dismiss()
                        }
                    }
                    else {
                        warningMessage(responseMessage.toString())
                        pDialog!!.dismiss()
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

    private fun warningMessage(message:String) {
        var popUp = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        popUp.setContentText(message)
        popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
        popUp.setConfirmClickListener()
        { sDialog ->
            sDialog.dismissWithAnimation()
            popUp.dismiss()
        }
        popUp.show()
        popUp.setCanceledOnTouchOutside(false)
    }

    private fun listDropdown(spineer: Spinner) {
        val popup: Field = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val popupWindow: ListPopupWindow = popup.get(spineer) as ListPopupWindow
        popupWindow.height = (200).toInt()
    }
}