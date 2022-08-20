package com.example.whm.ui.Piker_And_Packer_OrderList

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.AdapterClass.Stock_availableAdapter
import com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.ModelClass.StockAvailableModel
import com.example.myapplication.com.example.whm.ui.UpdateLocation.setCanceledOnTouchOutside
import com.example.whm.ui.OrderDetailsList.OrderDetailsActicity
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject


class StartPackingOrderList : AppCompatActivity() {
    lateinit var btnSaveNext: Button
    lateinit var btnFirstItem: Button
    lateinit var btnLastItem: Button
    lateinit var btnPrevious: Button
    lateinit var btnSkipAndNext: Button
    lateinit var btnPacked: Button
    lateinit var ViewBtn: Button

    lateinit var OrderNo1: TextView
    lateinit var ProductIDText: TextView
    lateinit var ProductNameText: TextView
    lateinit var UnitTypeText: TextView
    lateinit var LoctaionText: TextView
    lateinit var StockText: TextView
    lateinit var OrderQtyText: TextView
    lateinit var ShipingQtyText: TextView
    lateinit var RemainingQtyText: TextView
    lateinit var ProductImage: ImageView
    lateinit var ProductQtyText: TextView
    lateinit var textViewBarcode: TextView
    lateinit var Yeschange: TextView
    lateinit var changeStatusProduct: TextView
    lateinit var et: EditText
    lateinit var btn_increment: Button
    lateinit var btn_dicrement: Button
  lateinit var meainConstraint: ConstraintLayout
  lateinit var constraint1: ConstraintLayout

    var addbarcode: String? = null
    var Barcode: String = ""
    var OrderAutoId: String? = null
    var PackerAutoid: String? = null
    var StatusAutoId: String? = null
    var stusts: String? = null
    var isFreeItem: Int? = null
    var IsExchange: Int? = null
    var backarrow: TextView? = null
    var OrderNo: String? = null

    //   var icount: Int = 1
    var itemcount: Int = 1

    // var Total: Int = 0
    var OrderQty: Int = 0
    var ShipingQty: Int = 0
    var PAutoId: Int? = 0
    var unitAutoId: Int? = 0
    var remainingQty: Int = 0
    var Stock: String? = null
    var ProductID: Int? = 0
    var ProductName: String? = null
    var UnitType: String? = null
    var Loctaion: String? = null
    var ProductImage1: String = " "
    var PackStatus1: Int = 0
    var Increment: Int = 999
    var TItems: Int = 0
    @SuppressLint("ResourceType", "WrongViewCast")
    var StockData: ArrayList<StockAvailableModel> = arrayListOf()
    lateinit var StockAdapter: Stock_availableAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_packing_order_list)
        var addbarcode1: EditText = findViewById(R.id.ScanBarcode)
        btnSaveNext = findViewById(R.id.btnSaveNext)
        btnFirstItem = findViewById(R.id.FirstItemProduct)
        btnLastItem = findViewById(R.id.LastItemProduct)
        btnPrevious = findViewById(R.id.btnPreviousProduct)
        btnSkipAndNext = findViewById(R.id.btnSkipAndNext)
        btnPacked = findViewById(R.id.btnOrderPacked)
        ViewBtn = findViewById(R.id.ViewBtn)
        //For Increment and Decrement Button
        btn_increment = findViewById(R.id.btn_increment)
        btn_dicrement = findViewById(R.id.btn_dicrement)
        et = findViewById(R.id.EdtTextBox)

        OrderNo1 = findViewById(R.id.OrderTitle)
        ProductIDText = findViewById(R.id.Product_Id)
        ProductNameText = findViewById(R.id.ProductName)
        UnitTypeText = findViewById(R.id.Unit_Type)
        LoctaionText = findViewById(R.id.LocationProduct)
        StockText = findViewById(R.id.Stock)
        OrderQtyText = findViewById(R.id.OrderQty)
        ShipingQtyText = findViewById(R.id.PackedQty)
        RemainingQtyText = findViewById(R.id.RemainingQty)
        ProductImage = findViewById(R.id.ProductImage)
        ProductQtyText = findViewById(R.id.ProductQty)
        textViewBarcode = findViewById(R.id.textViewBarcode)
        changeStatusProduct = findViewById(R.id.changeStatusProduct)
        Yeschange = findViewById(R.id.Yeschange)
        meainConstraint=findViewById(R.id.meainConstraint)
        constraint1=findViewById(R.id.constraint1)
        changeStatusProduct.visibility = View.GONE
        Yeschange.visibility = View.GONE
        backarrow = findViewById(R.id.BtnBack)
        backarrow?.setOnClickListener(View.OnClickListener {
//            val intent: Intent = Intent(this, MainActivity2::class.java)
//            startActivity(intent)
            finish()
        })
        meainConstraint.visibility=View.VISIBLE
        constraint1.visibility=View.VISIBLE
        btnPrevious.isEnabled = false
        val preferencesid =
            PreferenceManager.getDefaultSharedPreferences(this@StartPackingOrderList)
        OrderAutoId = (preferencesid.getString("OrderAutoid", OrderAutoId.toString()))
        PackerAutoid = (preferencesid.getString("EmpAutoId", ""))
        StatusAutoId = (preferencesid.getString("StatusAutoId", ""))
        stusts = (preferencesid.getString("stusts", ""))

        OrderNo1.setText("Order No : " + preferencesid.getString("OrderNo",""))
        ProductDetailFunction()
        ViewBtn.isEnabled = true
        btnFirstItem.isEnabled = true
        //Code here Button First Item
        btnFirstItem.setOnClickListener(View.OnClickListener {
            addbarcode1!!.requestFocus()
            itemcount = 1
            ProductDetailFunction()
            btnSkipAndNext.isEnabled = true
            btnSaveNext.setText("Save & Next ")
            btnSaveNext.isEnabled = true
            btnLastItem.isEnabled = true


        })
        //Close First Item Button code


        //Code here Button Last Item
        btnLastItem.setOnClickListener(View.OnClickListener {
            addbarcode1!!.requestFocus()
            itemcount = TItems
            ProductDetailFunction()
            btnFirstItem.isEnabled = true
            btnLastItem.isEnabled = false
            btnSaveNext.isEnabled = true
            btnPrevious.isEnabled = true
        })
        //Close Last Item Button

        //Code for Previous button here
        btnPrevious.setOnClickListener(View.OnClickListener {
            //btnFirstItem.isEnabled=true
            //btnSaveNext.setText("Save & Next ")
            addbarcode1!!.requestFocus()
            PreviousButton()
            btnFirstItem.isEnabled = true
            btnSkipAndNext.isEnabled = true
            btnSaveNext.isEnabled = true
        })

        //Close Code for Previous button here
        btnSkipAndNext.setOnClickListener(View.OnClickListener {
            addbarcode1!!.requestFocus()
            SkipAndNextButton()
//            btnPrevious.isEnabled = true
            // btnSaveNext.setText("Save & Next")
            btnFirstItem.isEnabled = true
            btnLastItem.isEnabled = true
            btnSaveNext.isEnabled = true
            btnPrevious.isEnabled = true
        })


        //var btnPackedOrder=Dilogview.findViewById<Button>(R.id.btnPackedOrder)
        //val btnPackedOrder: Button =view.findViewById(R.id.btnPackedOrder)

        btnPacked.setOnClickListener(View.OnClickListener {
            // Toast.makeText(this,PackStatus1.toString(),Toast.LENGTH_LONG).show()
            if (PackStatus1 == 1) {
                val Dilogview =
                    View.inflate(this@StartPackingOrderList, R.layout.popupbox_picker, null)
                val builder = AlertDialog.Builder(this@StartPackingOrderList).setView(Dilogview)
                builder.setCancelable(false);
                builder.setCanceledOnTouchOutside(false);
                var textNameWarning = Dilogview.findViewById<TextView>(R.id.StartAndResumPacking)
                var nametext = Dilogview.findViewById<TextView>(R.id.CustomerName)
                var btnYesPack = Dilogview.findViewById<TextView>(R.id.btnYesPack)
                btnYesPack.requestFocus()
                var btnNoCancle = Dilogview.findViewById<TextView>(R.id.btnNoCancle)
                textNameWarning.setText("Warning")
                nametext.setText("Pack at least one item")
                btnNoCancle.setText("OK")
                val mSilog = builder.show()
                btnYesPack.visibility = View.GONE
                btnNoCancle.setOnClickListener(View.OnClickListener { mSilog.dismiss() })
            } else if (PackStatus1 == 2) {
                val Dilogview = View.inflate(this@StartPackingOrderList, R.layout.popupbox_picker, null)
                val builder = AlertDialog.Builder(this@StartPackingOrderList).setView(Dilogview)
                builder.setCancelable(false);
                builder.setCanceledOnTouchOutside(false);
                var textNameWarning = Dilogview.findViewById<TextView>(R.id.StartAndResumPacking)
                var nametext = Dilogview.findViewById<TextView>(R.id.CustomerName)
                var btnYesPack = Dilogview.findViewById<TextView>(R.id.btnYesPack)
                btnYesPack.requestFocus()
                var btnNoCancle = Dilogview.findViewById<TextView>(R.id.btnNoCancle)
                btnYesPack.visibility = View.VISIBLE
                textNameWarning.setText("Are you sure?")
                nametext.setText("Ship quantity of some product are less than ordered quantity. Are you sure you want to proceed?")
                val mSilog = builder.show()
                btnYesPack.setOnClickListener(View.OnClickListener {
                    PackedOrder()
                    mSilog.dismiss()
                })
                btnNoCancle.setOnClickListener(View.OnClickListener { mSilog.dismiss() })


            } else if (PackStatus1 == 0) {

                PackedOrder()
            }
        })
        btn_increment.setOnClickListener(View.OnClickListener {
            var value = et.text.trim().toString()
            if (value.trim() == "" || value.trim() == "0") {
                et.setText("1")
            } else {
                var shipcount = value.toInt()
                shipcount = shipcount + 1

                if (OrderQtyText.text.toString().toInt() >= shipcount) {
                    et.setText(shipcount.toString());
                } else {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(
                        "Shipped Qty can not more than Order Qty."
                    ).show()
                    // Toast.makeText(this,"Shipped Qty can not more than Order Qty.",Toast.LENGTH_LONG).show()
                }
            }
        })
        btn_dicrement.setOnClickListener(View.OnClickListener {
            var ship = et.text.trim().toString()

            if (ship.trim() == "" || ship.trim() == "0" || ship.trim() == "1") {
                et.setText("0")
            } else {
                var shipcount = ship.toInt()
                shipcount = shipcount - 1;
                et.setText(shipcount.toString());
            }
        })
        btnSaveNext.setOnClickListener(View.OnClickListener {
            if (AppPreferences.internetConnectionCheck(this)) {
                addbarcode1!!.requestFocus()
                var valueCount = et.text.toString()
                if (valueCount.trim() == "") {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(
                        "Shipped Qty can not  Blank."
                    ).show()
                    et.text.clear()
                } else if (OrderQtyText.text.toString().toInt() < valueCount.toInt()) {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(
                        "Shipped Qty can not more than Order Qty."
                    ).show()
                    et.text.clear()
                }

                else {
                    val Jsonarra = JSONObject()
                    val JSONObj = JSONObject()
                    val pobj = JSONObject()
                    val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                    pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
                    pDialog.titleText = "Fetching ..."
                    pDialog.setCancelable(false)
                    pDialog.show()
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                    var accessToken =
                        preferences.getString("accessToken", "")
                    JSONObj.put(
                        "requestContainer", Jsonarra.put(
                            "deviceID",
                            Settings.Secure.getString(
                                this?.contentResolver,
                                Settings.Secure.ANDROID_ID
                            )
                        )
                    )
                    //val queues = Volley.newRequestQueue(this)
                    JSONObj.put(
                        "requestContainer",
                        Jsonarra.put("appVersion", AppPreferences.AppVersion)
                    )
                    JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
                    JSONObj.put("pobj", pobj.put("orderAutoId", OrderAutoId))
                    JSONObj.put("pobj", pobj.put("productAutoId", PAutoId))
                    JSONObj.put("pobj", pobj.put("unitAutoId", unitAutoId))
                    JSONObj.put("pobj", pobj.put("isFreeItem", isFreeItem))
                    JSONObj.put("pobj", pobj.put("IsExchange", IsExchange))
                    Log.e("orderAutoId",OrderAutoId.toString())
                    Log.e("PAutoId",PAutoId.toString())
                    Log.e("unitAutoId",unitAutoId.toString())
                    Log.e("PackerAutoid",PackerAutoid.toString())
                    var counter = 0;
                    if (itemcount == TItems) {
                        btnSaveNext.isEnabled = true
                        counter = itemcount
                        btnSaveNext.setText("Save")
                    } else if (TItems > 1) {
                        counter = itemcount + 1
                        // btnSaveNext.setText(" ")
                    }

                    JSONObj.put("pobj", pobj.put("itemcount", counter))

                    var value = valueCount.toInt()
                    JSONObj.put("pobj", pobj.put("shipQty", value))

                    val queue = Volley.newRequestQueue(this)
// Request a string response from the provided URL.
                    val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
                        AppPreferences.ManualPackProduct_PICKER_PACKER,
                        JSONObj,
                        { response ->
                            // Display the first 500 characters of the response string.
                            val resobj = (response.toString())
                            val responsemsg = JSONObject(resobj.toString())
                            val resultobj = JSONObject(responsemsg.getString("d"))
                            val resmsg = resultobj.getString("responseMessage")
                            val rescode = resultobj.getString("responseCode")
                            //Toast.makeText(this,resmsg.toString(),Toast.LENGTH_LONG).show()
                            if (rescode == "200") {
                                val responsData = resultobj.getJSONArray("responseData")
                                for (i in 0 until responsData.length()) {
                                    //var icount: Int? = 0
//                                    var PId: Int = 0
//                                    var PName: String? = null
//                                    var Unitype: String? = null
//                                    var OrderRQty: Int = 0
//                                    var ShipQty: Int? = 0
//                                    var Stock: String? = null
//                                    var Loc: String? = null
                                    ProductID = responsData.getJSONObject(i).getInt("PId")
                                    isFreeItem = responsData.getJSONObject(i).getInt("isFreeItem")
                                    IsExchange = responsData.getJSONObject(i).getInt("IsExchange")
                                    PAutoId = responsData.getJSONObject(i).getInt("PAutoId")
                                    unitAutoId = responsData.getJSONObject(i).getInt("UAutoId")
                                    itemcount = responsData.getJSONObject(i).getInt("icount")
                                    if (itemcount == TItems) {
                                        btnSaveNext.isEnabled = true
                                        btnSaveNext.setText("Save")
                                    }
                                    ProductName = responsData.getJSONObject(i).getString("PName")
                                    UnitType = responsData.getJSONObject(i).getString("UName")
                                    Loctaion = responsData.getJSONObject(i).getString("Loc")
                                    TItems = responsData.getJSONObject(i).getInt("TItems")
                                    Stock = responsData.getJSONObject(i).getString("Stock")
                                    ShipingQty = responsData.getJSONObject(i).getInt("ShipQty")
                                    OrderQty = responsData.getJSONObject(i).getInt("RQty")
                                    var PImg = responsData.getJSONObject(i).getString("PImg")
                                    Barcode = responsData.getJSONObject(i).getString("Barcode")
                                    var PackStatus =
                                        responsData.getJSONObject(i).getInt("PackStatus")
                                    PackStatus1 = PackStatus
                                    remainingQty = (OrderQty!!.toInt() - ShipingQty!!.toInt())
                                    ProductIDText.setText(ProductID.toString())
                                    ProductNameText.setText(ProductName)
                                    UnitTypeText.setText(UnitType.toString())
                                    LoctaionText.setText(Loctaion)
                                    ProductQtyText.setText(itemcount.toString() + " Out of " + TItems.toString())
                                    StockText.setText(Stock.toString())
                                    OrderQtyText.setText(OrderQty.toString())
                                    ShipingQtyText.setText(ShipingQty.toString())
                                    RemainingQtyText.setText(remainingQty.toString())
                                    textViewBarcode.setText(Barcode.toString())
                                    et.setText(ShipingQty.toString())
                                    if (isFreeItem == 1) {
                                        changeStatusProduct.visibility = View.VISIBLE
                                        Yeschange.visibility = View.VISIBLE
                                        changeStatusProduct.setText("Is Free : ")
                                    } else if (IsExchange == 1) {
                                        changeStatusProduct.visibility = View.VISIBLE
                                        Yeschange.visibility = View.VISIBLE
                                        changeStatusProduct.setText("Is Exchange : ")
                                    } else {
                                        changeStatusProduct.visibility = View.GONE
                                        Yeschange.visibility = View.GONE
                                    }

                                    if (PImg=="") {
                                        Picasso.get().load(PImg).error(R.drawable.default_pic)
                                            .into(ProductImage);

                                    }

                                    // Picasso.get().load(PImg).error(R.drawable.picturesimg).into(holder.imageOrderDetails);


                                }
                                pDialog.dismiss()
                            }
                            else {
                                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(
                                    resmsg
                                ).show()
                                pDialog.dismiss()

                            }
                        },
                        Response.ErrorListener {
                            pDialog.dismiss()
                        })
                    JsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                        10000000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )


// Add the request to the RequestQueue.
                    try {
                        queue.add(JsonObjectRequest)
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
                    }
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
                    finish()
                }
                dialog?.show()
            }
            et.text.clear()
            btnFirstItem.isEnabled = true
            btnPrevious.isEnabled = true
        })

        ViewBtn.setOnClickListener(View.OnClickListener {
            ViewBtn.isEnabled = true
            intent = Intent(this, OrderDetailsActicity::class.java)
            startActivity(intent)
        })
        //Code here barcode
        if (AppPreferences.internetConnectionCheck(this)) {
            btnPrevious.isEnabled = true
            addbarcode1!!.requestFocus()
            addbarcode1!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                    var scanbarcodeproduct = addbarcode1!!.text.toString()
                    if (scanbarcodeproduct.trim().isEmpty()) {
                        addbarcode1!!.text.clear()
                        addbarcode1!!.setText("")
                        addbarcode1!!.requestFocus()

                    } else {

                        ScanproducDetails()   // Scan barcode function calling here
                        addbarcode1!!.text.clear()
                        // btnPrevious.isEnabled=true
                    }
                }

                false

            })
        } else {
            //CheckInterNetDailog()
            val dialog = this?.let { Dialog(it) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
            val btDismiss =
                dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
            btDismiss?.setOnClickListener {
                dialog.dismiss()
            }
            dialog?.show()
        }
    }

    private fun PackedOrder() {
        val Dilogview = View.inflate(this@StartPackingOrderList, R.layout.pack_boxpopup, null)
        val builder = AlertDialog.Builder(this@StartPackingOrderList).setView(Dilogview)
        val mSilog1 = builder.show()
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);
        var Count = Dilogview.findViewById<EditText>(R.id.textBoxNumber)
        Count.setText("1")
        var btnPluse = Dilogview.findViewById<Button>(R.id.btnPlus)
        btnPluse.setOnClickListener(View.OnClickListener {
            var valueIncrement = Count.text.trim().toString()
            if (valueIncrement.trim() == "" || valueIncrement.trim() == "0") {
                Count.setText("1")
            } else {
                var shipcount = valueIncrement.toInt()
                var shipcount1 = shipcount + 1
                if (shipcount1 <= Increment) {
                    Count.setText(shipcount1.toString());
                } else {
                    btnPluse.isEnabled = false
                }
            }

        })

        var btnMainus = Dilogview.findViewById<Button>(R.id.btnmainus)
        btnMainus.setOnClickListener(View.OnClickListener {
            var valueIncrement = Count.text.trim().toString()
            var orderstatus = StatusAutoId;
            if (valueIncrement.trim() == "" || (orderstatus != "9" && valueIncrement.trim() == "0")) {
                Count.setText("1")
            } else {
                var shipcount = valueIncrement.toInt()
                if (orderstatus == "9") {
                    if (shipcount >= 1) {
                        shipcount--
                        Count.setText(shipcount.toString());
                    }
                } else {
                    if (shipcount > 1) {
                        shipcount--
                        Count.setText(shipcount.toString());
                    }
                }
            }
        })

        var btnPackedOrder = Dilogview.findViewById<TextView>(R.id.btnPackedOrder)
        btnPackedOrder.requestFocus()
        var btnClose = Dilogview.findViewById<TextView>(R.id.btnClose)

        btnPackedOrder.setOnClickListener(View.OnClickListener {
             //mSilog1.dismiss()
            val Dilogview = View.inflate(this@StartPackingOrderList, R.layout.popupbox_picker, null)
            val builder = AlertDialog.Builder(this@StartPackingOrderList).setView(Dilogview)
            builder.setCancelable(false);
            builder.setCanceledOnTouchOutside(false);
            var textNameWarning = Dilogview.findViewById<TextView>(R.id.StartAndResumPacking)
            var nametext = Dilogview.findViewById<TextView>(R.id.CustomerName)
            var btnYesPack = Dilogview.findViewById<TextView>(R.id.btnYesPack)
            btnYesPack.requestFocus()
            var btnNoCancle = Dilogview.findViewById<TextView>(R.id.btnNoCancle)
            textNameWarning.setText("Are you sure?")
            nametext.setText("You want to Pack Order?")
            val mSilog = builder.show()
            btnYesPack.setOnClickListener(View.OnClickListener {
                if (AppPreferences.internetConnectionCheck(this)) {
                    var valueCount = Count.text.toString()
                    var StatusAutoId = StatusAutoId
                    if (StatusAutoId == "2") {
                        if (valueCount.trim() == "" || valueCount.trim() == "0") {
                            SweetAlertDialog(
                                this,
                                SweetAlertDialog.ERROR_TYPE
                            ).setContentText("No.of boxes required.")
                                .show()
                            Count.text.clear()
                        }
                        else {
                            //here is call valley library

                            val Jsonarra = JSONObject()
                            val JSONObj = JSONObject()
                            val pobj = JSONObject()
                            var pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                            pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
                            pDialog!!.titleText = "Fetching ..."
                            pDialog!!.setCancelable(false)
                            pDialog!!.show()
                            val preferences =
                                PreferenceManager.getDefaultSharedPreferences(this)
                            var accessToken = preferences.getString(
                                "accessToken",
                                ""
                            )
                            JSONObj.put("requestContainer", Jsonarra.put("deviceID", Settings.Secure.getString(this?.contentResolver, Settings.Secure.ANDROID_ID
                                    )
                                )
                            )
//                            val queues = Volley.newRequestQueue(this)
                            JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
                            JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
                            JSONObj.put("pobj", pobj.put("orderAutoId", OrderAutoId))
                            JSONObj.put("pobj", pobj.put("PackerAutoId", PackerAutoid))
                            Log.e("orderAutoId",OrderAutoId.toString())
                            Log.e("PackerAutoid",PackerAutoid.toString())
                            var value = valueCount.toInt()
                            JSONObj.put("pobj", pobj.put("PackedBoxes", value))
                            // Toast.makeText(this, PackerAutoid.toString(), Toast.LENGTH_LONG).show()
                            val queue = Volley.newRequestQueue(this)
                            // Request a string response from the provided URL.
                            val JsonObjectRequest =
                                JsonObjectRequest(Request.Method.POST,
                                    AppPreferences.PACKED_ORDER_PICKERPACKER,
                                    JSONObj,
                                    { response ->
                                        // Display the first 500 characters of the response string.
                                        val resobj = (response.toString())
                                        val responsemsg = JSONObject(resobj.toString())
                                        val resultobj = JSONObject(responsemsg.getString("d"))
                                        val resmsg = resultobj.getString("responseMessage")
                                        val rescode = resultobj.getString("responseCode")
                                        if (rescode=="201") {
                                             SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE).setContentText(resmsg).show()
                                            val intent = Intent(this, OrderDetailsActicity::class.java)
                                            startActivity(intent)
                                            finish()
                                            Count.text.clear()
                                            pDialog!!.dismiss()
                                            mSilog.dismiss()
                                        }

                                        else if (rescode=="202"){
                                            val obj = JSONObject()
                                            var ResponData:JSONArray?=null
                                            ResponData=resultobj.getJSONArray("responseData")
                                            obj.put("data",ResponData)
                                            var message=resultobj.getString("responseMessage")
                                            println(ResponData.length())
                                            //var resData=ResponseData.getJSONObject(1)==null
                                            mSilog.dismiss()
                                           // var mSilog2=builder2.show()
                                            if (ResponData.length()!=null && ResponData.length()>0) {
                                                    val recyclerview= findViewById<RecyclerView>(R.id.StockRecyclerview)
                                                    val layoutManager = LinearLayoutManager(this)
                                                    recyclerview.layoutManager = layoutManager
                                                    val intent = Intent(this, StockNotAvailableList::class.java)
                                                    intent.putExtra("jsonArray", ResponData.toString());
                                                    intent.putExtra("message",message.toString())
                                                   // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent)
                                                    (this as Activity).finish()
                                                    pDialog!!.dismiss()

                                            }
                                            else {
                                                val Dilogview2 = View.inflate(this@StartPackingOrderList, R.layout.stock_list_popup, null)
                                                val builder2 = AlertDialog.Builder(this@StartPackingOrderList).setView(Dilogview2)
                                                var mSilog2=builder2.show()
                                                builder2.setCancelable(false);
                                                builder2.setCanceledOnTouchOutside(false);
                                                mSilog2.show()
                                                var Message2=Dilogview2.findViewById<TextView>(R.id.Message)
                                                var btnOk = Dilogview2.findViewById<TextView>(R.id.btnOk1)
                                                   Message2.setText(message.toString())
                                               // pDialog!!.dismiss()
                                                btnOk!!.setOnClickListener(View.OnClickListener {
                                                    mSilog2.dismiss()
                                                    onBackPressed()
                                                    pDialog!!.dismiss()
                                                    finish()
                                                })
                                            }
                                        }
                                        else {
                                            SweetAlertDialog(
                                                this,
                                                SweetAlertDialog.ERROR_TYPE
                                            ).setContentText(resmsg)
                                                .show()
                                            pDialog!!.dismiss()
                                        }
                                    },

                                    Response.ErrorListener {
                                        pDialog!!.dismiss()
                                    })
                            JsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                                10000000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                            )

// Add the request to the RequestQueue.
                            try {
                                queue.add(JsonObjectRequest)
                            } catch (e: Exception) {
                                Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
                            }
                            mSilog.dismiss()
                        }
                    }
                    else {
                        //here is call valley library
                        val Jsonarra = JSONObject()
                        val JSONObj = JSONObject()
                        val pobj = JSONObject()
                        var pDialog1 = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                        pDialog1!!.progressHelper.barColor = Color.parseColor("#A5DC86")
                        pDialog1!!.titleText = "Fetching ..."
                        pDialog1!!.setCancelable(false)
                        pDialog1!!.setCanceledOnTouchOutside(false);
                        pDialog1!!.show()
                        val preferences =
                            PreferenceManager.getDefaultSharedPreferences(this)
                        var accessToken = preferences.getString(
                            "accessToken",
                            ""
                        )
                        JSONObj.put(
                            "requestContainer",
                            Jsonarra.put(
                                "deviceID",
                                Settings.Secure.getString(
                                    this?.contentResolver,
                                    Settings.Secure.ANDROID_ID
                                )
                            )
                        )
                        val queues = Volley.newRequestQueue(this)
                        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
                        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
                        JSONObj.put("pobj", pobj.put("orderAutoId", OrderAutoId))
                        JSONObj.put("pobj", pobj.put("PackerAutoId", PackerAutoid))
                        Log.e("orderAutoId",OrderAutoId.toString())
                        Log.e("orderAutoId",PackerAutoid.toString())
                        var value = valueCount.toInt()
                        JSONObj.put("pobj", pobj.put("PackedBoxes", value))
                        // Toast.makeText(this, PackerAutoid.toString(), Toast.LENGTH_LONG).show()
                        val queue = Volley.newRequestQueue(this)
                        // Request a string response from the provided URL.
                        val JsonObjectRequest =
                            JsonObjectRequest(Request.Method.POST,
                                AppPreferences.PACKED_ORDER_PICKERPACKER,
                                JSONObj,
                                { response ->
                                    // Display the first 500 characters of the response string.
                                    val resobj = (response.toString())
                                    val responsemsg = JSONObject(resobj.toString())
                                    val resultobj = JSONObject(responsemsg.getString("d"))
                                    val resmsg = resultobj.getString("responseMessage")
                                    val rescode = resultobj.getString("responseCode")
                                    if (rescode == "201") {
                                        // SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE).setContentText(resmsg).show()
                                        val intent = Intent(this, OrderDetailsActicity::class.java)
                                            startActivity(intent)
                                         finish()
                                        Count.text.clear()
                                        pDialog1.dismiss()
                                        mSilog.dismiss()
                                    }
                                      else if (rescode == "202") {
                                       val ResponData=resultobj.getJSONArray("responseData")
                                        var message=resultobj.getString("responseMessage")
                                        //var resData=ResponseData.getJSONObject(1)==null
                                        mSilog.dismiss()
                                        if (ResponData.length()!=null && ResponData.length()>0) {
                                                val intent = Intent(this, StockNotAvailableList::class.java)
                                                intent.putExtra("jsonArray", ResponData.toString());
                                                 intent.putExtra("message", message.toString());
                                                startActivity(intent)
                                            (this as Activity).finish()
                                             pDialog1!!.dismiss()
                                        }
                                        else {
                                            val Dilogview2 = View.inflate(this@StartPackingOrderList, R.layout.stock_list_popup, null)
                                            val builder2 = AlertDialog.Builder(this@StartPackingOrderList).setView(Dilogview2)
                                            var mSilog2=builder2.show()
                                            builder2.setCancelable(false);
                                            builder2.setCanceledOnTouchOutside(false);
                                            mSilog2.show()
                                            var Message2=Dilogview2.findViewById<TextView>(R.id.Message)
                                            var btnOk = Dilogview2.findViewById<TextView>(R.id.btnOk1)
                                            Message2.setText(message.toString())
                                            // pDialog!!.dismiss()
                                            btnOk!!.setOnClickListener(View.OnClickListener {
                                                mSilog2.dismiss()
                                                onBackPressed()
                                                pDialog1.dismiss()
                                                finish()
                                            })
                                        }

                                    } else {
                                        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(resmsg).show()
                                        pDialog1.dismiss()
                                    }
                                },

                                Response.ErrorListener {
                                    pDialog1.dismiss()
                                })
                        JsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                            10000000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )

// Add the request to the RequestQueue.
                        try {
                            queue.add(JsonObjectRequest)
                        } catch (e: Exception) {
                            Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
                        }
                        mSilog.dismiss()
                    }

                } else {
                    val dialog = this?.let { Dialog(it) }
                    dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
                    val btDismiss =
                        dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
                    btDismiss?.setOnClickListener {
                        dialog.dismiss()
                        finish()
                    }
                    dialog?.show()
                }
                mSilog.dismiss()
            })


            btnNoCancle.setOnClickListener(View.OnClickListener { mSilog.dismiss() })

            mSilog1.dismiss()
        })
        btnClose.setOnClickListener(View.OnClickListener { mSilog1.dismiss() })
    }
    private fun PreviousButton() {

        if (itemcount > 1) {
            itemcount--
            ProductDetailFunction()
        }

    }
    private fun SkipAndNextButton() {
        if (itemcount!! >= 0) {
            if (itemcount == TItems) {
                btnSkipAndNext.isEnabled = false
                btnFirstItem.isEnabled = true
            } else {
                // Total=Total
                // itemcount
                itemcount++
                TItems--
                ProductDetailFunction()
            }
        }
    }
    private fun ProductDetailFunction() {
        if (AppPreferences.internetConnectionCheck(this)) {
            //here is call valley library
            val Jsonarra = JSONObject()
            val JSONObj = JSONObject()
            val pobj = JSONObject()
            var pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog!!.titleText = "Fetching ..."
            pDialog!!.setCancelable(false)
            pDialog!!.show()
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            var accessToken =
                preferences.getString("accessToken", "")
            JSONObj.put(
                "requestContainer",
                Jsonarra.put(
                    "deviceID",
                    Settings.Secure.getString(this?.contentResolver, Settings.Secure.ANDROID_ID)
                )
            )

            JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
            JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
            JSONObj.put("pobj", pobj.put("orderAutoId", OrderAutoId))

            if (stusts == "-1") {
                JSONObj.put("pobj", pobj.put("itemcount", stusts));
                stusts = "0";
            } else {
                JSONObj.put("pobj", pobj.put("itemcount", itemcount))
            }
            // Toast.makeText(this,stusts.toString(),Toast.LENGTH_LONG).show()
            val queue = Volley.newRequestQueue(this)
// Request a string response from the provided URL.
            val JsonObjectRequest =
                JsonObjectRequest(Request.Method.POST, AppPreferences.PRODUCT_DETAILS_PACKER,
                    JSONObj, { response ->
                        // Display the first 500 characters of the response string.
                        val resobj = (response.toString())
                        val responsemsg = JSONObject(resobj.toString())
                        val resultobj = JSONObject(responsemsg.getString("d"))
                        val resmsg = resultobj.getString("responseMessage")
                        val rescode = resultobj.getString("responseCode")
                        if (rescode == "200") {
                            //var ShipingQty1:Int?=(OrderQty-ShipingQty)
                            val responsData = resultobj.getJSONArray("responseData")
                            for (i in 0 until responsData.length()) {
                                ProductID = responsData.getJSONObject(i).getInt("PId")
                                itemcount = responsData.getJSONObject(i).getInt("icount")
                                ProductName = responsData.getJSONObject(i).getString("PName")
                                UnitType = responsData.getJSONObject(i).getString("UName")
                                Loctaion = responsData.getJSONObject(i).getString("Loc")
                                TItems = responsData.getJSONObject(i).getInt("TItems")
                                Stock = responsData.getJSONObject(i).getString("Stock")
                                ShipingQty = responsData.getJSONObject(i).getInt("ShipQty")
                                OrderQty = responsData.getJSONObject(i).getInt("RQty")
                                var ProductImage2 = responsData.getJSONObject(i).getString("PImg")
                                isFreeItem = responsData.getJSONObject(i).getInt("isFreeItem")
                                IsExchange = responsData.getJSONObject(i).getInt("IsExchange")
                                PAutoId = responsData.getJSONObject(i).getInt("PAutoId")
                                unitAutoId = responsData.getJSONObject(i).getInt("UAutoId")
                                Barcode = responsData.getJSONObject(i).getString("Barcode")
                                var PackStatus = responsData.getJSONObject(i).getInt("PackStatus")
                                PackStatus1 = PackStatus
                                remainingQty = (OrderQty!!.toInt() - ShipingQty!!.toInt())
                                ProductIDText.setText(ProductID.toString())
                                ProductNameText.setText(ProductName)
                                UnitTypeText.setText(UnitType.toString())
                                LoctaionText.setText(Loctaion)
                                ProductQtyText.setText(itemcount.toString() + " Out of " + TItems.toString())

                                if (isFreeItem == 1) {
                                    changeStatusProduct.visibility = View.VISIBLE
                                    Yeschange.visibility = View.VISIBLE
                                    changeStatusProduct.setText("Is Free : ")
                                } else if (IsExchange == 1) {
                                    changeStatusProduct.visibility = View.VISIBLE
                                    Yeschange.visibility = View.VISIBLE
                                    changeStatusProduct.setText("Is Exchange : ")
                                } else {
                                    changeStatusProduct.visibility = View.GONE
                                    Yeschange.visibility = View.GONE
                                }

                                if (itemcount == TItems) {
                                    btnSaveNext.setText("Save")
                                    //btnSaveNext.isEnabled=false
                                } else {
                                    btnSaveNext.setText("Save & Next")
                                }
                                StockText.setText(Stock.toString())
                                OrderQtyText.setText(OrderQty.toString())
                                ShipingQtyText.setText(ShipingQty.toString())
                                RemainingQtyText.setText(remainingQty.toString())
                                textViewBarcode.setText(Barcode.toString())
                                et.setText(ShipingQty.toString())

                                if (ProductImage2 != "") {
                                    //Picasso.get().load(ProductImage2).into(ProductImage)
                                    Picasso.get().load(ProductImage2).error(R.drawable.default_pic)
                                        .into(ProductImage);
                                }
                            }

                            pDialog!!.dismiss()
                        } else {
                            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(
                                resmsg
                            ).show()
                            pDialog!!.dismiss()
                        }
                    },

                    Response.ErrorListener {
                        pDialog!!.dismiss()
                    })
            JsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

// Add the request to the RequestQueue.
            try {
                queue.add(JsonObjectRequest)
            } catch (e: Exception) {
                Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
            }

        } else {
            val dialog = this?.let { Dialog(it) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
            val btDismiss =
                dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
            btDismiss?.setOnClickListener {
                dialog.dismiss()
                finish()
            }
            dialog?.show()
        }
        btnFirstItem.isEnabled = false
        btnPrevious.isEnabled = true
    }

    fun ScanproducDetails() {
        if (AppPreferences.internetConnectionCheck(this)) {
            var OrderNo: String? = null
            // var addbarcode: String? = null
            var addbarcode1: EditText = findViewById(R.id.ScanBarcode)
            addbarcode = addbarcode1.text.toString()
            val preferencesid =
                PreferenceManager.getDefaultSharedPreferences(this@StartPackingOrderList)
            OrderAutoId = (preferencesid.getString("OrderAutoid", OrderAutoId.toString()))
            OrderNo1.setText("Order No : " + preferencesid.getString("OrderNo", OrderNo.toString()))
            val Jsonarra = JSONObject()
            val JSONObj = JSONObject()
            val pobj = JSONObject()
            val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog.titleText = "Fetching ..."
            pDialog.setCancelable(false)
            pDialog.show()
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            var accessToken = preferences.getString("accessToken", "")
            JSONObj.put(
                "requestContainer",
                Jsonarra.put(
                    "deviceID",
                    Settings.Secure.getString(this?.contentResolver, Settings.Secure.ANDROID_ID)
                )
            )
            val queues = Volley.newRequestQueue(this)
            JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
            JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
            JSONObj.put("pobj", pobj.put("orderAutoId", OrderAutoId))
            JSONObj.put("pobj", pobj.put("barcode", addbarcode))
            JSONObj.put("pobj", pobj.put("productAutoId", PAutoId))
            //JSONObj.put("pobj", pobj.put("itemcount", itemcount))
            JSONObj.put("pobj", pobj.put("unitAutoId", unitAutoId))
            JSONObj.put("pobj", pobj.put("isFreeItem", isFreeItem))
            JSONObj.put("pobj", pobj.put("IsExchange", IsExchange))
           // Toast.makeText(this, OrderAutoId.toString(), Toast.LENGTH_LONG).show()
            Log.e("unitAutoId",unitAutoId.toString())
            Log.e("ProductID",ProductID.toString())
            Log.e("Barcode",Barcode.toString())
            Log.e("PAutoId",PAutoId.toString())
            Log.e("OrderAutoId",PAutoId.toString())
            val POADDPRODUCT = JsonObjectRequest(
                Request.Method.POST,
                AppPreferences.SCAN_BARCODE_PRODUCT_DETAILS_PICKER_PACKER,
                JSONObj,
                Response.Listener { response ->
                    val resobj = (response.toString())
                    val responsemsg = JSONObject(resobj.toString())
                    val resultobj = JSONObject(responsemsg.getString("d"))
                    val resmsg = resultobj.getString("responseMessage")
                    val rescode = resultobj.getString("responseCode")
                    System.out.println(resmsg)
                    if (rescode == "200") {

                        val responsData = resultobj.getJSONArray("responseData")
                        for (i in 0 until responsData.length()) {
                            ProductID = responsData.getJSONObject(i).getInt("PId")
                            itemcount = responsData.getJSONObject(i).getInt("icount")
                            ProductName = responsData.getJSONObject(i).getString("PName")
                            UnitType = responsData.getJSONObject(i).getString("UName")
                            unitAutoId = responsData.getJSONObject(i).getInt("UAutoId")
                            Loctaion = responsData.getJSONObject(i).getString("location")
                            TItems = responsData.getJSONObject(i).getInt("TItems")
                            Stock = responsData.getJSONObject(i).getString("Stock")
                            ShipingQty = responsData.getJSONObject(i).getInt("ShipQty")
                            OrderQty = responsData.getJSONObject(i).getInt("RQty")
                            var ProductImage2 = responsData.getJSONObject(i).getString("PImg")
                            isFreeItem = responsData.getJSONObject(i).getInt("isFreeItem")
                            IsExchange = responsData.getJSONObject(i).getInt("IsExchange")
                            Barcode = responsData.getJSONObject(i).getString("Barcode")
                            PAutoId = responsData.getJSONObject(i).getInt("PAutoId")
                            var PackStatus = responsData.getJSONObject(i).getInt("PackStatus")
                            PackStatus1 = PackStatus
                            var remainingQty = (OrderQty!!.toInt() - ShipingQty.toInt())
                            ProductIDText.setText(ProductID.toString())
                            ProductNameText.setText(ProductName)
                            UnitTypeText.setText(UnitType)
                            LoctaionText.setText(Loctaion)
                            ProductQtyText.setText(itemcount.toString() + " Out of " + TItems.toString())
                            StockText.setText(Stock)
                            OrderQtyText.setText(OrderQty.toString())
                            ShipingQtyText.setText(ShipingQty.toString())
                            RemainingQtyText.setText(remainingQty.toString())
                            textViewBarcode.setText(Barcode.toString())

                            et.setText(ShipingQty.toString())
                            if (isFreeItem == 1) {
                                changeStatusProduct.visibility = View.VISIBLE
                                Yeschange.visibility = View.VISIBLE
                                changeStatusProduct.setText("Is Free : ")
                            } else if (IsExchange == 1) {
                                changeStatusProduct.visibility = View.VISIBLE
                                Yeschange.visibility = View.VISIBLE
                                changeStatusProduct.setText("Is Exchange : ")
                            } else {
                                changeStatusProduct.visibility = View.GONE
                                Yeschange.visibility = View.GONE
                            }
                            if (itemcount == TItems) {
                                btnSaveNext.setText("Save")
                                //btnSaveNext.isEnabled=false
                            } else {
                                btnSaveNext.setText("Save & Next")
                            }
                            if (ProductImage2 != "") {
                                //  Picasso.get().load(ProductImage2).into(ProductImage)
                                Picasso.get().load(ProductImage2).error(R.drawable.default_pic)
                                    .into(ProductImage);
                            }
                        }
                        //Toast.makeText(this,itemcount.toString(),Toast.LENGTH_LONG).show()
                        pDialog.dismiss()
                        addbarcode1!!.requestFocus()

                    } else if (rescode == "202") {

                        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(resmsg)
                            .show()
                        pDialog.dismiss()
                        //Toast.makeText(this,UnitType.toString(),Toast.LENGTH_LONG).show()
                        addbarcode1!!.requestFocus()

                    } else {
                        SweetAlertDialog(
                            this,
                            SweetAlertDialog.ERROR_TYPE
                        ).setContentText(UnitType.toString()).show()
                        pDialog.dismiss()
                        addbarcode1!!.requestFocus()

                    }
                },
                Response.ErrorListener { response ->
                    Log.e("onError", error(response.toString()))
                })
            POADDPRODUCT.retryPolicy = DefaultRetryPolicy(
                1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            queues.add(POADDPRODUCT)
        } else {
            val dialog = this?.let { Dialog(it) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
            val btDismiss =
                dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
            btDismiss?.setOnClickListener {
                dialog.dismiss()
                finish()
            }
            dialog?.show()

        }
        btnPrevious.isEnabled = true
    }

}








