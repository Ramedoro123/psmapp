package com.example.myapplication.com.example.whm.ui.UpdateLocation

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.whm.ui.UpdateLocation.UpdateLocation
import org.json.JSONObject


class UpdateLocationAdapter(
    private val UpdateList: List<UpdateLocationModel>,

    var UpdateLocations: Context?
) : RecyclerView.Adapter<UpdateLocationAdapter.ViewHolder>() {
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var OrderNo: TextView = itemView.findViewById(R.id.OrderValue)
        var DateValue: TextView = itemView.findViewById(R.id.DateValue)
        var CustomerName: TextView = itemView.findViewById(R.id.CustomerName)
        var No_BoxesValue: TextView = itemView.findViewById(R.id.No_BoxesValue)
        var LocationValue: TextView = itemView.findViewById(R.id.LocationValue)
        var LocationValuetext: TextView = itemView.findViewById(R.id.LocationValuetext)
        var UpdateCardList: CardView = itemView.findViewById(R.id.UpdateCardList)
        var Status:TextView= itemView.findViewById(R.id.Status)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpdateLocationAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.update_location, parent, false)
        return ViewHolder(view)
    }

    var ValueUpdate: String? = null
    var OrderNoValue: String? = null
    var Olvalue: String? = null
    override fun onBindViewHolder(holder: UpdateLocationAdapter.ViewHolder, position: Int) {
        var UpdateLocationList = UpdateList[position]
        holder.OrderNo.text = UpdateLocationList.getOrderNo().toString()
        holder.DateValue.text = UpdateLocationList.getODate().toString()
        holder.CustomerName.text = UpdateLocationList.getCName().toString()
        holder.No_BoxesValue.text = UpdateLocationList.getPackedBoxes().toString()
        holder.LocationValue.text = UpdateLocationList.getOL().toString()
        holder.Status.text = UpdateLocationList.getStatus().toString()
        holder.Status.setTextColor(Color.parseColor(UpdateLocationList.getColorCode()))

        holder.LocationValue.visibility = View.GONE
        holder.LocationValuetext.visibility = View.GONE
        Olvalue = UpdateLocationList.getOL().toString()
//        val Dilogview = View.inflate(UpdateLocation, R.layout.popupbox_updatelocation, null)
//        val builder = AlertDialog.Builder(UpdateLocation).setView(Dilogview)
        holder.UpdateCardList.setOnClickListener(View.OnClickListener {
            val Dilogviews = View.inflate(UpdateLocations, R.layout.popupbox_updatelocation, null)
            val builder = AlertDialog.Builder(UpdateLocations).setView(Dilogviews)
            builder.setCancelable(false);
            builder.setCanceledOnTouchOutside(false);
            val mSilog = builder.show()
            var textView16 = Dilogviews.findViewById<TextView>(R.id.textView16)
            var textView27 = Dilogviews.findViewById<TextView>(R.id.textView27)
            var ChangePotext = Dilogviews.findViewById<TextView>(R.id.ChangePotext)
            var view3 = Dilogviews.findViewById<View>(R.id.view3)
            var Bill_Date = Dilogviews.findViewById<TextView>(R.id.Bill_Date)
            var OrderNumber = Dilogviews.findViewById<TextView>(R.id.OrderNoValue)
            var BoxValue = Dilogviews.findViewById<TextView>(R.id.BoxValue)
            OrderNoValue = UpdateLocationList.getOrderNo().toString().trim()
            var btnSave = Dilogviews.findViewById<Button>(R.id.btnSave)
            var btnClose = Dilogviews.findViewById<TextView>(R.id.btnClose)

            textView16.visibility=View.VISIBLE
            textView27.visibility=View.VISIBLE
            view3.visibility=View.VISIBLE
            Bill_Date.visibility=View.GONE
            ChangePotext.visibility=View.GONE
            OrderNumber.visibility=View.VISIBLE
            BoxValue.visibility=View.VISIBLE
            OrderNumber.setText(holder.OrderNo.text)

            BoxValue.setText(holder.No_BoxesValue.text)
            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(UpdateLocations)
            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putString("OrderNoValue", OrderNoValue.toString())
            //sharedLoadOrderPage.putString("UpdateLocation",ValueUpdate.toString())
            sharedLoadOrderPage.apply()

            btnSave.setOnClickListener(View.OnClickListener {
                mSilog.dismiss()
                var textvalue = Dilogviews.findViewById<EditText>(R.id.textValue)
                ValueUpdate = textvalue.text.toString()
                if (ValueUpdate!!.isEmpty()) {
                    mSilog.show()
                    SweetAlertDialog(
                        UpdateLocations,
                        SweetAlertDialog.ERROR_TYPE
                    ).setContentText("Please Enter Location").show()

                } else {
                    val Dilogview = View.inflate(UpdateLocations, R.layout.popupbox_picker, null)
                    val builder = AlertDialog.Builder(UpdateLocations).setView(Dilogview)
                    builder.setCancelable(false);
                    builder.setCanceledOnTouchOutside(false);
                    val mSilogs = builder.show()
                    var CustomerName = Dilogview.findViewById<TextView>(R.id.CustomerName)
                    var OrderNo = Dilogview.findViewById<TextView>(R.id.OrderNo)
                    var StartAndResumPacking =
                        Dilogview.findViewById<TextView>(R.id.StartAndResumPacking)
                    var btnYes = Dilogview.findViewById<Button>(R.id.btnYesPack)
                    var btnNoCancle = Dilogview.findViewById<Button>(R.id.btnNoCancle)
                    btnNoCancle.setText("No")
                    btnYes.setText("Yes")
                    btnYes.visibility = View.VISIBLE
                    CustomerName.visibility = View.GONE
                    OrderNo.visibility = View.GONE
                    StartAndResumPacking.setText("Are you sure, you want to update location?")

                    btnYes.setOnClickListener(View.OnClickListener {
                        updateOrderLocation()
                        var intent: Intent = Intent(UpdateLocations, UpdateLocation::class.java)
                        UpdateLocations?.startActivity(intent)
                        (UpdateLocations as Activity).finish()
                        mSilogs.dismiss()
                    })
                    btnNoCancle.setOnClickListener(View.OnClickListener { mSilogs.dismiss() })
                }

            })
            btnClose.setOnClickListener(View.OnClickListener { mSilog.dismiss() })


            //Toast.makeText(UpdateLocation,UpdateLocationList.getAId().toString(),Toast.LENGTH_LONG).show()

        })

        if (Olvalue == "" || holder.LocationValue.text == "") {
            holder.LocationValue.visibility = View.GONE
            holder.LocationValuetext.visibility = View.GONE
        } else {
            holder.LocationValue.visibility = View.VISIBLE
            holder.LocationValuetext.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {

        return UpdateList.size
    }

    public fun updateOrderLocation() {
        val pDialog = SweetAlertDialog(UpdateLocations, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(false)
        pDialog.show()
        val preferences = PreferenceManager.getDefaultSharedPreferences(UpdateLocations)
        var accessToken = preferences.getString("accessToken", "")
        var empautoid = preferences.getString("EmpAutoId", "")
        var OrderNoValue = preferences.getString("OrderNoValue", "")
        //var UpdateLocationValue = preferences.getString("UpdateLocation", "")
        val requestContainer = JSONObject()
        val JSONObj = JSONObject()
        val pobj = JSONObject()
        JSONObj.put(
            "requestContainer",
        requestContainer.put("deviceID", Settings.Secure.getString(UpdateLocations?.contentResolver, Settings.Secure.ANDROID_ID.toString())))
        JSONObj.put(
            "requestContainer",
            requestContainer.put("appVersion", AppPreferences.AppVersion)
        )
        JSONObj.put(
            "requestContainer",
            requestContainer.put("deviceVersion", AppPreferences.versionRelease)
        )
        JSONObj.put(
            "requestContainer",
            requestContainer.put("deviceName", AppPreferences.DeviceName)
        )
        JSONObj.put("requestContainer", requestContainer.put("userAutoId", empautoid))
        JSONObj.put("requestContainer", requestContainer.put("accessToken", accessToken))
        JSONObj.put("pobj", pobj.put("orderno", OrderNoValue))
        JSONObj.put("pobj", pobj.put("OrderLocation", ValueUpdate))
        //Toast.makeText(this,accessToken.toString(),Toast.LENGTH_LONG).show()
//        Log.d("TAG", accessToken.toString())
//        Log.d("TAG", AppPreferences.AppVersion.toString())
//        Log.d("TAG", AppPreferences.versionRelease.toString())
//        Log.d("TAG", AppPreferences.DeviceName.toString())
//        Log.d("TAG", ScanOrderBarcode1.toString())
        Log.d("deviceVersion",  AppPreferences.versionRelease.toString())
        Log.d("appVersion",AppPreferences.AppVersion.toString())
        Log.d("deviceName", AppPreferences.DeviceName.toString())
        // Log.d("TAG", Settings.Secure.getString(UpdateLocation?.contentResolver, Settings.Secure.ANDROID_ID).toString())
        //Toast.makeText(this,empautoid.toString(),Toast.LENGTH_LONG).show()
        val queue = Volley.newRequestQueue(UpdateLocations)
// Request a string response from the provided URL.
        val JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, AppPreferences.updateOrderLocation, JSONObj,
            { response ->
                val Response = (response.toString())
                val responsemsg = JSONObject(Response.toString())
                val ResponsResult = JSONObject(responsemsg.getString("d"))
                val resmsg = ResponsResult.getString("responseMessage")
                val ResponseCode = ResponsResult.getString("responseCode")
                if (ResponseCode == "200") {
                    //val ResponseData=ResponsResult.getJSONArray("responseData")
                    //val negative=SweetAlertDialog
                    pDialog.dismiss()
//                    false
                } else {
                    SweetAlertDialog(UpdateLocations, SweetAlertDialog.ERROR_TYPE).setContentText(
                        resmsg
                    ).show()
                    if (pDialog != null) {
                        if (pDialog.isShowing) {
                            pDialog.dismiss()
                        }
                    }

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
            Toast.makeText(UpdateLocations, "Server Error", Toast.LENGTH_LONG).show()
        }
    }

}

fun AlertDialog.Builder.setCanceledOnTouchOutside(b: Boolean) {

}
