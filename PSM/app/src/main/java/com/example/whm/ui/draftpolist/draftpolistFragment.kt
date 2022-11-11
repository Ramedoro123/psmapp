package com.example.whm.ui.draftpolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
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
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassDraftOrderList
import com.example.myapplication.com.example.whm.ui.Sales_Person.SwipeGesture
import com.example.myapplication.com.example.whm.ui.draftpolist.draftpoadapter
import com.example.myapplication.com.example.whm.ui.draftpolist.draftpomodel
import com.example.whm.ui.Sales_Person.SalesPersonProductList
import com.example.whm.ui.inventoryreceive.ReceivePO
import org.json.JSONObject
import java.io.IOException


class draftpolistFragment : Fragment(), draftpoadapter.OnItemClickListeners {
      var draftModel:ArrayList<draftpomodel> = arrayListOf()
    private lateinit var draftAdapter: draftpoadapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_draftpol_list2, container, false)

        if (AppPreferences.internetConnectionCheck(this.context)) {
            val recyclerView: RecyclerView = view.findViewById(R.id.load_order)
            val layoutManager = LinearLayoutManager(this.context)
            recyclerView.layoutManager = layoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()

            val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog.titleText = "Fetching ..."
            pDialog.setCancelable(false)
            pDialog.show()
            val Jsonarrapolist = JSONObject()
            val Jsonarra = JSONObject()
            val JSONObj = JSONObject()
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            var empautoid = preferences.getString("EmpAutoId", "")
            var accessToken = preferences.getString("accessToken", "")
            var StatusD = 1
        //    val sharedLoadOrderPage = preferences.edit()
            //  Toast.makeText(this.context,StatusD.toString(),Toast.LENGTH_LONG).show()
            val queues = Volley.newRequestQueue(this.context)
            JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
            JSONObj.put("requestContainer", Jsonarra.put("userAutoId", empautoid))
            JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
            JSONObj.put("requestContainer",Jsonarra.put("deviceID", Settings.Secure.getString(
                context?.contentResolver, Settings.Secure.ANDROID_ID)))
            JSONObj.put("cObj", Jsonarrapolist.put("status", StatusD))
            Log.e("userAutoId",empautoid.toString())
            Log.e("accessToken",accessToken.toString())
            Log.e("StatusD",StatusD.toString())

            val draftpolist = JsonObjectRequest(
                Request.Method.POST, AppPreferences.DRAFT_PO_LIST,
                JSONObj,
                { response ->
                    val resobj = (response.toString())
                    val responsemsg = JSONObject(resobj.toString())
                    val resultobj = JSONObject(responsemsg.getString("d"))
                    val resmsg = resultobj.getString("responseMessage")
                    val rescode = resultobj.getString("responseCode")
                    if (rescode == "201") {
                        draftModel.clear()
//                        draftAdapter.notifyDataSetChanged()
                        val jsondata = resultobj.getJSONArray("responseData")
                        for (i in 0 until jsondata.length()) {
                            val BillNo = jsondata.getJSONObject(i).getString("BillNo")
                            val Billdate = jsondata.getJSONObject(i).getString("BillDate")
                            val VendorName = jsondata.getJSONObject(i).getString("VAutoId")
                            val DAutoId = jsondata.getJSONObject(i).getInt("DAutoId")
                            val Status = jsondata.getJSONObject(i).getInt("Status")
                            val NoofProduct = jsondata.getJSONObject(i).getInt("NoOfI")
                            DataBindLoadorder(
                                BillNo, Billdate, VendorName,Status,NoofProduct,DAutoId)

                        }

                        if (pDialog != null) {
                            if (pDialog.isShowing) {
                                pDialog.dismiss()
                            }
                        }
                    } else {
                        val alerts = AlertDialog.Builder(this.context)
                        alerts.setMessage(resmsg.toString())
                        alerts.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alerts.create()
                        dialog.show()
                        if (pDialog != null) {
                            if (pDialog.isShowing) {
                                pDialog.dismiss()
                            }
                        }
                    }
                },
                { response ->
                    Log.e("onError", error(response.toString()))
                    if (pDialog != null) {
                        if (pDialog.isShowing) {
                            pDialog.dismiss()
                        }
                    }
                })
            draftpolist.retryPolicy = DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            try {
                queues.add(draftpolist)
            } catch (e: IOException) {
                Toast.makeText(this.context, "Server Error", Toast.LENGTH_LONG).show()
            }
        } else {
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
        return view
    }

    private fun DataBindLoadorder(
        BillNo: String,
        Billdate: String,
        VendorName: String,
        Status: Int,
        NoofProduct: Int,
        DAutoId: Int
    ) {
        var RevertPoList = draftpomodel(BillNo, Billdate, VendorName, Status, NoofProduct, DAutoId)
        draftModel.add(RevertPoList)
        val recyclerView: RecyclerView = view!!.findViewById(R.id.load_order)
        draftAdapter = draftpoadapter(draftModel, this.context,this@draftpolistFragment)
        val SwipeGesture=object :SwipeGesture(this.context!!){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val ClickedItem: draftpomodel=draftModel[viewHolder.bindingAdapterPosition]
                when(direction){
                    ItemTouchHelper.LEFT ->{
                        var alertbox = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                        alertbox.titleText = "Are you sure?"
                        alertbox.setContentText("You want to delete.").contentTextSize = 20
//                        alertbox.setContentText("You want to delete"+"\n"+ClickedItem.getBillNo().toString()+"-"+ClickedItem.getnoofproducts().toString()+".")
                        alertbox.cancelButtonBackgroundColor = Color.parseColor("#4cae4c")
                        alertbox.setCancelButton("Yes")
                        { sDialog ->
                            draftAdapter.notifyDataSetChanged()
//                            finish()
                            sDialog.dismissWithAnimation()
                        }
                        alertbox.confirmText = "No"
                        alertbox.confirmButtonBackgroundColor = Color.parseColor("#E60606")
                        alertbox.setCancelClickListener { sDialog ->

                            draftAdapter.deleteItem(viewHolder.adapterPosition)
//                        Toast.makeText(this@DraftOrderList,customerOrderAdapter.toString(),Toast.LENGTH_LONG).show()
                            deleteDraftOrder(draftAutoid=ClickedItem.getDAutoid().toString())

                            sDialog.dismissWithAnimation()
                        }
                        alertbox.setConfirmClickListener { sDialog ->
                            draftAdapter.notifyDataSetChanged()
                            sDialog.dismissWithAnimation()
                        }
                        alertbox.setCanceledOnTouchOutside(false)
                        alertbox.show()
                    }
                    ItemTouchHelper.RIGHT->{
                        val intent = Intent(activity, ReceivePO::class.java)
                        activity?.startActivity(intent)
                        val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
                        val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
                        sharedLoadOrderPage.putInt("DAutoid", ClickedItem.getDAutoid()!!.toInt())
                        sharedLoadOrderPage.putInt("Status", ClickedItem.getStatus()!!.toInt())
                        sharedLoadOrderPage.putString("Bill_No", ClickedItem.getBillNo().toString())
                        sharedLoadOrderPage.putString("Bill_Date", ClickedItem.getBill_date().toString())
                        sharedLoadOrderPage.putString("VENDORName", ClickedItem.getnoofproducts().toString())
                        sharedLoadOrderPage.putInt("Status", ClickedItem.getStatus()!!.toInt())
                        sharedLoadOrderPage.apply()
                        draftAdapter.notifyDataSetChanged()
                        finish()
                    }
                }
            }
        }
        val tochHelper=ItemTouchHelper(SwipeGesture)
        tochHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = draftAdapter
        draftAdapter.notifyDataSetChanged()
    }
fun deleteDraftOrder(draftAutoid:String) {
    if (AppPreferences.internetConnectionCheck(this.context)) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        var accessToken = preferences.getString("accessToken", "")
        var empautoid = preferences.getString("EmpAutoId", "")
        var pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
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
                    this.context!!.contentResolver,
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
        if (draftAutoid!="0"&&draftAutoid!="") {
            sendRequestObject.put("cObj", pObj.put("draftAutoId", draftAutoid))
        }

        Log.e("sendRequestObject order ordersubmit", sendRequestObject.toString())

        //send request queue in vally
        val queue = Volley.newRequestQueue(this.context)
        val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
            AppPreferences.deletePOOrder, sendRequestObject,
            { response ->
                val responseResult = JSONObject(response.toString())
                val responsedData = JSONObject(responseResult.getString("d"))
                var responseMessage2 = responsedData.getString("responseMessage")
                val responseStatus = responsedData.getInt("responseCode")
                if (responseStatus == 201) {

                    var popUp = SweetAlertDialog(this.context, SweetAlertDialog.SUCCESS_TYPE)
                    popUp.setContentText(responseMessage2.toString())
                    popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                    popUp.setConfirmClickListener()
                    { sDialog ->
                        sDialog.dismissWithAnimation()
//                            startActivity(Intent(this,MainActivity2::class.java))
//                        SalesDraftOrder.setText(" Draft Order List"+"("+modelClassDraftOrder.size+")")
                        draftAdapter.notifyDataSetChanged()
                        finish()
                        popUp.dismiss()
                        pDialog.dismiss()
                    }
                    popUp.show()
                    popUp.setCanceledOnTouchOutside(false)
                    popUp.setCancelable(false)
                    pDialog.setCancelable(false)

                } else {
                    var popUp = SweetAlertDialog(this.context, SweetAlertDialog.WARNING_TYPE)
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
            Toast.makeText(this.context, e.toString(), Toast.LENGTH_LONG).show()
        }
    } else {
        val dialog = this.context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
        val btDismiss =
            dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
        btDismiss?.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this.context, MainActivity2::class.java)
            this?.startActivity(intent)
            finish()

        }
        dialog?.show()
    }
}

    private fun finish() {
//        finish()
    }

    override fun OnItemsClick(position: Int) {

    }

    override fun OnDeleteClicks(position: Int) {

    }
}