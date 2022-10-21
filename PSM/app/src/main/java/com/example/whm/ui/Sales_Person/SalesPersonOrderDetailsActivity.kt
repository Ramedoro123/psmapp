package com.example.whm.ui.Sales_Person

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.SalesPersonItemAdapterClass
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.OrderItemModelClass
import org.json.JSONArray
import org.json.JSONObject

class SalesPersonOrderDetailsActivity : AppCompatActivity() {
    lateinit var btnBackOrderDetails:TextView
    lateinit var orderNumber:TextView
    lateinit var orderdate:TextView
    lateinit var status:TextView
    lateinit var customerNameSp:TextView
    lateinit var billingAddressSp:TextView
    lateinit var subTotalOrderdetails:TextView
    lateinit var weighttaxQty:TextView
    lateinit var shippingaddressSp:TextView
    lateinit var payableAmountValue:TextView
    lateinit var balanceamountValue:TextView
    lateinit var salesPersonRem:TextView
    lateinit var salesPreRemarkvalue:TextView
    lateinit var driverRemarkValue:TextView
    lateinit var driverRemarkText:TextView
    lateinit var payableText:TextView
    lateinit var AdjAmountvalue:TextView
    lateinit var wegitTaxValue:TextView
    lateinit var shippingTypeSp:TextView
    lateinit var deliveryDateSp:TextView
    lateinit var salesTaxType:TextView
    lateinit var paidAmountValue:TextView
    lateinit var paidAmountText:TextView
    lateinit var customeridCS:TextView
    lateinit var orderTotalvalue:TextView
    lateinit var orderTotalText:TextView
    lateinit var mlTaxQty:TextView
    lateinit var adjText:TextView
    lateinit var taxML:TextView
    var orderAutoId:String?=null
    var responsdata=JSONArray()
    var modelClassOrderItems: ArrayList<OrderItemModelClass> = arrayListOf()
    lateinit var customerOrderItemAdapter: SalesPersonItemAdapterClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_person_order_details)
        btnBackOrderDetails=findViewById(R.id.btnBackOrderDetails)
        orderdate=findViewById(R.id.orderdate)
        orderNumber=findViewById(R.id.orderNumber)
        customeridCS=findViewById(R.id.customeridCS)
        customerNameSp=findViewById(R.id.customerNameSp)
        billingAddressSp=findViewById(R.id.billingAddressSp)
        shippingaddressSp=findViewById(R.id.shippingaddressSp)
        subTotalOrderdetails=findViewById(R.id.subTotalOrderdetails)
        payableAmountValue=findViewById(R.id.payableAmountValue)
        balanceamountValue=findViewById(R.id.balanceamountValue)
        payableText=findViewById(R.id.payableText)
        paidAmountValue=findViewById(R.id.paidAmountValue)
        salesPersonRem=findViewById(R.id.salesPersonRem)
        salesPreRemarkvalue=findViewById(R.id.salesPreRemarkvalue)
        driverRemarkText=findViewById(R.id.driverRemarkText)
        driverRemarkValue=findViewById(R.id.driverRemarkValue)
        paidAmountText=findViewById(R.id.paidAmountText)
        shippingTypeSp=findViewById(R.id.shippingTypeSp)
        AdjAmountvalue=findViewById(R.id.AdjAmountvalue)
        salesTaxType=findViewById(R.id.salesTaxType)
        deliveryDateSp=findViewById(R.id.deliveryDateSp)
        weighttaxQty=findViewById(R.id.weighttaxQty)
        wegitTaxValue=findViewById(R.id.wegitTaxValue)
        orderTotalvalue=findViewById(R.id.orderTotalvalue)
        orderTotalText=findViewById(R.id.orderTotalText)
        mlTaxQty=findViewById(R.id.mlTaxQty)
        status=findViewById(R.id.status)
        adjText=findViewById(R.id.adjText)
        taxML=findViewById(R.id.taxML)

        btnBackOrderDetails.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        orderAutoId = intent.getStringExtra("orderAutoId")
        Log.e("orderAutoId",orderAutoId.toString())
        if (AppPreferences.internetConnectionCheck(this)) {
            val recyclerview = findViewById<RecyclerView>(R.id.OrderCustomerDetailsRecyclerView)
            val layoutManager = LinearLayoutManager(this)
            recyclerview.layoutManager = layoutManager
            OrderItemtListfunctionCall()
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

    private fun OrderItemtListfunctionCall() {
        if (AppPreferences.internetConnectionCheck(this)) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            var accessToken = preferences.getString("accessToken", "")
            var empautoid = preferences.getString("EmpAutoId", "")
            var customerAutoId = preferences.getString("customerAutoId", "")
            val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog.titleText = "Fetching ..."
            pDialog.setCancelable(false)
            pDialog.show()
            //We Create Json object to send request for server
            val requestContainer = JSONObject()
            val searchcustomer = JSONObject()
            val ContainerObject = JSONObject()
            ContainerObject.put("requestContainer",
                requestContainer.put("appVersion", AppPreferences.AppVersion))
            ContainerObject.put("requestContainer",
                requestContainer.put("deviceID",
                    Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)))
            ContainerObject.put("requestContainer",
                requestContainer.put("deviceVersion", AppPreferences.versionRelease))
            ContainerObject.put("requestContainer",
                requestContainer.put("deviceName", AppPreferences.DeviceName))
            ContainerObject.put("requestContainer",requestContainer.put("accessToken",accessToken))
            ContainerObject.put("requestContainer",requestContainer.put("userAutoId",empautoid))
            ContainerObject.put("pObj", searchcustomer.put("OrderAutoId",orderAutoId.toString()))
//        ContainerObject.put("searchcustomer", searchcustomer.put("customerName",customerName ))
//        ContainerObject.put("searchcustomer", searchcustomer.put("customerType",autoId ))
            Log.e("ContainerObject Order list request",ContainerObject.toString())
            // Send request Queue in vally
            val queue = Volley.newRequestQueue(this)

            // Request a string response from the provided URL.
            val JsonObjectRequest =
                JsonObjectRequest(Request.Method.POST,
                    AppPreferences.OrderDetailsAPI,
                    ContainerObject,
                    { response ->
                        val Response = (response.toString())
                        val responseResultData = JSONObject(Response.toString())
                        val ResponseResult = JSONObject(responseResultData.getString("d"))
                        val ResponseMessage = ResponseResult.getString("responseMessage")
                        val responseCode = ResponseResult.getString("responseStatus")
                        if (responseCode == "200") {

                            var responsDataObject = JSONObject(ResponseResult.getString("responseData"))
                            var OrderNo=responsDataObject.getString("OrderNo")
                            var OrderDate=responsDataObject.getString("OrderDate")
                            var CustomerId=responsDataObject.getString("CustomerId")
                            var DeliveryDate=responsDataObject.getString("DeliveryDate")

//                            var TaxType=responsDataObject.getString("TaxType")
                            var BillAddr=responsDataObject.getString("BillAddr")
                            var ShipAddr=responsDataObject.getString("ShipAddr")
                            var CustomerName=responsDataObject.getString("CustomerName")
                            var Status=responsDataObject.getString("Status")
                            var ShippingTypeName=responsDataObject.getString("ShippingTypeName")
                            var AdjustmentAmt=responsDataObject.getString("AdjustmentAmt")
                            var SubTotal=responsDataObject.getString("SubTotal")
                            var Weigth_OZQty=responsDataObject.getString("Weigth_OZQty")
                            var Weigth_OZTax=responsDataObject.getString("Weigth_OZTax")
                            var Weigth_OZTaxAmount=responsDataObject.getString("Weigth_OZTaxAmount")
                            var MLQty=responsDataObject.getString("MLQty")
                            var MLTax=responsDataObject.getString("MLTax")
                            var MLTaxPer=responsDataObject.getString("MLTaxPer")
                            var TaxLabel=responsDataObject.getString("TaxLabel")
                            var PaidMaount=responsDataObject.getString("PaidMaount")
                            var DueAmount=responsDataObject.getString("DueAmount")
                            var PayableAmount=responsDataObject.getString("PayableAmount")

                            orderNumber.setText(OrderNo.toString())
                            orderdate.setText(OrderDate.toString())
                            customerNameSp.setText(CustomerName.toString())
                            shippingaddressSp.setText(ShipAddr.toString())
                            billingAddressSp.setText(BillAddr.toString())
                            shippingTypeSp.setText(ShippingTypeName)
                            status.setText(Status.toString())
                            customeridCS.setText(CustomerId.toString())
                            deliveryDateSp.setText(DeliveryDate.toString())
                            if (SubTotal!=null) {
                                var subTotal=SubTotal.toString().toFloat()
                                subTotalOrderdetails.setText("$" + "%.2f".format(subTotal.toDouble()))
                            }
                            else if (PaidMaount!=null) {
                                var paidAmount=PaidMaount.toString().toFloat()
                                paidAmountValue.setText("$" + "%.2f".format(paidAmount.toDouble()))
                            }
                            else if (AdjustmentAmt!=null) {
                                var adjustment=AdjustmentAmt.toString().toFloat()
                                AdjAmountvalue.setText("$" + "%.2f".format(adjustment.toDouble()))
                            }
                            else if (PayableAmount!=null){
                                var payable=PayableAmount.toString().toFloat()
                                payableAmountValue.setText("$" + "%.2f".format(payable.toDouble()))
                            }

                            if (Weigth_OZQty!=null&&Weigth_OZQty!=""&&Weigth_OZQty!="0"&&Weigth_OZTax!=null&&Weigth_OZTax!=""&&Weigth_OZTax!="0"
                                &&Weigth_OZTaxAmount!=""&&Weigth_OZTaxAmount!=null&&Weigth_OZTaxAmount!="0"){

                            }
                            else if (MLQty!=""&&MLQty!="0"&&MLQty!=null&& MLTax!=null&& MLTax!=""&& MLTax!="0" && MLTaxPer!="0"&& MLTaxPer!=""&& MLTaxPer!=null)
                            {

                            }else if (DueAmount!=null&&DueAmount!=""&&DueAmount!="0")
                            {

                            }
                            else
                            {

                            }
                            Log.e("responsDataObjectall",responsDataObject.toString())
                            val responseData = responsDataObject.getJSONArray("OrderItemList")
                            val responseData1 = responsDataObject.getJSONArray("DelItemList")
                            if (responseData!=null&&responseData.length()>0)
                            {
                                responsdata=responseData
                                Log.e("responseData3",responsdata.toString())
                            }
                            else if (responseData1!=null&&responseData1.length()>0)
                            {
                                responsdata=responseData1
                            }
                            Log.e("responseData5",responsdata.toString())

                            for (i in 0 until responsdata.length()) {
                                var ProductId = responsdata.getJSONObject(i).getInt("ProductId")
                                var ProductName = responsdata.getJSONObject(i).getString("ProductName")
                                var UnitType = responsdata.getJSONObject(i).getString("UnitType")
                                var UnitPrice = responsdata.getJSONObject(i).getString("UnitPrice")
                                var NetPrice = responsdata.getJSONObject(i).getString("NetPrice")
                                var ImageUrl = responsdata.getJSONObject(i).getString("ImageUrl")
                                var RequiredQty = responsdata.getJSONObject(i).getString("RequiredQty")
                                Log.e("RequiredQty",RequiredQty.toString())
                                //    Log.e("DueBalance",twoDigitValue.)
                                SalsePersonOrderItems(ProductId,ProductName,UnitType,UnitPrice,NetPrice,ImageUrl,RequiredQty)
                            }
                            var totalOrder=modelClassOrderItems.size
//                            SalesDraftOrder.setText("Order List"+"("+totalOrder+")")
                            pDialog.dismiss()
                        } else {

                            var popUp = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            popUp.setContentText(ResponseMessage)
                            popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                            popUp.setConfirmClickListener()
                            { sDialog ->
                                sDialog.dismissWithAnimation()
//                        CustomerList("","",)
                            }
                            popUp.show()
                            popUp.setCanceledOnTouchOutside(false)
                            pDialog.dismiss()
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
        else{
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

    private fun SalsePersonOrderItems(
        productId: Int,
        productName: String,
        unitType: String,
        unitPrice: String,
        netPrice: String,
        imageUrl: String,
        requiredQty: String
    ) {
        var ModelClassCustomer1 = OrderItemModelClass(productId,productName,unitType,unitPrice,netPrice,imageUrl,requiredQty)
        modelClassOrderItems.add(ModelClassCustomer1)
        val recyclerview = findViewById<RecyclerView>(R.id.OrderCustomerDetailsRecyclerView)
        customerOrderItemAdapter = SalesPersonItemAdapterClass(modelClassOrderItems, this)
        recyclerview.adapter = customerOrderItemAdapter

    }
}