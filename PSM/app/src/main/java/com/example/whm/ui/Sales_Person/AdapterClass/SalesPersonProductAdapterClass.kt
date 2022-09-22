package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.AddToCartDataModle
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.SalesPersonProductModel
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.getCartdetailsModle
import com.example.whm.ui.Sales_Person.SalesPersonProductList
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Field
import java.util.regex.Pattern


class SalesPersonProductAdapterClass(
    private val ProductItemList: List<SalesPersonProductModel>,
    var data: Context?
) : RecyclerView.Adapter<SalesPersonProductAdapterClass.ViewHolder>(),View.OnClickListener
{
    var responseResultData = JSONArray()
    var cartResponseResultData = JSONArray()
    lateinit var spineer: Spinner
    lateinit var Price: EditText
    lateinit var isFreeCheckBox :CheckBox
    lateinit var isExchangeCheckBox:CheckBox
    lateinit var discountAmount:EditText
    lateinit var discountPercent:EditText
    lateinit var valueIncrementDecrement:EditText
    lateinit var decrementBtn:Button
    lateinit var incrementBtn:Button
    var mylist = ArrayList<String>()
    var mylist1 = ArrayList<String>()
    var minPriceslist = ArrayList<String>()
    var isFreelist = ArrayList<String>()
    var isdefault = ArrayList<Int>()
    var unitAutoIdList = ArrayList<Int>()

    var minPrice:String?=null
    var checkFreeValue:Int=0
    var isExchangeValue:Int=0
    var checkExchangeValue:Int=0
    var draftAutoId:Int=0
    var UIDs: Int? = null
    var DraftAutoId: Int? = null
    var Total:String?=null
    var NofItem:String?=null
    var frees: Int? = null
    var MinPric: String? = null
    var isdefault1: Int? = null
    var UName: String? = null
    var price: String? = null
    var pricess: String? = null
    var minPrices: String? = null
    var unitAutoID:Int?=null
    var unitAutoidValue:Int?=null
    var isFree: String? = null
    var isFrees:String?=null
    var priceValue: Double? = null
    var uah:Float=0.0F
    var usd:Float= 0.0F
    var uahEdited = false
    var usdEdited = false

    var getcartDetailsdata: MutableList<getCartdetailsModle> = ArrayList()
    var addToCartData: MutableList<AddToCartDataModle> = ArrayList()

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){
        var ProductName = itemView.findViewById<TextView>(R.id.productIdSalse)
        var ProductdID = itemView.findViewById<TextView>(R.id.ProductdID)
        var ProductPrice = itemView.findViewById<TextView>(R.id.ProductPrice)
        var ProductStock = itemView.findViewById<TextView>(R.id.ProductStock)
        var ProductImage = itemView.findViewById<ImageView>(R.id.ProductImage)
        var cardView5 = itemView.findViewById<CardView>(R.id.cardView5)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        var view = LayoutInflater.from(parent.context).inflate(R.layout.product_list, parent, false)
        //title.setText("hello")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var ProductItem = ProductItemList[position]
        holder.ProductName.text = ProductItem.getPName().toString()
        holder.ProductdID.text = ProductItem.getPId().toString()
        var priceProduct = ProductItem.getBP()!!.toFloat()
        var UnitType = ProductItem.getUnitType().toString()
        holder.ProductPrice.setText("$" + "%.2f".format(priceProduct) + "(" + UnitType + ")")
        var productStoct = ProductItem.getCStock()
        if (productStoct=="0") {
            holder.ProductStock.setText("Stock : " + productStoct.toString())
            holder.ProductStock.setTextColor(Color.parseColor("#DC3545"))
        } else {
            holder.ProductStock.setText("Stock : " + productStoct.toString())
            holder.ProductStock.setTextColor(Color.parseColor("#000000"))
        }
        Picasso.get().load(ProductItem.getImageUrl()).error(R.drawable.default_pic)
            .into(holder.ProductImage);

        holder.cardView5.setOnClickListener(View.OnClickListener {
            if (AppPreferences.internetConnectionCheck(it.context)){
            val preferences = PreferenceManager.getDefaultSharedPreferences(data)
            var accessToken = preferences.getString("accessToken", "")
            var empautoid = preferences.getString("EmpAutoId", "")
            var customerId = preferences.getString("customerId", "")
            var pDialog = SweetAlertDialog(data, SweetAlertDialog.PROGRESS_TYPE)
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
                    Settings.Secure.getString(data!!.contentResolver, Settings.Secure.ANDROID_ID)
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
            sendRequestObject.put("pObj", pObj.put("productId", ProductItem.getPId()))
            sendRequestObject.put("pObj", pObj.put("customerId", customerId))
            //send request queue in vally
            val queue = Volley.newRequestQueue(data)
            val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
                AppPreferences.getpackingdetails, sendRequestObject,
                { response ->
                    val responseResult = JSONObject(response.toString())
                    val responsedData = JSONObject(responseResult.getString("d"))
                    var responseMessage = responsedData.getString("responseMessage")
                    val responseCode = responsedData.getString("responseCode")
                    if (responseCode == "201") {
                        responseResultData = responsedData.getJSONArray("responseData")
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
                                unitAutoID=getcartDetailsdata[n].getuiDs()
                                unitAutoID?.let { it1 -> unitAutoIdList.add(it1) }
                                isFreelist.add(isFree.toString())
                                mylist1.add(pricess.toString())
                                minPriceslist.add(minPrices.toString())
                                var isdefault1 = getcartDetailsdata[n].getisdefault()
                                isdefault.add(isdefault1!!.toInt())
                                var UName = getcartDetailsdata[n].getuName()
                                mylist.add(UName.toString())
                            }
                            var adapter = ArrayAdapter<String>(
                                it.context,
                                R.layout.support_simple_spinner_dropdown_item,
                                mylist
                            )
                            spineer.adapter = adapter
                            for (i in 0..isdefault.size - 1){
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
                                        id: Long
                                    ) {
                                        val item2: String = mylist1[position]
                                         minPrice=minPriceslist[position]
                                         isFrees=isFreelist[position]
                                        unitAutoidValue=unitAutoIdList[position]
                                        Log.e("isFrees",isFrees.toString())
                                        priceValue=item2.toDouble()
                                        Price.setText("%.2f".format(priceValue))
                                        Log.e("Price",Price.text.toString())
                                        if (isFreeCheckBox.isChecked) {
                                            isFreeCheckBox.toggle()
                                            Price.isEnabled = true
                                            discountAmount.isEnabled = true
                                            discountPercent.isEnabled = true
                                            Price.setBackgroundResource(R.drawable.borderline)
                                            discountAmount.setBackgroundResource(R.drawable.borderline)
                                            discountPercent.setBackgroundResource(R.drawable.borderline)
                                        }
                                        if(isExchangeCheckBox.isChecked){
                                           isExchangeCheckBox.toggle()
                                            Price.isEnabled = true
                                            discountAmount.isEnabled = true
                                            discountPercent.isEnabled = true
                                            Price.setBackgroundResource(R.drawable.borderline)
                                            discountAmount.setBackgroundResource(R.drawable.borderline)
                                            discountPercent.setBackgroundResource(R.drawable.borderline)
                                        }
                                        var free=isFrees.toString().toInt()
                                        if (free==1){
                                            isFreeCheckBox.setEnabled(true)
                                        }
                                        else{
                                            isFreeCheckBox.setEnabled(false)
                                        }
                                    }
                                }
                            listDropdown(spineer)

                            pDialog!!.dismiss()
                        }

                        else {
                            var popUp = SweetAlertDialog(data, SweetAlertDialog.WARNING_TYPE)
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
                    }
                    else {
                        var popUp = SweetAlertDialog(data, SweetAlertDialog.WARNING_TYPE)
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
                Toast.makeText(data, e.toString(), Toast.LENGTH_LONG).show()
            }
            }
            else{
                val dialog = data?.let { Dialog(it) }
                dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
                val btDismiss =
                    dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
                btDismiss?.setOnClickListener {
                    dialog.dismiss()
                    val intent = Intent(data, MainActivity2::class.java)
                    data?.startActivity(intent)
                              finish()

                }
                dialog?.show()
            }

            var dilog = Dialog(it.context)
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
            discountAmount =dilog.findViewById<EditText>(R.id.TaxAmount);
            discountPercent =dilog.findViewById<EditText>(R.id.AmountP);
            discountPercent =dilog.findViewById<EditText>(R.id.AmountP);
            valueIncrementDecrement =dilog.findViewById<EditText>(R.id.valueIncrementDecrement);
            incrementBtn =dilog.findViewById<Button>(R.id.incrementBtn);
            decrementBtn =dilog.findViewById<Button>(R.id.decrementBtn);
            Price = dilog.findViewById<EditText>(R.id.Price)
            isFreeCheckBox = dilog.findViewById<CheckBox>(R.id.isFreeCheckBox)
            isExchangeCheckBox = dilog.findViewById<CheckBox>(R.id.isExchangeCheckBox)
            var imageView13 = dilog.findViewById<ImageView>(R.id.imageView13)
            spineer = dilog.findViewById<Spinner>(R.id.spineer) as Spinner
            var value = valueIncrementDecrement.text.trim().toString()
            var countvalue = value.toInt()
            incrementBtn.setOnClickListener(View.OnClickListener {
                if (value.trim() == "" || value.trim() == "0") {
                    valueIncrementDecrement.setText("1")
                } else {
                    countvalue = countvalue + 1
                    valueIncrementDecrement.setText(countvalue.toString())
                }
            })
            decrementBtn.setOnClickListener(View.OnClickListener {
                if (value.trim() == "" || value.trim() == "0" || value.trim() == "0") {
                    valueIncrementDecrement.setText("1")
                } else {

                    if (countvalue>1) {
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
                    after: Int
                ) {
                    if (!usdEdited) {
                        uahEdited = true

                    }
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val tmp = discountAmount.text.toString()
                    val tmps = minPrice.toString()
                    val temsprie=Price.text.toString()
                    if (!tmp.isEmpty() && uahEdited  && tmp!="." &&tmps!=null && tmps!="." &&temsprie!="" &&temsprie!=null &&tmp!="0.00" ) {
                        uah = tmp.toFloat()
                        val price =temsprie.toFloat()
                        val minprice=tmps.toFloat()
                        var Amountdiscount:Float=price-uah
                        if (uah>price){
                            discountAmount.text.replace(0,discountAmount.text.length,"0.00")
                        }
                        if(minprice<=Amountdiscount){
                            usd = uah * 100 / price
                            discountPercent.setText("%.2f".format(usd))
                        }else{
                            var dilog=Dialog(it.context)
                            dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dilog.setCancelable(false)
                            dilog.setContentView(R.layout.success_message_popup)
                            dilog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dilog.window!!.setGravity(Gravity.CENTER)
                            val lp = WindowManager.LayoutParams()
                            lp.copyFrom(dilog.getWindow()!!.getAttributes())
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT
                            var customername=dilog.findViewById<TextView>(R.id.messagetitle)
                            var btnOk=dilog.findViewById<TextView>(R.id.btnOk)
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
                    after: Int
                ) {
                    if (!uahEdited) {
                        usdEdited = true

                    }
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val tmp = discountPercent.text.toString()
                    val tmps = Price.text.toString()
                    if (!tmp.isEmpty() && usdEdited  && tmp!="." && tmps!=null &&tmps!="" && tmps!="." &&tmp!="0.00") {

                         usd =tmp.toFloat()
                        val price =tmps.toFloat()
                        val minprice= minPrice.toString().toFloat()
                        var Amountdiscount:Float=price-usd*100/100
                        if (usd>100){
                            discountPercent.text.replace(0,discountPercent.text.length,"0.00")
                        }
                        if(minprice<=Amountdiscount) {
                            uah = usd * price / 100
                            discountAmount.setText("%.2f".format(uah))
                        }else{
                            var dilog=Dialog(it.context)
                            dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dilog.setCancelable(false)
                            dilog.setContentView(R.layout.success_message_popup)
                            dilog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dilog.window!!.setGravity(Gravity.CENTER)
                            val lp = WindowManager.LayoutParams()
                            lp.copyFrom(dilog.getWindow()!!.getAttributes())
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT
                            var customername=dilog.findViewById<TextView>(R.id.messagetitle)
                            var Title=dilog.findViewById<TextView>(R.id.Title)
                               Title.visibility=View.GONE
                            var btnOk=dilog.findViewById<TextView>(R.id.btnOk)
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
            SProductID.setText(ProductItem.getPId())
            s_ProductName.setText(ProductItem.getPName())
            stockProductS.setText("Stock : " + ProductItem.getCStock().toString())
            Picasso.get().load(ProductItem.getImageUrl()).error(R.drawable.default_pic).into(imageView13);
            var btnAddToCart = dilog.findViewById<Button>(R.id.btnAddToCart)
            var btnCloseCart = dilog.findViewById<Button>(R.id.btnCloseCart)
            btnAddToCart.setOnClickListener(View.OnClickListener {
                if (AppPreferences.internetConnectionCheck(it.context)) {
                    val preferences = PreferenceManager.getDefaultSharedPreferences(data)
                    var accessToken = preferences.getString("accessToken", "")
                    var empautoid = preferences.getString("EmpAutoId", "")
                    var customerAutoId = preferences.getString("customerAutoId", "")
                    var pDialog = SweetAlertDialog(data, SweetAlertDialog.PROGRESS_TYPE)
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
                                data!!.contentResolver,
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
                    sendRequestObject.put("pObj", pObj.put("productId", ProductItem.getPId()))
                    sendRequestObject.put("pObj", pObj.put("CustomerAutoId", customerAutoId))
                    sendRequestObject.put("pObj", pObj.put("unitAutoId", unitAutoidValue))
                    sendRequestObject.put("pObj", pObj.put("isFree", checkFreeValue))
                    sendRequestObject.put("pObj", pObj.put("isExchange", isExchangeValue))
                    sendRequestObject.put("pObj", pObj.put("ReqQty", valueIncrementDecrement.text.toString()))
                    sendRequestObject.put("pObj", pObj.put("unitPrice", Price.text.toString()))
                    sendRequestObject.put("pObj", pObj.put("Oim_Discount", discountPercent.text.toString()))
                    sendRequestObject.put("pObj", pObj.put("Oim_DiscountAmount", discountAmount.text.toString()))
                    sendRequestObject.put("pObj", pObj.put("draftAutoId", draftAutoId))
                    //send request queue in vally
                    val queue = Volley.newRequestQueue(data)
                    val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
                        AppPreferences.addToCartAPI, sendRequestObject,
                        { response ->
                            val responseResult = JSONObject(response.toString())
                            val responsedData = JSONObject(responseResult.getString("d"))
                            var responseMessage = responsedData.getString("responseMessage")
                            val responseStatus = responsedData.getString("responseStatus")
                            if (responseStatus == "200") {
                                cartResponseResultData = responsedData.getJSONArray("responseData")
                                addToCartData.clear()
                                if (cartResponseResultData.length() != null && cartResponseResultData.length() > 0) {
                                    for (i in 0 until cartResponseResultData.length()) {

                                        DraftAutoId= cartResponseResultData.getJSONObject(i).getInt("DraftAutoId")
                                        Total= cartResponseResultData.getJSONObject(i).getString("Total")
                                        NofItem= cartResponseResultData.getJSONObject(i).getString("NooofItem")
                                        var cartData=AddToCartDataModle(DraftAutoId!!,Total!!,NofItem!!)
                                        addToCartData.add(cartData)
                                    }
                                            Log.e("DraftAutoId",DraftAutoId.toString())
                                            Log.e("Total",Total.toString())
                                            Log.e("NofItem",NofItem.toString())
                                    val intent1 = Intent("USER_NAME_CHANGED_ACTION")
                                    intent1.putExtra("NofItem",NofItem)
                                     // put your all data using put extra
                                     // put your all data using put extra
                                    LocalBroadcastManager.getInstance(it.context).sendBroadcast(intent1)

                                    pDialog.dismiss()
                                    val intent = Intent(data, SalesPersonProductList::class.java)
                                    data?.startActivity(intent)
                                    dilog.dismiss()
                                    finish()
                                    checkFreeValue = 0
                                    isExchangeValue = 0
                                }
                                else{
                                    var popUp = SweetAlertDialog(data, SweetAlertDialog.WARNING_TYPE)
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
                            }
                            else{
                                var popUp = SweetAlertDialog(data, SweetAlertDialog.WARNING_TYPE)
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
                        Toast.makeText(data, e.toString(), Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    val dialog = data?.let { Dialog(it) }
                    dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
                    val btDismiss =
                        dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
                    btDismiss?.setOnClickListener {
                        dialog.dismiss()
                        val intent = Intent(data, MainActivity2::class.java)
                        data?.startActivity(intent)
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
        })

    }

    private fun finish() {
    }

    override fun onClick(Box: View?) {
        Box as CheckBox
        var isChecked:Boolean=Box.isChecked
        when(Box.id){
            R.id.isFreeCheckBox->if(isChecked){
                checkFreeValue =1
                isFreeCheckedfunction(true)
            }
            else{
                isFreeUnCheckedFunction()
            }
            R.id.isExchangeCheckBox->if(isChecked){
                isExchangeValue=1
                isFreeCheckedfunction(isFreeChecked = false)
            }else{
                isFreeUnCheckedFunction()

            }

        }

    }
    private fun isFreeCheckedfunction(isFreeChecked: Boolean) {
        if(isFreeChecked) {
            isExchangeCheckBox.isChecked = false

        }
        else {
            isFreeCheckBox.isChecked=false
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
        Price.isEnabled=true
        discountAmount.isEnabled=true
        discountPercent.isEnabled=true
        Price.setBackgroundResource(R.drawable.borderline)
        discountAmount.setBackgroundResource(R.drawable.borderline)
        discountPercent.setBackgroundResource(R.drawable.borderline)
        Price.setText("%.2f".format(priceValue))
        discountAmount.setText("%.2f".format(0.00))
        discountPercent.setText("%.2f".format(0.00))
    }

    private fun listDropdown(spineer: Spinner) {
        val popup: Field = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val popupWindow: ListPopupWindow = popup.get(spineer) as ListPopupWindow
        popupWindow.height = (200).toInt()
    }

    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {
        //                                             digitsBeforeZero  or       digitsBeforeZero + dot + digitsAfterZero
        private val pattern = Pattern.compile("(\\d{0,$digitsBeforeZero})|(\\d{0,$digitsBeforeZero}\\.\\d{0,$digitsAfterZero})")
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
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

    override fun getItemCount(): Int {
        return ProductItemList.size
    }

}



