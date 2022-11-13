package com.example.whm.ui.Sales_Person

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
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
import org.json.JSONObject
import java.lang.reflect.Field
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


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
    lateinit var payableAmount: EditText
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
     var Total:Float?=null
     var round:Float?=null
    var adjcment: String? = null
    var MLTax: String? = null
    var WeightTax: String? = null
    var Tax: String? = null
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
        payableAmount = findViewById<EditText>(R.id.payableAmount)
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
            val c: Calendar = Calendar.getInstance()
            val dialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    val _year = year.toString()
                    val _month = if (month + 1 < 10) "0" + (month + 1) else (month + 1).toString()
                    val _date = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
                    val _pickedDate = "$_month-$_date-$_year"
                    Log.e("PickedDate: ", "Date: $_pickedDate") //2019-02-12
                    deliveryDate?.text = _pickedDate
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH))
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
            dialog.show()
        }
        shipingCharge.filters= arrayOf(OrderSummaryActivity.DecimalDigitsInputFilter(6,2))
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        moreOption.setOnClickListener(View.OnClickListener {
            val popupMenu = PopupMenu(this, it)
            val inflater: MenuInflater = popupMenu.getMenuInflater()
            inflater.inflate(R.menu.more_optionmenu, popupMenu.getMenu())
            var ChangeBillNo = popupMenu.menu?.findItem(R.id.submitOrder)
            var deleteOrderBtn = popupMenu.menu?.findItem(R.id.deleteOrderBtn)
//              popup Menu.menu.setGroupDividerEnabled(true)
            if (ChangeBillNo != null) {
                ChangeBillNo.isVisible = true
            }
            if (deleteOrderBtn != null) {
                deleteOrderBtn.isVisible = true
            }
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.submitOrder -> {
                    OrderSubmitFunction()
                        true
                    }
                    R.id.deleteOrderBtn -> {
//                 Toast.makeText(this,"hello",Toast.LENGTH_LONG).show()
                        deleteDraftOrder()
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
                var shipingCharges = shipingCharge.text.toString()
                var shipingChargesvalue:Float?=null
                val tmps=SubTotal.toString()
                var tmps1=Total
                if (!tmp.isEmpty() && uahEdited && tmp != "." && tmps != null && tmps != "."&&tmps1!=null&&tmps1!=0.0f) {
                    uah = tmp.toFloat()
                    val minprice = tmps.toFloat()
                    if (minprice>=uah) {
                        usd = uah * 100 / minprice
                        overAllDisP.setText("%.2f".format(usd))
                    }
                    else{
                        overAllDisAmount.text.replace(0, overAllDisAmount.text.length, "0.00")
                        Toast.makeText(this@OrderSummaryActivity,"Percent Amount should not be greater than Sub Total Amount.",Toast.LENGTH_LONG).show()
                    }
                    if (shipingCharges!=""&&shipingCharges!=null){
                        shipingChargesvalue=shipingCharges.toFloat()
                    }
                    else{
                        shipingChargesvalue=0.0f
                    }
                            var tmp1 = tmp.toFloat()
                            var grandTotal = (tmps1+shipingChargesvalue)-(tmp1)
                            var round1 = grandTotal.roundToInt().toFloat()
                            var adjs = round1!!-grandTotal!!
                            adjcmentV.setText("%.2f".format(adjs.toDouble()))
                            payableAmount.setText("%.2f".format(round1))

                }
                else if (tmp.isEmpty()) {
                    var shipvalue:Float?=null
                    if (shipingCharges!=null&&shipingCharges!="")
                    {
                        shipvalue=shipingCharges.toFloat()
                    }else{
                        shipvalue=0.0f
                    }
                    var tem3 = 0.00
                    var temp3=tmps1!!.toFloat()
                    var gangs = temp3+tem3+ shipvalue!!
                    var round1 = gangs.roundToInt().toFloat()
                    var adjs = round1!!-gangs!!
                    adjcmentV.setText("%.2f".format(adjs.toDouble()))
                    payableAmount.setText("%.2f".format(round1))
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
                var shipingCharges=shipingCharge.text.toString()
                var shipingChargesvalue:Float?=null
                val tmps = SubTotal.toString()

                var tmps1=Total
                if (!tmp.isEmpty() && usdEdited && tmp != "." && tmps != null && tmps != "" && tmps != "." && tmp != "0"&&tmps1!=null&&tmps1!=0.0f) {
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
                      if (shipingCharges!=""&&shipingCharges!=null){
                          shipingChargesvalue=shipingCharges.toFloat()
                      }
                      else{
                          shipingChargesvalue=0.0f
                      }
                    var tmp1 = uah.toFloat()
                    var grandTotal = (tmps1+shipingChargesvalue!!)-(tmp1)
                    var round1 = grandTotal.roundToInt().toFloat()
                    var adjs = round1!!-grandTotal!!
                    adjcmentV.setText("%.2f".format(adjs.toDouble()))
                    payableAmount.setText("%.2f".format(round1))



                } else if (tmp.isEmpty()) {
                    var shipvalue:Float?=null
                    if (shipingCharges!=null&&shipingCharges!="")
                    {
                        shipvalue=shipingCharges.toFloat()
                    }else{
                        shipvalue=0.0f
                    }
                    var tem3 = 0.00
                    var temp3=tmps1!!.toFloat()
                    var gangs = temp3+tem3+shipvalue!!
                    var round1 = gangs.roundToInt().toFloat()
                    var adjs = round1!!-gangs!!
                    adjcmentV.setText("%.2f".format(adjs.toDouble()))
                    payableAmount.setText("%.2f".format(round1))
                    overAllDisAmount.text.clear()
                }
            }

            override fun afterTextChanged(s: Editable) {
                usdEdited = false
            }
        })

        shipingCharge.addTextChangedListener(object : TextWatcher {
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
                var shiping = shipingCharge.text.toString()
                val DisAmount = overAllDisAmount.text.toString()
                var tmps = Total
                var DisAmount1:Float?=null
                if (!shiping.isEmpty() && usdEdited && shiping.trim() != ""&& tmps != null) {
                    if (DisAmount!=""&&DisAmount!=null)
                    {
                        DisAmount1=DisAmount.toFloat()
                    }else{
                        DisAmount1=0.0f
                    }
                    var shiping1=shiping.toFloat()
                    var tmp2=tmps.toFloat()
                    var total=(tmp2+shiping1)-(DisAmount1!!)
                    var round1=total.roundToInt().toFloat()
                    var adjs=round1!!-total!!
                    adjcmentV.setText("%.2f".format(adjs.toDouble()))
                    payableAmount.setText("%.2f".format(round1.toFloat()))
                }
                else if (shiping.isEmpty()) {
                    var tem3 = 0.00
                    var temp3=tmps!!.toFloat()
                    var DisAmount2:Float?=null
                    if (DisAmount!=""&&DisAmount!=null) {
                        DisAmount2 = DisAmount!!.toFloat()
                    }
                    else{
                        DisAmount2=0.0f
                    }
                    var gangs = temp3+tem3-DisAmount2!!
                    payableAmount.setText("%.2f".format(gangs.roundToInt().toFloat()))
                    shipingCharge.text.clear()
                }
            }

            override fun afterTextChanged(s: Editable) {
                usdEdited = false
            }
        })

        OrderSummaryFunction()
    }

    private fun deleteDraftOrder() {
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
            if (draftAutoId != 0) {
                sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
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
                            startActivity(Intent(this,MainActivity2::class.java))
                            finish()
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
            sendRequestObject.put("pObj", pObj.put("Remarks", salsePRemark.text.toString()))
            sendRequestObject.put("pObj", pObj.put("DriverRemarks", driverRemark.text.toString()))
            sendRequestObject.put("pObj", pObj.put("DeliveryDate", deliveryDate.text.toString()))
            var overAllDisP1=overAllDisP.text.toString()
            var shipingCharge1=shipingCharge.text.toString()
            var overAllDisAmount1=overAllDisAmount.text.toString()
            if (overAllDisP1==""||overAllDisAmount1==""||shipingCharge1=="") {
                sendRequestObject.put("pObj", pObj.put("ShippingCharges", 0))
                sendRequestObject.put("pObj", pObj.put("OverallDisc", 0))
                sendRequestObject.put("pObj", pObj.put("OverallDiscAmt", 0))
            }else{
                sendRequestObject.put("pObj", pObj.put("ShippingCharges", shipingCharge1))
                sendRequestObject.put("pObj", pObj.put("OverallDisc", overAllDisP1))
                sendRequestObject.put("pObj", pObj.put("OverallDiscAmt", overAllDisAmount1))
            }
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
                            startActivity(Intent(this,MainActivity2::class.java))
                            finish()
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
            sendRequestObject.put(
                "requestContainer",
                requestContainer.put("userAutoId", empautoid)
            )
            if (draftAutoId != 0) {
                sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
            }

            Log.e("sendRequestObject order summery", sendRequestObject.toString())
            Log.e("AppPreferences.AppVersion", AppPreferences.AppVersion)

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
                             adjcment = responsDataObject.getString("Adj")
                             var MLQty = responsDataObject.getString("MLQty")
                             MLTax = responsDataObject.getString("MLTax")
                             var WeightQty = responsDataObject.getString("WeightQty")
                             WeightTax = responsDataObject.getString("WeightTax")
                            var TaxType = responsDataObject.getString("TaxType")
                            Tax = responsDataObject.getString("Tax")
                            var CD = responsDataObject.getString("CD")
                            var DisAmt = responsDataObject.getString("DisAmt")
                            var  discountP= CD.toFloat()
                            var  disAmt= DisAmt.toFloat()
                            var subTotals=SubTotal?.toFloat()
                            var adjcments=adjcment!!.toFloat()
                            var MLTaxValue=MLTax!!.toFloat()
                            var WeightTaxs=WeightTax!!.toFloat()
                        if(MLQty!=null &&MLQty!="0"&&MLQty!="" &&MLTax!="0"&&MLTax!=""&&MLTax!=null){
                             Log.e("MLQty",MLQty)
                            var MLQtys=MLQty.toFloat()
                            var MLTaxValue=MLTax!!.toFloat()
                            mlTaxLevel.visibility=View.VISIBLE
                            mlTax.visibility=View.VISIBLE
                            mlTaxLevel.setText("ML Tax (QTY : %.2f".format(MLQtys.toDouble()) + ")")
                            mlTax.setText("$ %.2f".format(MLTaxValue.toDouble()))
                        }else{
                            mlTaxLevel.visibility=View.GONE
                            mlTax.visibility=View.GONE
                        }
                        if (WeightQty!=null &&WeightQty!="0" &&WeightQty!=""&&WeightTax!=""&&WeightTax!="0"&&WeightTax!=null)
                        {
                            var WeightQtys=WeightQty.toFloat()
                            var WeightTaxs=WeightTax!!.toFloat()
                            weightTaxLevel.visibility=View.VISIBLE
                            weightTax.visibility=View.VISIBLE
                            weightTaxLevel.setText("Weight Tax (QTY : %.2f".format(WeightQtys.toDouble()) + ")")

                            weightTax.setText("$ %.2f".format(WeightTaxs.toDouble()))
                        }
                        else{
                            weightTaxLevel.visibility=View.GONE
                            weightTax.visibility=View.GONE
                        }
                       if (Tax!="0"&&Tax!=""&&Tax!=null)
                        {
                            var totalTaxs=Tax!!.toFloat()
                            totalTax.visibility=View.VISIBLE
                            textView8.visibility=View.VISIBLE
                            totalTax.setText("%.2f".format(totalTaxs.toDouble()))
                        }
                        else{
                            totalTax.visibility=View.GONE
                            textView8.visibility=View.GONE
                        }
                            billingA.setText(billingsA)
                            shippingA.setText(shippingsA)
                            adjcmentV.setText("%.2f".format(adjcments.toDouble()))
                            subTotal.setText("%.2f".format(subTotals?.toDouble()))
                            deliveryDate.setText(deliveryDates)
                            taxType.setText(TaxType)
                            overAllDisP.setText("%.2f".format(discountP.toDouble()))
                            overAllDisAmount.setText("%.2f".format(disAmt.toDouble()))
                            var disAmount:Float?=null
                            var adjsment:Float?=null
                            var overAllDisAmount1= overAllDisAmount.text.trim().toString()
                            var adjcmentV1= adjcmentV.text.trim().toString()
                            if (overAllDisAmount1!="" &&overAllDisAmount1!=null&&adjcmentV1!=null&&adjcmentV1!="")
                            {
                                 disAmount=overAllDisAmount1.toFloat()
                                 adjsment =adjcmentV1.toFloat()
                            }
                          if (subTotals!=null&&Tax!=""&&Tax!=null)
                          {         var totalTaxs=Tax!!.toFloat()
                                   Total=(subTotals+WeightTaxs+totalTaxs+MLTaxValue)
                                    var grandTotal=Total!!-disAmount!!
                                   round=grandTotal!!.roundToInt().toFloat()
                                   var adjs=round!!-grandTotal!!
                                  adjcmentV.setText("%.2f".format(adjs.toDouble()))
                                  payableAmount.setText("%.2f".format(round))
                          }
//                        salsePRemark
//                        driverRemark
                        }

//                        var popUp = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
//                        popUp.setContentText(responseMessage2.toString())
//                        popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
//                        popUp.setConfirmClickListener()
//                        { sDialog ->
//                            sDialog.dismissWithAnimation()
//                            popUp.dismiss()
//                            pDialog.dismiss()
//                        }
//                        popUp.show()
//                        popUp.setCanceledOnTouchOutside(false)
//                        popUp.setCancelable(false)
                        pDialog.setCancelable(false)
                        pDialog.dismiss()
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
    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {
        //                                             digitsBeforeZero  or       digitsBeforeZero + dot + digitsAfterZero
        private val pattern =
            Pattern.compile("(\\d{0,$digitsBeforeZero})|(\\d{0,$digitsBeforeZero}\\.\\d{0,$digitsAfterZero})")

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int,
        ): CharSequence? {
            return if (source.isEmpty()) {
                // When the source text is empty, we need to remove characters and check the result
                if (pattern.matcher(dest.removeRange(dstart, dend)).matches()) {
                    // No changes to source
                    null
                } else {
                    // Don't delete characters, return the old subsequence
                    dest.subSequence(dstart, dend)
                }
            } else {
                // Check the result
                if (pattern.matcher(dest.replaceRange(dstart, dend, source)).matches()) {
                    // No changes to source
                    null
                } else {
                    // Return nothing
                    ""
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        var ChangeBillNo: MenuItem? = null
        var deleteOrderBtn: MenuItem? = null
        inflater.inflate(R.menu.menuitem, menu)
        ChangeBillNo = menu?.findItem(R.id.submitOrder)
        deleteOrderBtn = menu?.findItem(R.id.deleteOrderBtn)
        if (ChangeBillNo != null) {
            ChangeBillNo.isVisible = true
        }
        if (deleteOrderBtn != null) {
            deleteOrderBtn.isVisible = true
        }
        return super.onCreateOptionsMenu(menu)
    }
}

