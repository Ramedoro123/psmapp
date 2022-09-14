package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.SalesPersonProductModel
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.getCartdetailsModle
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class SalesPersonProductAdapterClass(
    private val ProductItemList: List<SalesPersonProductModel>,
    var data: Context?
) : RecyclerView.Adapter<SalesPersonProductAdapterClass.ViewHolder>() {
    var responseResultData = JSONArray()
    lateinit var spineer: Spinner
    lateinit var Price: TextView
    lateinit var TaxAmount: EditText
    lateinit var AmountP: EditText
    var isdefault: Int? = null
    var UName = arrayOf(String())
    var UID = intArrayOf()
    var free = intArrayOf()
    var price:Double?=null
    var getcartDetailsdata: ArrayList<getCartdetailsModle> = arrayListOf()
    var numbers = intArrayOf()
    var MinPrice = intArrayOf()

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var ProductName = itemView.findViewById<TextView>(R.id.productIdSalse)
        var ProductdID = itemView.findViewById<TextView>(R.id.ProductdID)
        var ProductPrice = itemView.findViewById<TextView>(R.id.ProductPrice)
        var ProductStock = itemView.findViewById<TextView>(R.id.ProductStock)
        var ProductImage = itemView.findViewById<ImageView>(R.id.ProductImage)
        var cardView5 = itemView.findViewById<CardView>(R.id.cardView5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
        var productStoct = ProductItem.getCStock()!!.toInt()
        if (productStoct == 0) {
            holder.ProductStock.setText("Stock : " + productStoct.toString())
            holder.ProductStock.setTextColor(Color.parseColor("#DC3545"))
        } else {
            holder.ProductStock.setText("Stock : " + productStoct.toString())
            holder.ProductStock.setTextColor(Color.parseColor("#000000"))
        }
        Picasso.get().load(ProductItem.getImageUrl()).error(R.drawable.default_pic)
            .into(holder.ProductImage);

        holder.cardView5.setOnClickListener(View.OnClickListener {
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
                        if (responseResultData.length() != null && responseResultData.length() > 0)
                        {
                            for (i in 0 until responseResultData.length()) {
                                var UIDs = responseResultData.getJSONObject(i).getInt("UID")
                                UID = IntArray(UIDs)
                               var UName =responseResultData.getJSONObject(i).getString("UName")

                                var frees = responseResultData.getJSONObject(i).getInt("Free")
                                free = IntArray(frees)
                                price =responseResultData.getJSONObject(i).getDouble("price")

                                var MinPrices = responseResultData.getJSONObject(i).getInt("MinPrice")
                                MinPrice = IntArray(MinPrices)
                                var isdefault = responseResultData.getJSONObject(i).getInt("isdefault")
                                numbers = IntArray(isdefault)
                                //     names= arrayOf(UName)
//                                 Free=price.toFloat()
                                Price.setText("%.2f".format(price).toString())
                                cartDetailsdata(UIDs,UName,frees, price!!,MinPrices,isdefault)
                            }

                                Log.e("UID",UID.count().toString())
                            SpinnerValue()
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
                        pDialog!!.dismiss()
                    } else {

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
            Price = dilog.findViewById<EditText>(R.id.Price)
            TaxAmount = dilog.findViewById<EditText>(R.id.TaxAmount)
            AmountP = dilog.findViewById<EditText>(R.id.AmountP)
            var amout = TaxAmount.text.trim().toString()
            if (amout == "" || amout == "0") {
                TaxAmount.setText("0.00")
            } else {
                TaxAmount.setText(amout.toString())
            }
            var imageView13 = dilog.findViewById<ImageView>(R.id.imageView13)
            spineer = dilog.findViewById<Spinner>(R.id.spineer) as Spinner
            SProductID.setText(ProductItem.getPId())
            s_ProductName.setText(ProductItem.getPName())
            stockProductS.setText("Stock : " + ProductItem.getCStock().toString())

            Picasso.get().load(ProductItem.getImageUrl()).error(R.drawable.default_pic)
                .into(imageView13);
            var btnCancle = dilog.findViewById<TextView>(R.id.textView33)

            btnCancle.setOnClickListener(View.OnClickListener {
                dilog.dismiss()
            })
            dilog.show()
            dilog.getWindow()!!.setAttributes(lp);
        })



    }

    private fun cartDetailsdata(uiDs: Int, uName:String, frees: Int, price: Double, minPrices: Int, isdefault: Int) {
        var CartDatalist = getCartdetailsModle(uiDs,uName,frees,price,minPrices,isdefault)
          getcartDetailsdata.add(CartDatalist)
    }

    private fun SpinnerValue() {
        var array1: Array<String?> = emptyArray()

        val adapter: ArrayAdapter<String?> = object :
            ArrayAdapter<String?>(
                data!!.applicationContext,
                android.R.layout.simple_spinner_dropdown_item
            ) {
            override fun getView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val v = super.getView(position, convertView, parent)
                if (position == count) {
                    (v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                    (v.findViewById<View>(android.R.id.text1) as TextView).text = getItem(count) //"Hint to be displayed"

                }
                return v
            }

            override fun getCount(): Int {
                return super.getCount()-1// you dont display last item. It is used as hint.
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        var Unames: String? = null
        var Number:Int=1
        for (i in 0 until responseResultData.length()) {
             var isdefault = responseResultData.getJSONObject(i).getInt("isdefault")
            if (Number==isdefault) {
                Unames = responseResultData.getJSONObject(i).getString("UName")
                adapter.add(Unames.toString())
            }else{
                var Unamess = responseResultData.getJSONObject(i).getString("UName")
                adapter.add(Unamess.toString())
            }
        }

        adapter.add(Unames)
        spineer.adapter = adapter
        spineer.setSelection(adapter.count)
        //      spineer.setSelection(i);//to set default values
//        spineer.setSelection(adapter.count) //set the hint the default selection so it appears on launch.

    }

    override fun getItemCount(): Int {
        var CustomerlistSize = ProductItemList.size.toString()
        val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(data)
        val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
        sharedLoadOrderPage.putString("ProductItemList", CustomerlistSize)
        sharedLoadOrderPage.apply()
//        if(ProductItemList.size!=0){
//            (data as AppCompatActivity)!!.supportActionBar!!.setWindowTitle("Add-On("+ProductItemList.size+")")
//        }else{
//            (data as AppCompatActivity)!!.supportActionBar!!.title = "Add-On("+ProductItemList.size+")"
//
//        }
        return ProductItemList.size
    }

}