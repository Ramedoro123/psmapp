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
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.CartListAdapterClass
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.SalesPersonProductAdapterClass
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.CartListModelClass
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.SalesPersonProductModel
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.getCartdetailsModle
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Field

class CartViewActicity : AppCompatActivity(), View.OnClickListener, CartListAdapterClass.OnItemClickListener{

    var  accessToken: String? = null
    var  empautoid: String? = null
    var  draftAutoId: Int?=null
    var  TotalAmount: Float?=null
    var  totalcount: Int?=null
    lateinit var CustomerName:TextView
    var productListModelClass: ArrayList<CartListModelClass> = arrayListOf()
    lateinit var cartListAdapter:CartListAdapterClass
    var itemCounts: Int = 0
    var responseMessage: String? = null
    var pDialog: SweetAlertDialog? = null
    var mylist = ArrayList<String>()
    var mylist1 = ArrayList<String>()
    var minPriceslist = ArrayList<String>()
    var isFreelist = ArrayList<String>()
    var isdefault = ArrayList<Int>()
    var unitAutoIdList = ArrayList<Int>()
    var Total: String? = null
    var UName: String? = null
    var price: String? = null
    var minPrice: String? = null
    var pricess: String? = null
    var isFrees: String? = null
    var isFree: String? = null
    var MinPric: String? = null
    var minPrices: String? = null
    var OQty: Int? = null
    var UnitPrice: Float? = null
    var UnitType: String? = null
    var Tax: Int? = null
    var NofItem: String? = null
    var UIDs: Int? = null
    var frees: Int? = null
    var unitAutoID: Int? = null
    var checkFreeValue: Int = 0
    var isExchangeValue: Int = 0
    var isdefault1: Int? = null
    var unitAutoidValue: Int? = null
    var removedPosition: Int? = null
    var priceValue: Double? = null
    var uah: Float = 0.0F
    var usd: Float = 0.0F
    var uahEdited = false
    var usdEdited = false
    var getcartDetailsdata: MutableList<getCartdetailsModle> = ArrayList()
    var cartResponseResultData = JSONArray()
    lateinit var spineer: Spinner
    lateinit var valueIncrementDecrement: EditText
    lateinit var decrementBtn: Button
    lateinit var incrementBtn: Button

    lateinit var Price: EditText
    lateinit var isFreeCheckBox: CheckBox
    lateinit var isExchangeCheckBox: CheckBox
    lateinit var discountAmount: EditText
    lateinit var discountPercent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_view_acticity)
        var btnBackCart=findViewById<TextView>(R.id.btnBackCart)
        var OrderSummary=findViewById<TextView>(R.id.OrderSummary)
         CustomerName=findViewById<TextView>(R.id.CustomerName)
        var CustomerInformation=findViewById<TextView>(R.id.CustomerInformation)
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
           var TotalAmounts =preferences.getString("TotalPrice","0.00")
           var totalcounts = preferences.getString("itemCounts", "")
        if (draft=="" || draft==null  ||TotalAmounts==null||TotalAmounts==" " ||totalcounts==null || totalcounts==""){
             draftAutoId=0
        }else if (draft!="" || draft!=null  ||TotalAmounts!=null||TotalAmounts!=" " ||totalcounts!=null || totalcounts!=""){
            draftAutoId=draft.toString().toInt()
//            TotalAmount=TotalAmounts.toString().toFloat()
//            totalcount=totalcounts.toString().toInt()
        }
        Log.e("TotalAmount 13",TotalAmount.toString())
        Log.e("totalcount 13",totalcount.toString())
        btnBackCart.setOnClickListener(View.OnClickListener {
            var intent=Intent(this@CartViewActicity,SalesPersonProductList::class.java)
            intent.putExtra("draftAutoId",draftAutoId)
            intent.putExtra("TotalAmount",TotalAmount)
            intent.putExtra("totalcount",totalcount)

            Log.e("draftAutoId", draftAutoId.toString())
            startActivity(intent)
            finish()
        })
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
                            var PId = responseData.getJSONObject(i).getString("PId")
                            var PName = responseData.getJSONObject(i).getString("PName")
                            var UnitType = responseData.getJSONObject(i).getString("UnitType")
                            var QtyPerUnit = responseData.getJSONObject(i).getInt("QtyPerUnit")
                            var UnitPrices = responseData.getJSONObject(i).getDouble("UnitPrice")
                            var ReqQty = responseData.getJSONObject(i).getInt("ReqQty")
                            var NetPrices = responseData.getJSONObject(i).getDouble("NetPrice")
                            var Free = responseData.getJSONObject(i).getInt("Free")
                            var Exchange = responseData.getJSONObject(i).getInt("Exchange")
                            var Tax = responseData.getJSONObject(i).getInt("Tax")
                            var UnitAutoId = responseData.getJSONObject(i).getInt("UnitAutoId")
                            var ImgPath = responseData.getJSONObject(i).getString("ImgPath")
                            var NetPrice:Float=NetPrices.toFloat()
                            var UnitPrice:Float=UnitPrices.toFloat()
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
                                ImgPath,
                                0,
                                "",
                                "",
                                0,
                            )
                        }
                        var totalSize = productListModelClass.size
                        CustomerName.setText("Cart(" + totalSize + ")").toString()
                        Log.e("getCartListDetails",productListModelClass.size.toString())
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



    override fun OnItemsClick(position: Int) {
        val ClickedItem:CartListModelClass=productListModelClass[position]
        if (AppPreferences.internetConnectionCheck(this@CartViewActicity)) {
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
            sendRequestObject.put("pObj", pObj.put("productId", ClickedItem.getPId()))
            sendRequestObject.put("pObj", pObj.put("customerId", customerId))
            //send request queue in vally
            val queue = Volley.newRequestQueue(this)
            val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
                AppPreferences.getpackingdetails, sendRequestObject,
                { response ->
                    val responseResult = JSONObject(response.toString())
                    val responsedData = JSONObject(responseResult.getString("d"))
                    responseMessage = responsedData.getString("responseMessage")
                    val responseCode = responsedData.getString("responseCode")
                    if (responseCode == "201") {
                        var responseResultData = responsedData.getJSONArray("responseData")
                        // array.clear()
                        getcartDetailsdata.clear()
                        if (responseResultData.length() != null && responseResultData.length() > 0) {
                            for (i in 0 until responseResultData.length()) {
                                UIDs = responseResultData.getJSONObject(i).getInt("UID")
                                UName = responseResultData.getJSONObject(i).getString("UName")
                                frees = responseResultData.getJSONObject(i).getInt("Free")
                                price = responseResultData.getJSONObject(i).getString("price")
                                MinPric = responseResultData.getJSONObject(i).getString("MinPrice")
                                isdefault1 = responseResultData.getJSONObject(i).getInt("isdefault")
                                var cartdata = getCartdetailsModle(
                                    UIDs!!,
                                    UName!!,
                                    frees!!,
                                    price!!,
                                    MinPric!!,
                                    isdefault1!!
                                )
                                getcartDetailsdata.add(cartdata)

                            }
                            mylist.clear()
                            mylist1.clear()
                            isdefault.clear()
                            minPriceslist.clear()
                            isFreelist.clear()
                            unitAutoIdList.clear()
                            for (n in 0..getcartDetailsdata.size - 1) {
                                pricess = getcartDetailsdata[n].getprice()
                                minPrices = getcartDetailsdata[n].getMinPric()
                                isFree = getcartDetailsdata[n].getfrees().toString()
                                unitAutoID = getcartDetailsdata[n].getuiDs()
                                unitAutoID?.let { it1 -> unitAutoIdList.add(it1) }
                                isFreelist.add(isFree.toString())
                                mylist1.add(pricess.toString())
                                minPriceslist.add(minPrices.toString())
                                var isdefault1 = getcartDetailsdata[n].getisdefault()
                                isdefault.add(isdefault1!!.toInt())
                                var UName = getcartDetailsdata[n].getuName()
                                mylist.add(UName.toString())
                            }

                            var adapter = ArrayAdapter<String>(this,
                                R.layout.support_simple_spinner_dropdown_item,
                                mylist)
                            spineer.adapter = adapter
                            for (i in 0..isdefault.size - 1) {
                                Log.e("isdefault", isdefault[i].toString())
                                var number = isdefault[i]
                                if (number == 1) {
                                    spineer.setSelection(i);
                                } else {

                                }
                            }
                            spineer.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        val item2: String = mylist1[position]
                                        minPrice = minPriceslist[position]
                                        isFrees = isFreelist[position]
                                        unitAutoidValue = unitAutoIdList[position]
                                        Log.e("isFrees", isFrees.toString())
                                        priceValue = item2.toDouble()
                                        Price.setText("%.2f".format(priceValue))
                                        Log.e("Price", Price.text.toString())
                                        if (isFreeCheckBox.isChecked) {
                                            isFreeCheckBox.toggle()
                                            Price.isEnabled = true
                                            discountAmount.isEnabled = true
                                            discountPercent.isEnabled = true
                                            Price.setBackgroundResource(R.drawable.borderline)
                                            discountAmount.setBackgroundResource(R.drawable.borderline)
                                            discountPercent.setBackgroundResource(R.drawable.borderline)
                                        }
                                        if (isExchangeCheckBox.isChecked) {
                                            isExchangeCheckBox.toggle()
                                            Price.isEnabled = true
                                            discountAmount.isEnabled = true
                                            discountPercent.isEnabled = true
                                            Price.setBackgroundResource(R.drawable.borderline)
                                            discountAmount.setBackgroundResource(R.drawable.borderline)
                                            discountPercent.setBackgroundResource(R.drawable.borderline)
                                        }
                                        var free = isFrees.toString().toInt()
                                        if (free == 1) {
                                            isFreeCheckBox.setEnabled(true)
                                        } else {
                                            isFreeCheckBox.setEnabled(false)
                                        }
                                    }
                                }
                            listDropdown(spineer)
                        }

                        else {
                            warningMessage(message = responseMessage.toString())
                            pDialog!!.dismiss()
                        }
                        pDialog!!.dismiss()
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
        else {
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
        var dilog = Dialog(this@CartViewActicity)
        dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dilog.setCancelable(false)
        dilog.setContentView(R.layout.add_to_cart_popupview)
        dilog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dilog.window!!.setGravity(Gravity.CENTER)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dilog.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        var SProductID = dilog.findViewById<TextView>(R.id.productIdSalse)
        var s_ProductName = dilog.findViewById<TextView>(R.id.text_ProductName)
        var stockProductS = dilog.findViewById<TextView>(R.id.stockProductS)
        discountAmount = dilog.findViewById<EditText>(R.id.TaxAmount);
        discountPercent = dilog.findViewById<EditText>(R.id.AmountP);
        discountPercent = dilog.findViewById<EditText>(R.id.AmountP);
        valueIncrementDecrement = dilog.findViewById<EditText>(R.id.valueIncrementDecrement);
        incrementBtn = dilog.findViewById<Button>(R.id.incrementBtn);
        decrementBtn = dilog.findViewById<Button>(R.id.decrementBtn);
        var btnAddToCart = dilog.findViewById<Button>(R.id.btnUpdateLocation)
        var btnCloseCart = dilog.findViewById<Button>(R.id.btnCloseLocation)
        Price = dilog.findViewById<EditText>(R.id.Price)
        isFreeCheckBox = dilog.findViewById<CheckBox>(R.id.isFreeCheckBox)
        isExchangeCheckBox = dilog.findViewById<CheckBox>(R.id.isExchangeCheckBox)
        var imageView13 = dilog.findViewById<ImageView>(R.id.imageView13)
        spineer = dilog.findViewById<Spinner>(R.id.spineer) as Spinner
        isFreeCheckBox.visibility=View.GONE
        isExchangeCheckBox.visibility=View.GONE
        SProductID.setText(ClickedItem.getPId())
        s_ProductName.setText(ClickedItem.getPName())
//        stockProductS.setText("Stock : " + ClickedItem.getCStock())
        Picasso.get().load(ClickedItem.getImgPath()).error(R.drawable.default_pic)
            .into(imageView13);
        Price.filters = arrayOf(SalesPersonProductAdapterClass.DecimalDigitsInputFilter(5, 2))
        var valueOrderQty = ClickedItem.getReqQty()
        if (valueOrderQty != 0 && valueOrderQty.toString() != null && valueOrderQty.toString() != "0") {
            valueIncrementDecrement.setText(valueOrderQty.toString())
        }
        var value1 = valueIncrementDecrement.text.toString()
        var value: Int = 0
        try {
            value = value1.toInt()
        } catch (nfe: NumberFormatException) {
            // Handle the condition when str is not a number.
        }
        var countvalue = value
        incrementBtn.setOnClickListener(View.OnClickListener {
            if (value == 0 || value == null) {
                valueIncrementDecrement.setText("1")
            } else {
                countvalue = countvalue + 1
                valueIncrementDecrement.setText(countvalue.toString())
            }
        })
        decrementBtn.setOnClickListener(View.OnClickListener {
            if (value == 0 || value == null) {
                valueIncrementDecrement.setText("1")
            } else {

                if (countvalue > 1) {
                    countvalue = countvalue - 1;
                    valueIncrementDecrement.setText(countvalue.toString());
                }
            }
        })

        discountAmount.addTextChangedListener(object : TextWatcher {
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
                val tmp = discountAmount.text.toString()
                val tmps = minPrice.toString()
                val temsprie = Price.text.toString()
                if (!tmp.isEmpty() && uahEdited && tmp != "." && tmps != null && tmps != "." && temsprie != "" && temsprie != null && tmp != "0.00") {
                    uah = tmp.toFloat()
                    var price = temsprie.toFloat()
                    val minprice = tmps.toFloat()
                    var Amountdiscount: Float = price - uah
                    if (uah > price) {
                        discountAmount.text.replace(0, discountAmount.text.length, "0.00")
                    }
                    if (minprice <= Amountdiscount) {
                        usd = uah * 100 / price
                        discountPercent.setText("%.2f".format(usd))
                    } else {
                        var dilog = Dialog(this@CartViewActicity)
                        dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dilog.setCancelable(false)
                        dilog.setContentView(R.layout.success_message_popup)
                        dilog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dilog.window!!.setGravity(Gravity.CENTER)
                        val lp = WindowManager.LayoutParams()
                        lp.copyFrom(dilog.getWindow()!!.getAttributes())
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT
                        var customername = dilog.findViewById<TextView>(R.id.messagetitle)
                        var btnOk = dilog.findViewById<TextView>(R.id.btnOk)
                        customername.setText("Price can not be below min price.")
                        btnOk.setOnClickListener(View.OnClickListener {
//                                discountAmount.text.replace(0,discountPercent.text.length,"0.00")
                            discountAmount.text.clear()
                            dilog.dismiss()
                        })
                        dilog.show()
                        dilog.getWindow()!!.setAttributes(lp);
                    }
                } else if (tmp.isEmpty()) {
                    discountPercent.text.clear()
                }
            }

            override fun afterTextChanged(s: Editable) {
                uahEdited = false

            }
        })
        discountPercent.addTextChangedListener(object : TextWatcher {
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
                val tmp = discountPercent.text.toString()
                val tmps = Price.text.toString()
                if (!tmp.isEmpty() && usdEdited && tmp != "." && tmps != null && tmps != "" && tmps != "." && tmp != "0.00") {

                    usd = tmp.toFloat()
                    val price = tmps.toFloat()
                    val minprice = minPrice.toString().toFloat()
                    var Amountdiscount: Float = price - usd * 100 / 100
                    if (usd > 100) {
                        discountPercent.text.replace(0, discountPercent.text.length, "0.00")
                    }
                    if (minprice <= Amountdiscount) {
                        uah = usd * price / 100
                        discountAmount.setText("%.2f".format(uah))
                    } else {
                        var dilog = Dialog(this@CartViewActicity)
                        dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dilog.setCancelable(false)
                        dilog.setContentView(R.layout.success_message_popup)
                        dilog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dilog.window!!.setGravity(Gravity.CENTER)
                        val lp = WindowManager.LayoutParams()
                        lp.copyFrom(dilog.getWindow()!!.getAttributes())
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT
                        var customername = dilog.findViewById<TextView>(R.id.messagetitle)
                        var Title = dilog.findViewById<TextView>(R.id.Title)
                        Title.visibility = View.GONE
                        var btnOk = dilog.findViewById<TextView>(R.id.btnOk)
                        customername.setText("Price can not be below min price.")
                        btnOk.setOnClickListener(View.OnClickListener {
//                                discountPercent.text.replace(0,discountPercent.text.length,"0.00")
                            discountPercent.text.clear()
                            dilog.dismiss()
                        })
                        dilog.show()
                        dilog.getWindow()!!.setAttributes(lp);
                    }
                } else if (tmp.isEmpty()) {
                    discountAmount.text.clear()
                }
            }

            override fun afterTextChanged(s: Editable) {
                usdEdited = false
            }
        })

        isFreeCheckBox.setOnClickListener(this)
        isExchangeCheckBox.setOnClickListener(this)

        btnAddToCart.setOnClickListener(View.OnClickListener {

            if (AppPreferences.internetConnectionCheck(it.context)) {
                var pricevalue = Price.text.toString()
                val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                var accessToken = preferences.getString("accessToken", "")
                var empautoid = preferences.getString("EmpAutoId", "")
                var customerAutoId = preferences.getString("customerAutoId", "")
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
                sendRequestObject.put("pObj", pObj.put("productId", ClickedItem.getPId()))
                sendRequestObject.put("pObj", pObj.put("CustomerAutoId", customerAutoId))
                sendRequestObject.put("pObj", pObj.put("unitAutoId", unitAutoidValue))
                sendRequestObject.put("pObj", pObj.put("isFree", ClickedItem.getFree()))
                sendRequestObject.put("pObj", pObj.put("isExchange", ClickedItem.getExchange()))
                sendRequestObject.put("pObj", pObj.put("ReqQty", valueIncrementDecrement.text.toString()))
                if (pricevalue.trim() != "" && pricevalue != null) {
                    var price = pricevalue.toFloat()
                    sendRequestObject.put("pObj", pObj.put("unitPrice", price))
                }
                sendRequestObject.put("pObj", pObj.put("Oim_Discount", discountPercent.text.toString()))
                sendRequestObject.put("pObj", pObj.put("Oim_DiscountAmount", discountAmount.text.toString()))
                if (draftAutoId!=0) {
                    sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
                }

                Log.e("sendRequestObject",sendRequestObject.toString())

                //send request queue in vally
                val queue = Volley.newRequestQueue(this)
                val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
                    AppPreferences.addToCartAPI, sendRequestObject,
                    { response ->
                        val responseResult = JSONObject(response.toString())
                        val responsedData = JSONObject(responseResult.getString("d"))
                        var responseMessage1 = responsedData.getString("responseMessage")
                        val responseStatus = responsedData.getInt("responseStatus")
                        if (responseStatus == 200) {
                            cartResponseResultData = responsedData.getJSONArray("responseData")
                            if (cartResponseResultData.length() != null && cartResponseResultData.length() > 0) {
                                var NetPrice:Float?=null
                                for (i in 0 until cartResponseResultData.length()) {
                                    draftAutoId = cartResponseResultData.getJSONObject(i).getInt("DraftAutoId")
                                    Total = cartResponseResultData.getJSONObject(i).getString("Total")
                                    NofItem = cartResponseResultData.getJSONObject(i).getString("NooofItem")
                                    OQty = cartResponseResultData.getJSONObject(i).getInt("OQty")
                                    UnitPrice = cartResponseResultData.getJSONObject(i).getDouble("UnitPrice").toFloat()
                                    UnitType = cartResponseResultData.getJSONObject(i).getString("UnitType")
                                    Tax = cartResponseResultData.getJSONObject(i).getInt("Tax")
                                    Log.e("Tax level ",Tax.toString())
                                    NetPrice=UnitPrice!!.toFloat()* OQty!!

//                                    addCartDetailsModelClass("","",UnitType!!,0, unitPrice = UnitPrice!!,
//                                        0, netPrice = NetPrice!!,0,0,0,0,"",draftAutoId!!,Total!!,NofItem!!,OQty!!)
                                    // addToCartData.add(cartData)
                                }
                                ClickedItem.setNetPrice(NetPrice)
                                ClickedItem.setUnitPrice(UnitPrice)
                                ClickedItem.setUnitType(UnitType)
                                ClickedItem.setNofItem(NofItem)
                                ClickedItem.setReqQty(OQty)
                                ClickedItem.setUnitPrice(UnitPrice)
                                ClickedItem.setTotal(Total)
                                ClickedItem.setUnitType(UnitType)
                                ClickedItem.settax(Tax)
                                ClickedItem.setNetPrice(NetPrice)
//                                TotalPrice=(NetPrice!!)
//                                if (ClickedItem.getadded()!!>0){
//                                    itemCounts++
//                                }
                                TotalAmount=Total.toString().toFloat()
                              ClickedItem.setdraftAutoId(draftAutoId)
                                cartListAdapter.notifyItemChanged(position)
                                //Adapter.NO_SELECTION
                                var popUp = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                popUp.setContentText(responseMessage1.toString())
                                popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                                popUp.setConfirmClickListener()
                                { sDialog ->
                                    sDialog.dismissWithAnimation()
                                    popUp.dismiss()
                                }

                                popUp.show()
                                popUp.setCanceledOnTouchOutside(false)
                                pDialog!!.dismiss()
                                dilog.dismiss()
                                checkFreeValue = 0
                                isExchangeValue = 0
                            } else {
                                warningMessage(message = responseMessage1.toString())
                                pDialog!!.dismiss()
                            }
                            // holder.cardView5.setCardBackgroundColor(Color.parseColor("#F2A2E8"))
                        } else {
                            var popUp = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            popUp.setContentText(responseMessage1.toString())
                            popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                            popUp.setConfirmClickListener()
                            { sDialog ->
                                sDialog.dismissWithAnimation()
                                popUp.dismiss()
                                pDialog.dismiss()
                            }
                            popUp.show()
                            popUp.setCanceledOnTouchOutside(false)
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
        })

        btnCloseCart.setOnClickListener(View.OnClickListener {
            dilog.dismiss()
        })
        dilog.show()
        dilog.getWindow()!!.setAttributes(lp);
        cartListAdapter.notifyItemChanged(position)
    }


    override fun OnDeleteClicks(position: Int) {
        val ClickedItem: CartListModelClass = productListModelClass[position]
        val actualPosition = position
        productListModelClass.removeAt(actualPosition)
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
            sendRequestObject.put("pObj", pObj.put("productId", ClickedItem.getPId()))
            sendRequestObject.put("pObj", pObj.put("unitAutoId",unitAutoidValue))
            sendRequestObject.put("pObj", pObj.put("isFree",ClickedItem.getFree()))
            sendRequestObject.put("pObj", pObj.put("isExchange",ClickedItem.getExchange()))
            if (draftAutoId == 0) {
                sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
            } else {
                sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
            }
            //  Log.e("productId",productId.toString())
//            Log.e("checkFreeValue", checkFreeValue.toString())
//            Log.e("isExchangeValue", isExchangeValue.toString())
//            Log.e("unitAutoidValue", unitAutoidValue.toString())
            Log.e("draftAutoId", draftAutoId.toString())
            Log.e("draftAutoId", sendRequestObject.toString())

            //send request queue in vally
            val queue = Volley.newRequestQueue(this)
            val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
                AppPreferences.deleteItemApi, sendRequestObject,
                { response ->
                    val responseResult = JSONObject(response.toString())
                    val responsedData = JSONObject(responseResult.getString("d"))
                    var responseMessage2 = responsedData.getString("responseMessage")
                    val responseStatus = responsedData.getInt("responseStatus")
                    if (responseStatus == 200) {
                        var responsDataObject = JSONObject(responsedData.getString("responseData"))
                        var Cstock = responsDataObject.getString("CStock")
                        var bp = responsDataObject.getDouble("BP")
                        var BP = bp.toFloat()
                        ClickedItem.setReqQty(0)
                           var n1: Float = 0.0F
                        ClickedItem.setNetPrice(n1)
                        ClickedItem.setTotal("0.00")
                        CustomerName.setText("Cart(" + productListModelClass.size + ")").toString()
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
        cartListAdapter.notifyItemChanged(actualPosition)
        cartListAdapter.notifyItemRangeChanged(actualPosition, productListModelClass.size)
    }


    private fun addCartDetailsModelClass(
        pId: String,
        pName: String,
        unitType: String,
        qtyPerUnit: Int,
        unitPrice: Float,
        reqQty: Int,
        netPrice: Float,
        free: Int,
        exchange: Int,
        tax: Int,
        unitAutoId: Int,
        ImgPath:String,
        draftAutoId:Int,
        Total:String,
        NofItem:String,
        OQty:Int,
        )
    {
        var ModelClassCustomer1 = CartListModelClass(pId,pName,unitType,qtyPerUnit,unitPrice,reqQty,netPrice,free,exchange,tax,unitAutoId,ImgPath,Total,NofItem,draftAutoId,OQty)
        productListModelClass.add(ModelClassCustomer1)
        val recyclerview = findViewById<RecyclerView>(R.id.cartDetailsRecyclerView)
        cartListAdapter = CartListAdapterClass(productListModelClass, this@CartViewActicity,this@CartViewActicity)
        recyclerview.adapter = cartListAdapter
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
    }

    private fun listDropdown(spineer: Spinner) {
        val popup: Field = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val popupWindow: ListPopupWindow = popup.get(spineer) as ListPopupWindow
        popupWindow.height = (200).toInt()
    }

    override fun onClick(Box: View?) {
        Box as CheckBox
        var isChecked: Boolean = Box.isChecked
        when (Box.id) {
            R.id.isFreeCheckBox -> if (isChecked) {
                checkFreeValue = 1
                isFreeCheckedfunction(true)
            } else {
                isFreeUnCheckedFunction()
            }
            R.id.isExchangeCheckBox -> if (isChecked) {
                isExchangeValue = 1
                isFreeCheckedfunction(isFreeChecked = false)
            } else {
                isFreeUnCheckedFunction()

            }

        }

    }

    private fun isFreeCheckedfunction(isFreeChecked: Boolean) {
        if (isFreeChecked) {
            isExchangeCheckBox.isChecked = false

        } else {
            isFreeCheckBox.isChecked = false
        }
        Price.isEnabled = false
        discountAmount.isEnabled = false
        discountPercent.isEnabled = false
        discountAmount.text.clear()
        discountPercent.text.clear()
        Price.setText("0.00")
        Price.setBackgroundColor(Color.parseColor("#E5E5E5"))
        discountAmount.setBackgroundColor(Color.parseColor("#E5E5E5"))
        discountPercent.setBackgroundColor(Color.parseColor("#E5E5E5"))
        discountAmount.setText("0.00")
        discountPercent.setText("0.00")
    }

    private fun isFreeUnCheckedFunction() {
        checkFreeValue = 0
        isExchangeValue = 0
        Price.isEnabled = true
        discountAmount.isEnabled = true
        discountPercent.isEnabled = true
        Price.setBackgroundResource(R.drawable.borderline)
        discountAmount.setBackgroundResource(R.drawable.borderline)
        discountPercent.setBackgroundResource(R.drawable.borderline)
        Price.setText("%.2f".format(priceValue))
        discountAmount.setText("%.2f".format(0.00))
        discountPercent.setText("%.2f".format(0.00))
    }
}