package com.example.whm.ui.OrderDetailsList
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.*
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.OrderDetailsList.ModelOrderDetails
import com.example.myapplication.com.example.whm.ui.OrderDetailsList.OrderDetailsAdapter
import org.json.JSONObject


class OrderDetailsActicity : AppCompatActivity() {
    var OrderAutoId: String? = null
    var StatusAutoId: String? = null
    lateinit var print:TextView
    //val data = ArrayList<ModelOrderDetails>()
    var data: ArrayList<ModelOrderDetails> = arrayListOf()
     lateinit var DetailsAdapter: OrderDetailsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details_acticity)
        var OrderNoDetails_text: TextView = findViewById(R.id.OrderNoDetails_text)
        var CustomerNameDetails_text: TextView = findViewById(R.id.CustomerNameDetails_text)
        var Date_Details_text: TextView = findViewById(R.id.Date_Details_text)
        var Status_Details_text: TextView = findViewById(R.id.Status_Details_text)
        var Show_OrderTotalItem_PickerText: TextView =findViewById(R.id.Show_OrderTotalItem_PickerText)
        var btnBack: TextView = findViewById(R.id.btnBackDetailsPage)
        var packbox: TextView = findViewById(R.id.packBoxno)
         print= findViewById(R.id.PrintTextView)
        print.visibility=View.GONE
        var Orderdedtails:ConstraintLayout=findViewById(R.id.OrderDetails)
       // var Product_ID:TextView=findViewById(R.id.Product_ID)
        btnBack.setOnClickListener(View.OnClickListener {
//           var intent:Intent= Intent(this,MainActivity2::class.java);
//            startActivity(intent);
            finish()
        })

        val preferencesid=PreferenceManager.getDefaultSharedPreferences(this@OrderDetailsActicity)
        OrderAutoId = (preferencesid.getString("OrderAutoid", OrderAutoId.toString()))
        StatusAutoId = (preferencesid.getString("StatusAutoId", StatusAutoId.toString()))
         // getting the recyclerview by its id
        // This loop will create 20 Views containing
        // the image with the count of view
        if (AppPreferences.internetConnectionCheck(this.applicationContext)) {
            val recyclerview = findViewById<RecyclerView>(R.id.OrderDetailsListView)
            val layoutManager = LinearLayoutManager(this)
            recyclerview.layoutManager = layoutManager

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
            val queues = Volley.newRequestQueue(this)
            JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
            JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
            JSONObj.put("pobj", pobj.put("OrderAutoId", OrderAutoId))
//          Request a string response from the provided URL.
            val JsonObjectRequest =
                JsonObjectRequest(
                    Request.Method.POST, AppPreferences.API_PACKER_ORDER_DETAILS,
                    JSONObj, { response ->
                        // Display the first 500 characters of the response string.
                        val resobj = (response.toString())
                        val responsemsg = JSONObject(resobj.toString())
                        val resultobj = JSONObject(responsemsg.getString("d"))
                        val rescode = resultobj.getString("responseCode")
                        if (rescode == "200") {
                            val responsData = resultobj.getJSONArray("responseData")
                            if (responsData.length() != null) {

                                for (i in 0 until responsData.length()) {
                                    var OrderNo = responsData.getJSONObject(i).getString("OrderNo")
                                    var OrderDate =responsData.getJSONObject(i).getString("OrderDate")
                                    var Customer_Name = responsData.getJSONObject(i).getString("CustomerName")
                                    var Status = responsData.getJSONObject(i).getString("Status")
                                    var TotalNoOfItem = responsData.getJSONObject(i).getInt("TotalNoOfItem")
                                    var StatusCode = responsData.getJSONObject(i).getInt("StatusCode")
                                    var PrintURL = responsData.getJSONObject(i).getString("PrintURL")
                                    var PackedBoxes = responsData.getJSONObject(i).getString("PackedBoxes")
                                    //Toast.makeText(this,StatusAutoId.toString(),Toast.LENGTH_LONG).show()
                                    if (StatusCode==3|| StatusCode==10) {
                                        print.visibility=View.VISIBLE
                                        print.setOnClickListener(View.OnClickListener {
                                            try {
                                                val openURL =
                                                    Intent(android.content.Intent.ACTION_VIEW)
                                                openURL.data = Uri.parse(PrintURL)
                                                this?.startActivity(openURL)
                                            } catch (e: ActivityNotFoundException) {
                                                Toast.makeText(
                                                    this,
                                                    "No application can handle this request."
                                                            + " Please install a webbrowser",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                e.printStackTrace()
                                            }
                                        })
                                    }
                                    var Items=responsData.getJSONObject(i).getJSONArray("Items")
                                    var TotalItem: String = TotalNoOfItem.toString()
                                    OrderNoDetails_text.setText(OrderNo)
                                    Date_Details_text.setText(OrderDate)
                                    CustomerNameDetails_text.setText(Customer_Name)
                                    Status_Details_text.setText(Status)
                                    Show_OrderTotalItem_PickerText.setText(TotalItem)
                                    packbox.setText(PackedBoxes)
                                    for (i in 0 until Items.length()){
                                        var PId = Items.getJSONObject(i).getInt("PId")
                                        var PName=Items.getJSONObject(i).getString("PName")
                                        var UnitType=Items.getJSONObject(i).getString("UnitType")
                                        var QtyPerUnit=Items.getJSONObject(i).getInt("QtyPerUnit")
                                        var RequiredQty=Items.getJSONObject(i).getInt("RequiredQty")
                                        var QtyShip=Items.getJSONObject(i).getString("QtyShip")
                                        var ImageUrl=Items.getJSONObject(i).getString("ImageUrl")
                                        var IsExchange=Items.getJSONObject(i).getInt("IsExchange")
                                        var isFreeItem=Items.getJSONObject(i).getInt("isFreeItem")

                                        //Toast.makeText(this,PId.toString(),Toast.LENGTH_LONG).show()
                                        var check=false
                                        if (!check) {
                                            if (UnitType!=null) {
                                                if (PName!=null) {
                                                    OrderDetailsData(
                                                        PId,
                                                        PName,
                                                        UnitType,
                                                        QtyPerUnit,
                                                        RequiredQty,
                                                        QtyShip,
                                                        StatusCode,
                                                        IsExchange,
                                                        isFreeItem,
                                                        ImageUrl
                                                    )
                                                    Orderdedtails.visibility = View.VISIBLE
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()

                            }

                            pDialog.dismiss()
                        } else {

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
                queues.add(JsonObjectRequest)
            } catch (e: Exception) {
                Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
            }
        } else {
            val dialog = this?.let { Dialog(it) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
            val btDismiss = dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
            btDismiss?.setOnClickListener {
                dialog.dismiss()
               finish()
            }
            dialog?.show()
        }
    }
    private fun OrderDetailsData(PId: Int, PName: String, UnitType: String, QtyPerUnit: Int,RequiredQty:Int,QtyShip:String,StatusCode:Int,IsExchange:Int,isFreeItem:Int,ImageUrl:String)
    {
        var DetailsListOrder=ModelOrderDetails(PId,PName,UnitType,QtyPerUnit,RequiredQty,QtyShip,StatusCode,IsExchange,isFreeItem,ImageUrl)
        data.add(DetailsListOrder)
        val recyclerview = findViewById<RecyclerView>(R.id.OrderDetailsListView)
        DetailsAdapter = OrderDetailsAdapter(data, this)
        recyclerview.adapter = DetailsAdapter
       // DetailsAdapter.notifyDataSetChanged()

    }
}



