package com.example.whm.ui.Piker_And_Packer_OrderList

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
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
import com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.AdapterClass.Add_OnAdapter
import com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.ModelClass.ModelClassOrderLIst
import org.json.JSONObject

class Add_On_Order_Fragment : Fragment() {
    lateinit var mView: View
    var shouldExecuteOnResume = false
    var orderNotAvailable: TextView? = null
    private val orderList = ArrayList<ModelClassOrderLIst>()
    private lateinit var AddonlistAdapter: Add_OnAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

     val view=  inflater.inflate(R.layout.fragment_add__on__order_, container, false)
        orderNotAvailable= view.findViewById(R.id.orderNotAvailable)
//        if (orderList.size!=0) {
//        }else{
//            var orderNotAvailable:TextView= view.findViewById(R.id.orderNotAvailable)
//            orderNotAvailable!!.text = "No order available"
//            orderNotAvailable.visibility=View.VISIBLE
//        }
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                }
            }
            true
        })
        shouldExecuteOnResume = false
        val recyclerView: RecyclerView = view.findViewById(R.id.Add_On_OrderListFragment_Packer)
        val sharedUnloadOrderPreferences =
            PreferenceManager.getDefaultSharedPreferences(this.context)
        var UnloadOrderCount = sharedUnloadOrderPreferences.getString("UnloadOrder", "[0]")
        setHasOptionsMenu(true)
        AddonlistAdapter = Add_OnAdapter(orderList, this.context)
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = AddonlistAdapter
        addOneList()
        return view
    }
    @Override

    private fun addOneList() {

        if (AppPreferences.internetConnectionCheck(this.context)) {
            //here is call valley library
            val Jsonarra = JSONObject()
            val JSONObj = JSONObject()
            val pobj = JSONObject()
            val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog.titleText = "Fetching ..."
            pDialog.setCancelable(false)
            pDialog.show()
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            var accessToken = preferences.getString("accessToken", "")
            var empautoid = preferences.getString("EmpAutoId", "")
           // var StatusAutoId=preferences.getString("StatusAutoId","")
            JSONObj.put(
                "requestContainer", Jsonarra.put(
                    "deviceID",
                    Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
                )
            )
            val queues = Volley.newRequestQueue(this.context)
            var StatusAutoId:Int=9
            JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
            JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
            JSONObj.put("requestContainer", Jsonarra.put("userAutoId", empautoid))
            JSONObj.put("pobj", pobj.put("status", StatusAutoId))

// Request a string response from the provided URL.
            val JsonObjectRequest =
                JsonObjectRequest(
                    Request.Method.POST, AppPreferences.API_PICKER_AND_PACKER,
                    JSONObj, { response ->
                        orderList.clear()
                        // Display the first 500 characters of the response string.
                        val resobj = (response.toString())
                        val responsemsg = JSONObject(resobj.toString())
                        val resultobj = JSONObject(responsemsg.getString("d"))
                        val resmsg = resultobj.getString("responseMessage")
                        val rescode = resultobj.getString("responseCode")
                        if (rescode == "200") {
                            val responsData = resultobj.getJSONArray("responseData")
                            var Status: String
                            Status = ""
                            if (responsData.length()!=0) {
                                for (i in 0 until responsData.length()) {
                                    var AId=responsData.getJSONObject(i).getInt("AId")
                                    val OrderNo = responsData.getJSONObject(i).getString("ONo")
                                    val Customer = responsData.getJSONObject(i).getString("CName")
                                    val salesPerson =
                                        responsData.getJSONObject(i).getString("SName")
                                    val ShippingType = responsData.getJSONObject(i).getString("ST")
                                    val Products = responsData.getJSONObject(i).getString("ONo")
                                    Status = responsData.getJSONObject(i).getString("Status")
                                    val ColorCode =
                                        responsData.getJSONObject(i).getString("ColorCode")
                                    val OrderDate = responsData.getJSONObject(i).getString("ODate")
                                    val StatusAutoId = responsData.getJSONObject(i).getInt("StatusAutoId")
                                    StatusAutoId.toInt().toString()
                                    val TotalNoOfItem = responsData.getJSONObject(i).getInt("TotalNoOfItem")
                                    val ResumeO = responsData.getJSONObject(i).getInt("ResumeO")
                                    val PrintUR="" //= responsData.getJSONObject(i).getString("PrintUR")
                                    // var StatusColor:RecyclerView=view.findViewById(R.id.StatusView)

                                    Add_On_OrderListData(
                                        AId,
                                        OrderNo,
                                        Customer,
                                        salesPerson,
                                        ShippingType,
                                        Products,
                                        Status,
                                        OrderDate,
                                        ColorCode,
                                        StatusAutoId,
                                        TotalNoOfItem,
                                        PrintUR,
                                        ResumeO
                                    )
                                    orderNotAvailable!!.visibility=View.GONE
                                    pDialog.dismiss()
                                    //Toast.makeText(context,"successful",Toast.LENGTH_LONG).show()
                                }
                                pDialog.dismiss()
                            }
                            else{
                                //Toast.makeText(this.context,"Something went wrong",Toast.LENGTH_LONG).show()
                                val alerts = AlertDialog.Builder(this.context)
                                alerts.setMessage(resmsg.toString())
                                alerts.setCancelable(false)
                                alerts.setPositiveButton("OK", null)
                                val dialog: AlertDialog = alerts.create()
                                dialog.show()
                                if(pDialog!=null){
                                    if(pDialog.isShowing){
                                        pDialog.dismiss()
                                    }
                                }
                            }
                            pDialog.dismiss()
                        } else {
                            orderNotAvailable!!.text = "No order available"
                            orderNotAvailable!!.visibility=View.VISIBLE
                            val alerts = AlertDialog.Builder(this.context)
                            alerts.setMessage("No order available")
                            alerts.setCancelable(false)
                            alerts.setPositiveButton("ok", null)
                            val dialog: AlertDialog = alerts.create()
                            dialog.show()
                            if(pDialog!=null){
                                if(pDialog.isShowing){
                                    pDialog.dismiss()
                                }
                            }
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
                queues.add(JsonObjectRequest)
            } catch (e: Exception) {
                Toast.makeText(this.context, "Server Error", Toast.LENGTH_LONG).show()
            }

        }
        else {
            val dialog = context?.let { Dialog(it) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
            val btDismiss =
                dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
            btDismiss?.setOnClickListener {
                dialog.dismiss()
                this.findNavController().navigate(com.example.myapplication.R.id.nav_home)
            }
            dialog?.show()
        }
    }

override fun onResume() {
    super.onResume()
    if (shouldExecuteOnResume) {
        // Your onResume Code Here
        shouldExecuteOnResume = true
        addOneList()
    } else {
        shouldExecuteOnResume = true
       // addOneList()
    }
}
    private fun Add_On_OrderListData(
        AId:Int,
        OrderNo: String,
        Customer: String,
        salesPerson: String,
        ShippingType: String,
        Products: String,
        Status: String,
        OrderDate: String,
        ColorCode:String,
        StatusAutoId:Int,
        TotalNoOfItem:Int,
        PrintUR:String,
        ResumeO:Int
    ) {
        var order = ModelClassOrderLIst(
            AId,
            OrderNo,
            Customer,
            salesPerson,
            ShippingType,
            Products,
            Status,
            OrderDate,
            ColorCode,
            StatusAutoId,
            TotalNoOfItem,
            PrintUR,
            ResumeO
        )
        orderList.add(order)
        AddonlistAdapter.notifyDataSetChanged()
    }
}