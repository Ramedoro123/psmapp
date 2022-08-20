package com.example.whm.ui.UpdateLocation

import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
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
import com.example.myapplication.com.example.whm.ui.UpdateLocation.UpdateLocationAdapter
import com.example.myapplication.com.example.whm.ui.UpdateLocation.UpdateLocationModel
import com.example.myapplication.com.example.whm.ui.UpdateLocation.setCanceledOnTouchOutside
import org.json.JSONObject

class UpdateLocation : AppCompatActivity() {
    var shouldExecuteOnResume = false
    lateinit var ScanOrderBarcode:EditText
    lateinit var btnBackArrow:TextView
    var ScanOrderBarcode1:String?=null
    var UpdateLocationData: ArrayList<UpdateLocationModel> = arrayListOf()
    lateinit var adapterUpdateLocation: UpdateLocationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_location)
        findbyid()
//        val actionBar: ActionBar? = actionBar
//        actionBar.setDisplayHomeAsUpEnabled(true)
        ScanOrderBarcode.requestFocus()
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btnBackArrow.setOnClickListener(View.OnClickListener {
        finish()
        })
        if (AppPreferences.internetConnectionCheck(this)) {
            ScanOrderBarcode!!.requestFocus()
            ScanOrderBarcode!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                    var scanbarcodeproduct= ScanOrderBarcode!!.text.toString()
                    if (scanbarcodeproduct.trim().isEmpty()) {
                        ScanOrderBarcode.text.clear()
                        ScanOrderBarcode.setText("")

                        ScanOrderBarcode.requestFocus()
                    } else {
                        // Scan barcode function calling here
                        UpdateLocationData.clear()
                         UpdateLocationFunctoin()
                        ScanOrderBarcode.requestFocus()
                        ScanOrderBarcode.text.clear()
                        // btnPrevious.isEnabled=true
                    }
                }

                false

            })
        }
        else {
            //CheckInterNetDailog()
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



        if (AppPreferences.internetConnectionCheck(this)){
            shouldExecuteOnResume = false
            val recyclerview = findViewById<RecyclerView>(R.id.Update_Location_recyclerView)
            val layoutManager = LinearLayoutManager(this)
            recyclerview.layoutManager = layoutManager
            UpdateLocationFunctoin()
            UpdateLocationData.clear()
        }
        else{
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
    }
    public fun UpdateLocationFunctoin() {
        ScanOrderBarcode1=ScanOrderBarcode.text.toString().trim()
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(false)
        pDialog.show()
        val preferences= PreferenceManager.getDefaultSharedPreferences(this@UpdateLocation)
        var accessToken = preferences.getString("accessToken", "")
        var empautoid = preferences.getString("EmpAutoId", "")

        val requestContainer = JSONObject()
        val JSONObj = JSONObject()
        val pobj = JSONObject()
        JSONObj.put("requestContainer", requestContainer.put("deviceID", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)))
        JSONObj.put("requestContainer", requestContainer.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer", requestContainer.put("deviceVersion", AppPreferences.versionRelease))
        JSONObj.put("requestContainer", requestContainer.put("deviceName", AppPreferences.DeviceName))
        JSONObj.put("requestContainer", requestContainer.put("userAutoId", empautoid))
        JSONObj.put("requestContainer", requestContainer.put("accessToken", accessToken))
        if (ScanOrderBarcode1!="") {
            JSONObj.put("pobj", pobj.put("orderno", ScanOrderBarcode1))
        }else{
            JSONObj.put("pobj", pobj.put("orderno", ""))
        }
        //Toast.makeText(this,accessToken.toString(),Toast.LENGTH_LONG).show()
        Log.d("TAG", accessToken.toString())
        Log.d("TAG", AppPreferences.AppVersion.toString())
        Log.d("TAG", AppPreferences.versionRelease.toString())
        Log.d("TAG", AppPreferences.DeviceName.toString())
        Log.d("TAG", ScanOrderBarcode1.toString())
        Log.d("TAG",Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID).toString())
        //Toast.makeText(this,empautoid.toString(),Toast.LENGTH_LONG).show()
        val queue = Volley.newRequestQueue(this)
// Request a string response from the provided URL.
        val JsonObjectRequest = JsonObjectRequest(Request.Method.POST, AppPreferences.UpdateLocation_Url, JSONObj,
            { response ->
                val Response = (response.toString())
                val responsemsg = JSONObject(Response.toString())
                val ResponsResult = JSONObject(responsemsg.getString("d"))
                val resmsg = ResponsResult.getString("responseMessage")
                val ResponseCode = ResponsResult.getString("responseCode")
               UpdateLocationData.clear()
                ScanOrderBarcode.requestFocus()
                if (ResponseCode=="200"){
                    val ResponseData=ResponsResult.getJSONArray("responseData")
                    for (i in 0 until ResponseData.length()){
                        var AId = ResponseData.getJSONObject(i).getInt("AId")
                        val OrderNo = ResponseData.getJSONObject(i).getString("ONo")
                        val CName = ResponseData.getJSONObject(i).getString("CName")
                        val ODate = ResponseData.getJSONObject(i).getString("ODate")
                        val PackedBoxes = ResponseData.getJSONObject(i).getInt("PackedBoxes")
                        val ColorCode = ResponseData.getJSONObject(i).getString("ColorCode")
                        val OL = ResponseData.getJSONObject(i).getString("OL")
                        val Status = ResponseData.getJSONObject(i).getString("Status")
                        addUpdateLocationModel(
                            AId,
                            OrderNo,
                            CName,
                            ODate,
                            PackedBoxes,
                            ColorCode,
                            OL,
                            Status
                        )
                        pDialog.dismiss()
                    }
                }else{
                    val Dilogview2 = View.inflate(this@UpdateLocation, R.layout.stock_list_popup, null)
                    val builder2 = AlertDialog.Builder(this@UpdateLocation).setView(Dilogview2)
                    var mSilog2=builder2.show()
                    builder2.setCancelable(false);
                    builder2.setCanceledOnTouchOutside(false);
                    mSilog2.show()
                    var Message2=Dilogview2.findViewById<TextView>(R.id.Message)
                    var btnOk = Dilogview2.findViewById<TextView>(R.id.btnOk1)
                    Message2.setText(resmsg.toString())
                    // pDialog!!.dismiss()
                        btnOk!!.setOnClickListener(View.OnClickListener {
                        UpdateLocationFunctoin()
                          mSilog2.dismiss()
                        pDialog.dismiss()
                       // finish()
                    })

                }
        },
            Response.ErrorListener { pDialog.dismiss() })

        JsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            10000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        try {
            queue.add(JsonObjectRequest)
        } catch (e: Exception) {
            Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
        }

    }

    private fun addUpdateLocationModel(AId: Int, OrderNo: String, cName: String, ODate: String, PackedBoxes: Int,ColorCode:String, ol: String,Status:String)    {
        var UpdateLocationModel= UpdateLocationModel(AId, OrderNo,ODate,cName,PackedBoxes,ColorCode,ol,Status)
        UpdateLocationData.add(UpdateLocationModel)
        val recyclerview = findViewById<RecyclerView>(R.id.Update_Location_recyclerView)
        adapterUpdateLocation= UpdateLocationAdapter(UpdateLocationData, this)
        recyclerview.adapter = adapterUpdateLocation
        // DetailsAdapter.notifyDataSetChanged()
    }
    private  fun findbyid(){
        ScanOrderBarcode=findViewById(R.id.Scan_OrderBarcode)
        btnBackArrow=findViewById(R.id.btnBackarrow)
    }
}