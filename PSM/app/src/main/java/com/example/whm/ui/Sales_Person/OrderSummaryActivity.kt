package com.example.whm.ui.Sales_Person

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.MainActivity2
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.getShippingTypeDataModel
import com.example.myapplication.com.example.whm.ui.inventoryreceive.DatePickerFragment
import org.json.JSONObject
import java.lang.reflect.Field


class OrderSummaryActivity : AppCompatActivity() {
    lateinit var billingA: TextView
    lateinit var shippingA: TextView
    lateinit var subTotal: TextView
    lateinit var weightTax: TextView
    lateinit var totalTax: TextView
    lateinit var weightTaxLevel: TextView
    lateinit var mlTaxLevel: TextView
    lateinit var textView8: TextView
    lateinit var taxType: TextView
    lateinit var mlTax: TextView
    lateinit var deliveryDate: TextView
    lateinit var overAllDisP: EditText
    lateinit var overAllDisAmount: EditText
    lateinit var shipingCharge: EditText
    lateinit var payableAmount: TextView
    lateinit var adjcmentV: EditText
    lateinit var salsePRemark: EditText
    lateinit var driverRemark: EditText
    var shippingTypeList = ArrayList<String>()
    var AutoID1 = ArrayList<Int>()
    var scrollView2: ScrollView? = null
    var spinner4: Spinner? = null
    var AutoID: Int? = null
    var Autoid: Int? = null
    var draftAutoId: Int? = null
    var EnabledTax: Int? = null
    var shippingType: String? = null
    var SubTotal: String? = null
    var uah: Float = 0.0F
    var usd: Float = 0.0F
    var uahEdited = false
    var usdEdited = false
    var getShipType: MutableList<getShippingTypeDataModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)
        var btnBackOrderSummary8 = findViewById<TextView>(R.id.btnBackOrderSummaryDetails)
      deliveryDate = findViewById<TextView>(R.id.deliveryDate)
        billingA = findViewById<TextView>(R.id.billingA)
        shippingA = findViewById<TextView>(R.id.shippingA)
        subTotal = findViewById<TextView>(R.id.subTotal)
        weightTax = findViewById<TextView>(R.id.weightTax)
        totalTax = findViewById<TextView>(R.id.totalTax)
        mlTaxLevel = findViewById<TextView>(R.id.mlTaxLevel)
        taxType = findViewById<TextView>(R.id.taxType)
        textView8 = findViewById<TextView>(R.id.textView8)
        mlTax = findViewById<TextView>(R.id.mlTax)
        weightTaxLevel = findViewById<TextView>(R.id.weightTaxLevel)
        overAllDisP = findViewById<EditText>(R.id.overAllDisP)
        overAllDisAmount = findViewById<EditText>(R.id.overAllDisAmount)
        shipingCharge = findViewById<EditText>(R.id.shipingCharge)
        payableAmount = findViewById<TextView>(R.id.payableAmount)
        adjcmentV = findViewById<EditText>(R.id.adjcmentV)
        salsePRemark = findViewById<EditText>(R.id.salsePRemark)
        driverRemark = findViewById<EditText>(R.id.driverRemark)
        var moreOption = findViewById<ImageView>(R.id.moreOption)
        scrollView2 = findViewById<ScrollView>(R.id.scrollView2)
        spinner4 = findViewById<Spinner>(R.id.spinner4)
        this.scrollView2!!.visibility = View.GONE
        mlTaxLevel.visibility=View.GONE
        weightTaxLevel.visibility=View.GONE
        weightTax.visibility=View.GONE
        mlTax.visibility=View.GONE
        getShippingType()
        draftAutoId = intent.getIntExtra("draftAutoId", 0)
        btnBackOrderSummary8.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CartViewActicity::class.java))
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
        moreOption.setOnClickListener(View.OnClickListener {
            val popupMenu = PopupMenu(this, it)
            val inflater: MenuInflater = popupMenu.getMenuInflater()
            inflater.inflate(R.menu.more_optionmenu, popupMenu.getMenu())
            var ChangeBillNo = popupMenu.menu?.findItem(R.id.submitOrder)
            if (ChangeBillNo != null) {
                ChangeBillNo.isVisible = true
            }
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.submitOrder -> {
                    OrderSubmitFunction()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.more_optionmenu)
            popupMenu.show()
        })

        overAllDisAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int,
            ) {
                if (!usdEdited) {
                    uahEdited = true

                }
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val tmp = overAllDisAmount.text.toString()
                val tmps = SubTotal.toString()
                if (!tmp.isEmpty() && uahEdited && tmp != "." && tmps != null && tmps != ".") {
                    uah = tmp.toFloat()
                    val minprice = tmps.toFloat()
                    if (minprice>=uah) {
                        usd = uah * 100 / minprice
                        overAllDisP.setText("%.2f".format(usd))
                    }
                    else {
                        overAllDisAmount.text.replace(0, overAllDisAmount.text.length, "0.00")
                        Toast.makeText(this@OrderSummaryActivity,"Percent Amount should not be greater than Sub Total Amount.",Toast.LENGTH_LONG).show()
                    }

                } else if (tmp.isEmpty()) {
                    overAllDisP.text.clear()
                }
            }

            override fun afterTextChanged(s: Editable) {
                uahEdited = false

            }
        })
        overAllDisP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int,
            ) {
                if (!uahEdited) {
                    usdEdited = true

                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val tmp = overAllDisP.text.toString()
                val tmps = SubTotal.toString()
                if (!tmp.isEmpty() && usdEdited && tmp != "." && tmps != null && tmps != "" && tmps != "." && tmp != "0") {
                    usd = tmp.toFloat()
                    val price = tmps.toFloat()
                        uah = usd * price / 100
                    val minprice = SubTotal.toString().toFloat()
//                    if (usd>100){
//                        overAllDisP.text.replace(0, overAllDisP.text.length, "0.00")
//                        Toast.makeText(this@OrderSummaryActivity,"",Toast.LENGTH_LONG).show()
//                    }
                    if (minprice>=uah){
                        overAllDisAmount.setText("%.2f".format(uah))
                    }
                    else {
                        overAllDisP.text.replace(0, overAllDisP.text.length, "0.00")
                        Toast.makeText(this@OrderSummaryActivity,"Percent Amount should not be greater than Sub Total Amount.",Toast.LENGTH_LONG).show()
                    }
                } else if (tmp.isEmpty()) {
                    overAllDisAmount.text.clear()
                }
            }

            override fun afterTextChanged(s: Editable) {
                usdEdited = false
            }
        })

        OrderSummaryFunction()
    }

    private fun OrderSubmitFunction() {
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

            sendRequestObject.put("pObj", pObj.put("ShippingType", Autoid))
            sendRequestObject.put("pObj", pObj.put("ShippingCharges", shipingCharge.text.toString()))
            sendRequestObject.put("pObj", pObj.put("Remarks", salsePRemark.text.toString()))
            sendRequestObject.put("pObj", pObj.put("DriverRemarks", driverRemark.text.toString()))
            sendRequestObject.put("pObj", pObj.put("totalTax", totalTax.text.toString()))
            sendRequestObject.put("pObj", pObj.put("deliveryDate", deliveryDate.text.toString()))
            if (draftAutoId != 0) {
                sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
            }

            Log.e("sendRequestObject order ordersubmit", sendRequestObject.toString())

            //send request queue in vally
            val queue = Volley.newRequestQueue(this)
            val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
                AppPreferences.ordersubmit, sendRequestObject,
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

    private fun OrderSummaryFunction() {
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
            sendRequestObject.put(
                "requestContainer",
                requestContainer.put("userAutoId", empautoid)
            )
            if (draftAutoId != 0) {
                sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
            }

            Log.e("sendRequestObject order summery", sendRequestObject.toString())

            //send request queue in vally
            val queue = Volley.newRequestQueue(this)
            val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
                AppPreferences.summarydetails, sendRequestObject,
                { response ->
                    val responseResult = JSONObject(response.toString())
                    val responsedData = JSONObject(responseResult.getString("d"))
                    var responseMessage2 = responsedData.getString("responseMessage")
                    val responseStatus = responsedData.getInt("responseStatus")
                    if (responseStatus == 200) {
                        var responsDataObject = JSONObject(responsedData.getString("responseData"))
                        if (responsDataObject!=null) {
                            var billingsA = responsDataObject.getString("BA")
                            var shippingsA = responsDataObject.getString("SA")
                            var deliveryDates = responsDataObject.getString("DeliveryDate")
                             SubTotal = responsDataObject.getString("SubTotal")
                            var adjcment = responsDataObject.getString("Adj")
                            var MLQty = responsDataObject.getString("MLQty")
                            var MLTax = responsDataObject.getString("MLTax")
                            var WeightQty = responsDataObject.getString("WeightQty")
                            var WeightTax = responsDataObject.getString("WeightTax")
                            var TaxType = responsDataObject.getString("TaxType")
                            var Tax = responsDataObject.getString("Tax")
                            var CD = responsDataObject.getString("CD")
                            var DisAmt = responsDataObject.getString("DisAmt")
                            var  discountP= CD.toFloat()
                            var  disAmt= DisAmt.toFloat()
                            var subTotals=SubTotal?.toFloat()
                            var adjcments=adjcment.toFloat()
                        if(MLQty!=null &&MLQty!="0" &&WeightQty!=null &&WeightQty!="0"
                            &&WeightQty!=""&&MLQty!="" &&MLTax!="0"&&MLTax!=""&&MLTax!=null&&WeightTax!=""&&WeightTax!="0"&&WeightTax!=null){
                             Log.e("MLQty",MLQty)
                            var MLQtys=MLQty.toFloat()
                            var MLTaxValue=MLTax.toFloat()
                            var WeightQtys=WeightQty.toFloat()
                            var WeightTaxs=WeightTax.toFloat()
                            mlTaxLevel.visibility=View.VISIBLE
                            weightTaxLevel.visibility=View.VISIBLE
                            weightTax.visibility=View.VISIBLE
                            mlTax.visibility=View.VISIBLE
                            mlTaxLevel.setText("ML Tax (QTY : %.2f".format(MLQtys.toDouble()) + ")")
                            weightTaxLevel.setText("Weight Tax (QTY : %.2f".format(WeightQtys.toDouble()) + ")")

                            weightTax.setText("$ %.2f".format(WeightTaxs.toDouble()))
                            mlTax.setText("$ %.2f".format(MLTaxValue.toDouble()))
                        }else if (Tax!="0"&&Tax!=""&&Tax!=null)
                        {
                            var totalTaxs=Tax.toFloat()
                            totalTax.visibility=View.VISIBLE
                            textView8.visibility=View.VISIBLE
                            totalTax.setText("%.2f".format(totalTaxs.toDouble()))
                        }
                        else{
                            mlTaxLevel.visibility=View.GONE
                            weightTaxLevel.visibility=View.GONE
                            weightTax.visibility=View.GONE
                            mlTax.visibility=View.GONE
                            totalTax.visibility=View.GONE
                            textView8.visibility=View.GONE
                        }

                            billingA.setText(billingsA)
                            shippingA.setText(shippingsA)
                            subTotal.setText("%.2f".format(subTotals?.toDouble()))
                            deliveryDate.setText(deliveryDates)
                            taxType.setText(TaxType)
                            overAllDisP.setText("%.2f".format(discountP.toDouble()))
                            overAllDisAmount.setText("%.2f".format(disAmt.toDouble()))
                          if (subTotals!=null&& adjcments!=null)
                          {

                                   var Total=(adjcments+subTotals)
                                   payableAmount.setText("%.2f".format(Total.toDouble()))
                          }

                        adjcmentV.setText("%.2f".format(adjcments.toDouble()))
//                        salsePRemark
//                        driverRemark
                        }

                        var popUp = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
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

    private fun getShippingType() {
        if (AppPreferences.internetConnectionCheck(this)) {
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
                                shippingType =
                                    responseResultData.getJSONObject(i).getString("shippingType")
                                EnabledTax =
                                    responseResultData.getJSONObject(i).getInt("EnabledTax")
                                var cartdata = getShippingTypeDataModel(
                                    AutoID!!,
                                    shippingType!!,
                                    EnabledTax!!
                                )
                                getShipType.add(cartdata)
                            }
                            Log.e("AutoID", AutoID.toString())
                            this.scrollView2!!.visibility = View.VISIBLE

                            for (n in 0..getShipType.size - 1) {

                                var shippingType = getShipType[n].getshippingType()
                                var AutoID = getShipType[n].getAutoID()
                                shippingTypeList.add(shippingType.toString())
                                AutoID1.add(AutoID!!.toInt())
                            }
                            var adapter = ArrayAdapter<String>(this,
                                R.layout.support_simple_spinner_dropdown_item, shippingTypeList)
                            spinner4!!.adapter = adapter
                            for (i in 0..AutoID1.size - 1) {
                                Log.e("isdefault", AutoID1[i].toString())
                                var number = AutoID1[i]
                                if (number ==1) {
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
                                        id: Long,
                                    ) {
                                        val item2: String = shippingTypeList[position]
                                         Autoid= AutoID1[position]
                                    }
                                }
                            listDropdown(spinner4!!)
                            pDialog.dismiss()
                        } else {
                            warningMessage(message = responseMessage.toString())
                            pDialog!!.dismiss()
                        }
                    } else {
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

    private fun warningMessage(message: String) {
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
        popUp.setCancelable(false)
    }

    private fun listDropdown(spineer: Spinner) {
        val popup: Field = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val popupWindow: ListPopupWindow = popup.get(spineer) as ListPopupWindow
        popupWindow.height = (200).toInt()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        var ChangeBillNo: MenuItem? = null
        inflater.inflate(R.menu.menuitem, menu)
        ChangeBillNo = menu?.findItem(R.id.submitOrder)
        if (ChangeBillNo != null) {
            ChangeBillNo.isVisible = true
        }
        return super.onCreateOptionsMenu(menu)
    }
}