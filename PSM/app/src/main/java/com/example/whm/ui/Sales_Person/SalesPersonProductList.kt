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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.getCartdetailsModle
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Field


class SalesPersonProductList : AppCompatActivity(), View.OnClickListener,
    SalesPersonProductAdapterClass.OnItemClickLitener {
    var accessToken: String? = null
    var empautoid: String? = null
    var draftAutoId: Int = 0
    var itemCounts: Int = 0
    var TotalPrice:Float=0.0f
    var totalValue:Double?=null
    //    var draftAutoID:Int?=0
    var customerName: String? = null
    var ProductItemList: String? = null
    var customerId: String? = null
    var username: String? = null
    lateinit var productTitle: TextView
    lateinit var cart_badge: TextView
    lateinit var orderTotalValue: TextView
    var responseMessage: String? = null
    var pDialog: SweetAlertDialog? = null
    var productListModelClass: ArrayList<SalesPersonProductModel> = arrayListOf()
    lateinit var productAdapterClass: SalesPersonProductAdapterClass

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
        setContentView(R.layout.activity_sales_person_product_list)
        var userInformation = findViewById<TextView>(R.id.information)
        var ScanBarcode = findViewById<EditText>(R.id.ScanBarcodeEditeTextView)
        cart_badge = findViewById<TextView>(R.id.cart_badge)
        orderTotalValue = findViewById<TextView>(R.id.cart2)

        productTitle = findViewById<TextView>(R.id.productTitle)
        var syncProduct = findViewById<TextView>(R.id.syncProduct)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@SalesPersonProductList)
        accessToken = preferences.getString("accessToken", "")
        empautoid = preferences.getString("EmpAutoId", "")
        customerName = preferences.getString("CustomerName", "")
        customerId = preferences.getString("customerId", "")
        ProductItemList = preferences.getString("ProductItemList", "")

        draftAutoId = intent.getIntExtra("draftAutoId", 0)
         var Totals   = intent.getFloatExtra("TotalAmount",0.0f)
         var NofItems = intent.getIntExtra("totalcount",0)
        TotalPrice=Totals.toString().toFloat()
        itemCounts=NofItems.toInt()
        cart_badge.setText(itemCounts.toString())
        orderTotalValue.setText("%.2f".format(TotalPrice))

        Log.e("draftautoId 12", draftAutoId.toString())
        Log.e("totalValue 12", TotalPrice.toString())
        Log.e("username 12", itemCounts.toString())
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver, IntentFilter("USER_NAME_CHANGED_ACTION"))

        userInformation.setOnClickListener(View.OnClickListener {
            var dilog = Dialog(this@SalesPersonProductList)
            dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dilog.setCancelable(false)
            dilog.setContentView(R.layout.success_message_popup)
            dilog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dilog.window!!.setGravity(Gravity.CENTER)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dilog.getWindow()!!.getAttributes())
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            var customerID = dilog.findViewById<TextView>(R.id.Title)
            var customername = dilog.findViewById<TextView>(R.id.messagetitle)
            customerID.visibility = View.VISIBLE
            var btnOk = dilog.findViewById<TextView>(R.id.btnOk)
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
                productListApiCall("", "", "")
            } else {
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
        var btnBackarrow1 = findViewById<TextView>(R.id.btnSalesPersonBack)
        var SearchProduct = findViewById<TextView>(R.id.SearchProduct)
        var cartview = findViewById<ImageView>(R.id.cartview)
        btnBackarrow1.setOnClickListener(View.OnClickListener {
            finish();
        })
        SearchProduct.setOnClickListener(View.OnClickListener {
            dialogCustom()
        })
        cartview.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, CartViewActicity::class.java)
            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putString("OrderAutoid", draftAutoId?.toString())
            sharedLoadOrderPage.putFloat("TotalPrice", Total!!.toFloat())
            sharedLoadOrderPage.putInt("itemCounts", NofItem!!.toInt())

            sharedLoadOrderPage.apply()
            startActivity(intent)
            finish()
        })
        if (AppPreferences.internetConnectionCheck(this)) {
            ScanBarcode!!.requestFocus()
            ScanBarcode!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                    var scanbarcodeproduct = ScanBarcode!!.text.toString()
                    if (scanbarcodeproduct.trim().isEmpty()) {
                        ScanBarcode.text.clear()
                        ScanBarcode.setText("")
                        ScanBarcode.requestFocus()
                    } else {
                        // Scan barcode function calling here
                        productListModelClass.clear()
                        productListApiCall("", "", barcode = ScanBarcode.text.toString())
                        ScanBarcode.requestFocus()
                        ScanBarcode.text.clear()
                        // btnPrevious.isEnabled=true
                    }
                }

                false

            })
        }
        if (AppPreferences.internetConnectionCheck(this)) {
            val recyclerview = findViewById<RecyclerView>(R.id.ProductListRecyclerView)
            val layoutManager = LinearLayoutManager(this)
            recyclerview.layoutManager = layoutManager
            productListApiCall("", "", "")
        } else {
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
            if (intent != null) {
                username = intent.getStringExtra("username");
                var total = intent.getStringExtra("Total");
                 draftAutoId = intent.getIntExtra("draftAutoId", 0);
                 totalValue = total.toString().toDouble()
                cart_badge.setText(username)
                orderTotalValue.setText("%.2f".format(totalValue))
            }
        }
    }

    private fun productListApiCall(productId: String, productName: String, barcode: String) {
        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog!!.titleText = "Fetching ..."
        pDialog!!.setCancelable(false)
        pDialog!!.show()
        val sendRequestObject = JSONObject()
        val requestContainer = JSONObject()
        val pObj = JSONObject()
        sendRequestObject.put("requestContainer",
            requestContainer.put("appVersion", AppPreferences.AppVersion))
        sendRequestObject.put("requestContainer",
            requestContainer.put("deviceID",
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)))
        sendRequestObject.put("requestContainer",
            requestContainer.put("deviceVersion", AppPreferences.versionRelease))
        sendRequestObject.put("requestContainer",
            requestContainer.put("deviceName", AppPreferences.DeviceName))
        sendRequestObject.put("requestContainer", requestContainer.put("accessToken", accessToken))
        sendRequestObject.put("requestContainer", requestContainer.put("userAutoId", empautoid))
        sendRequestObject.put("pObj", pObj.put("productName", productName))
        sendRequestObject.put("pObj", pObj.put("productId", productId))
        sendRequestObject.put("pObj", pObj.put("customerId", customerId))
        sendRequestObject.put("pObj", pObj.put("barcode", barcode))
        if (draftAutoId == 0) {
            sendRequestObject.put("pObj", pObj.put("draftautoId", draftAutoId))
        } else {
            sendRequestObject.put("pObj", pObj.put("draftautoId", draftAutoId))
        }
        //send request queue in vally
        val queue = Volley.newRequestQueue(this)
        val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
            AppPreferences.getProductListApi,
            sendRequestObject,
            { response ->
                val responseResult = JSONObject(response.toString())
                val responsedData = JSONObject(responseResult.getString("d"))
                responseMessage = responsedData.getString("responseMessage")
                val responseCode = responsedData.getString("responseCode")
                productListModelClass.clear()
                if (responseCode == "201") {
                    val responseResultData = responsedData.getJSONArray("responseData")
                    if (responseResultData.length() != null && responseResultData.length() > 0) {
                        for (i in 0 until responseResultData.length()) {
                            var PId = responseResultData.getJSONObject(i).getString("PId")
                            var PName = responseResultData.getJSONObject(i).getString("PName")
                            var ImageUrl = responseResultData.getJSONObject(i).getString("ImageUrl")
                            var CStock = responseResultData.getJSONObject(i).getString("CStock")
                            var balencePrice = responseResultData.getJSONObject(i).getDouble("BP")
                            var UnitType = responseResultData.getJSONObject(i).getString("UnitType")
                            var NetPrices =
                                responseResultData.getJSONObject(i).getDouble("NetPrice")
                            var IsTaxable = responseResultData.getJSONObject(i).getInt("IsTaxable")
                            var ReqQty = responseResultData.getJSONObject(i).getInt("ReqQty")
                            var OUnitType = responseResultData.getJSONObject(i).getInt("OUnitType")
                            var UnitPrice =
                                responseResultData.getJSONObject(i).getDouble("UnitPrice")
                            var UnitPrices: Float = UnitPrice.toFloat()
                            var NetPrice: Float = NetPrices.toFloat()
                            var BP: Float = balencePrice.toFloat()
                            Log.e("ReqQty", ReqQty.toString())
                            addProductdataModelClass(PId,
                                PName,
                                ImageUrl,
                                CStock,
                                BP,
                                UnitType,
                                NetPrice,
                                IsTaxable,
                                ReqQty,
                                OUnitType,
                                UnitPrices,
                                0,
                                "",
                                "",
                                0.2f,
                                0,
                                0)
                        }
                        var totalSize = productListModelClass.size
                        productTitle.setText("Product(" + totalSize + ")").toString()
                        pDialog!!.dismiss()
                    } else {
                        var popUp = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        popUp.setContentText(responseMessage)
                        popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                        popUp.setConfirmClickListener()
                        { sDialog ->
                            sDialog.dismissWithAnimation()
                            popUp.dismiss()
                        }
                        popUp.show()
                        popUp.setCanceledOnTouchOutside(false)
                        pDialog!!.dismiss()
                    }
                    pDialog!!.dismiss()
                } else {
                    massagePopupAlert()
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


    override fun OnItemClick(position: Int) {
        val ClickedItem: SalesPersonProductModel = productListModelClass[position]

        if (AppPreferences.internetConnectionCheck(this@SalesPersonProductList)) {
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
                            pDialog!!.dismiss()
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
        var dilog = Dialog(this@SalesPersonProductList)
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
        SProductID.setText(ClickedItem.getPId())
        s_ProductName.setText(ClickedItem.getPName())
        stockProductS.setText("Stock : " + ClickedItem.getCStock())
        Picasso.get().load(ClickedItem.getImageUrl()).error(R.drawable.default_pic)
            .into(imageView13);
        Price.filters = arrayOf(SalesPersonProductAdapterClass.DecimalDigitsInputFilter(5, 2))
        var valueOrderQty = ClickedItem.getOQty()
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
                        var dilog = Dialog(this@SalesPersonProductList)
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
                        var dilog = Dialog(this@SalesPersonProductList)
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
                sendRequestObject.put("pObj", pObj.put("isFree", checkFreeValue))
                sendRequestObject.put("pObj", pObj.put("isExchange", isExchangeValue))
                sendRequestObject.put("pObj",
                    pObj.put("ReqQty", valueIncrementDecrement.text.toString()))
                if (pricevalue.trim() != "" && pricevalue != null) {
                    var price = pricevalue.toFloat()
                    sendRequestObject.put("pObj", pObj.put("unitPrice", price))
                }
                sendRequestObject.put("pObj",
                    pObj.put("Oim_Discount", discountPercent.text.toString()))
                sendRequestObject.put("pObj",
                    pObj.put("Oim_DiscountAmount", discountAmount.text.toString()))
                if (draftAutoId == 0) {
                    sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
                } else {
                    sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
                }

                Log.e("checkFreeValue", checkFreeValue.toString())
                Log.e("isExchangeValue", isExchangeValue.toString())
                Log.e("NofItem", valueIncrementDecrement.text.toString())
                Log.e("Price", Price.text.toString())
                Log.e("unitAutoidValue", unitAutoidValue.toString())
                Log.e("discountPercent.text", discountPercent.text.toString())
                Log.e("discountAmount.text", discountAmount.text.toString())
                Log.e("draftAutoId", draftAutoId.toString())

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
                                    draftAutoId = cartResponseResultData.getJSONObject(i)
                                        .getInt("DraftAutoId")
                                    Total =
                                        cartResponseResultData.getJSONObject(i).getString("Total")
                                    NofItem = cartResponseResultData.getJSONObject(i)
                                        .getString("NooofItem")
                                    OQty = cartResponseResultData.getJSONObject(i).getInt("OQty")
                                    UnitPrice = cartResponseResultData.getJSONObject(i)
                                        .getDouble("UnitPrice").toFloat()
                                    UnitType = cartResponseResultData.getJSONObject(i)
                                        .getString("UnitType")
                                    Tax = cartResponseResultData.getJSONObject(i).getInt("Tax")
                                    Log.e("Tax level ",Tax.toString())
                                     NetPrice=UnitPrice!!.toFloat()* OQty!!
//                                            UnitPrice:Float=UnitPric.toFloat()
                                    // var cartData=AddToCartDataModle(draftAutoId,Total!!,NofItem!!,OQty!!,UnitPrices,UnitType)
                                    addProductdataModelClass("",
                                        "",
                                        "",
                                        "",
                                        0.2f,
                                        UnitType!!,
                                        NetPrice = NetPrice,
                                        IsTaxable = Tax!!,
                                        0,
                                        0,
                                        0.2f,
                                        draftAutoId!!,
                                        Total!!,
                                        NofItem!!,
                                        UnitPrice!!,
                                        OQty!!,
                                        Tax!!)

                                    // addToCartData.add(cartData)
                                }

                                ClickedItem.setNofItem(NofItem)
                                ClickedItem.setOQty(OQty)
                                ClickedItem.setUnitPrice(UnitPrice)
                                ClickedItem.setTotal(Total)
                                ClickedItem.setUnitType(UnitType)
                                ClickedItem.setIsTaxable(Tax)
                                ClickedItem.setNetPrice(NetPrice)
                                TotalPrice=(NetPrice!!)
                                if (ClickedItem.getNetPrice()!!>0.0f){
                                    itemCounts++
                                }
//                              ClickedItem.setdraftAutoId(draftAutoId)
                                //Adapter.NO_SELECTION
                                val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                                val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
                                sharedLoadOrderPage.putInt("checkFreeValue", checkFreeValue)
                                sharedLoadOrderPage.putInt("isExchangeValue", isExchangeValue)
                                sharedLoadOrderPage.putInt("unitAutoidValue", unitAutoidValue!!)
                                sharedLoadOrderPage.putInt("draftAutoId", draftAutoId!!)
                                sharedLoadOrderPage.apply()

                                Log.e("draftAutoId", draftAutoId.toString())
                                val intent = Intent("USER_NAME_CHANGED_ACTION")
                                intent.putExtra("username", NofItem.toString())
                                intent.putExtra("Total", Total.toString())
                                intent.putExtra("draftAutoId", draftAutoId)
                                // put your all data using put extra
                                // put your all data using put extra
                                LocalBroadcastManager.getInstance(it.context).sendBroadcast(intent)

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
        productAdapterClass.notifyItemChanged(position)
    }

    override fun OnDeleteClick(position: Int) {
        val ClickedItem: SalesPersonProductModel = productListModelClass[position]
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
            sendRequestObject.put("pObj", pObj.put("unitAutoId", unitAutoidValue))
            sendRequestObject.put("pObj", pObj.put("isFree", checkFreeValue))
            sendRequestObject.put("pObj", pObj.put("isExchange", isExchangeValue))
            if (draftAutoId == 0) {
                sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
            } else {
                sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
            }
            if (ClickedItem.getOUnitType() != null && ClickedItem.getOUnitType() != 0) {
                sendRequestObject.put("pObj", pObj.put("unitAutoId", ClickedItem.getOUnitType()))
            }

            //  Log.e("productId",productId.toString())
//            Log.e("checkFreeValue", checkFreeValue.toString())
//            Log.e("isExchangeValue", isExchangeValue.toString())
//            Log.e("unitAutoidValue", unitAutoidValue.toString())
//            Log.e("draftAutoId", draftAutoId.toString())
//            Log.e("draftAutoId", sendRequestObject.toString())

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
                        val actualPosition = position
                        var Cstock = responsDataObject.getString("CStock")
                         Total= responsDataObject.getString("OrderTotal")
                         NofItem = responsDataObject.getString("TotalItems")
                        var bp = responsDataObject.getDouble("BP")
                        var BP = bp.toFloat()
                        UnitType = responsDataObject.getString("UnitType")
                        val intent = Intent("USER_NAME_CHANGED_ACTION")
                        intent.putExtra("username", NofItem.toString())
                        intent.putExtra("Total", Total.toString())
                        intent.putExtra("draftAutoId", draftAutoId)
                        // put your all data using put extra
                        // put your all data using put extra
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                        ClickedItem.setReqQty(0)
                        ClickedItem.setOQty(0)
                        var n1: Float = 0.0F
                        ClickedItem.setNetPrice(n1)
                        ClickedItem.setBP(BP = BP)
                        ClickedItem.setUnitType(UnitType)
                        ClickedItem.setCStock(Cstock)
                        ClickedItem.setTotal(Total)
                        ClickedItem.setNofItem(NofItem)
                        ClickedItem.setIsTaxable(0)
                        productAdapterClass.notifyItemChanged(actualPosition)
                        productAdapterClass.notifyItemRangeChanged(actualPosition,
                            getcartDetailsdata.size)

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
    }

    private fun addProductdataModelClass(
        PId: String,
        PName: String,
        ImageUrl: String,
        CStock: String,
        BP: Float,
        UnitType: String,
        NetPrice: Float,
        IsTaxable: Int,
        ReqQty: Int,
        OUnitType: Int,
        UnitPrices: Float,
        draftAutoId: Int,
        Total: String,
        NofItem: String,
        UnitPrice: Float,
        OQty: Int,
        Tax: Int,
    ) {
        var productModelClass = SalesPersonProductModel(PId,
            PName,
            ImageUrl,
            CStock,
            BP,
            UnitType,
            NetPrice,
            IsTaxable,
            ReqQty,
            OUnitType,
            UnitPrices,
            OQty,
            Tax,
            UnitPrice,
            Total,
            NofItem,
            draftAutoId)
        productListModelClass.add(productModelClass)
        val recyclerview = findViewById<RecyclerView>(R.id.ProductListRecyclerView)
        productAdapterClass = SalesPersonProductAdapterClass(productListModelClass,
            this@SalesPersonProductList,
            this@SalesPersonProductList)
        recyclerview.adapter = productAdapterClass
    }

    private fun dialogCustom() {
        var dilog = Dialog(this@SalesPersonProductList)
        dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dilog.setCancelable(false)
        dilog.setContentView(R.layout.activity_search_customer)
        dilog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dilog.window!!.setGravity(Gravity.CENTER)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dilog.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        var productId = dilog.findViewById<EditText>(R.id.AmountP)
        var productName = dilog.findViewById<EditText>(R.id.CustomerName)
        productId.setHint("Product ID ")
        productName.setHint("Product Name ")
        var btnSearch = dilog.findViewById<Button>(R.id.btnSearch)
        var btnCancle = dilog.findViewById<TextView>(R.id.CancleBtn)
        var spinner = dilog.findViewById<View>(R.id.Spinnervalue) as Spinner
        spinner.visibility = View.GONE
        btnCancle.setOnClickListener(View.OnClickListener {
            dilog.dismiss()
        })
        btnSearch.setOnClickListener(View.OnClickListener {
            productListModelClass.clear()
            productListApiCall(productId = productId.text.toString(),
                productName = productName.text.toString(),
                "")
            dilog.dismiss()
        })
        dilog.show()
        dilog.getWindow()!!.setAttributes(lp);
    }

    private fun massagePopupAlert() {
        var popUp = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        popUp.setContentText(responseMessage)
        popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
        popUp.setConfirmClickListener()
        { sDialog ->
            sDialog.dismissWithAnimation()
            productListApiCall("", "", "")
        }
        popUp.show()
        popUp.setCanceledOnTouchOutside(false)
        pDialog!!.dismiss()
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

