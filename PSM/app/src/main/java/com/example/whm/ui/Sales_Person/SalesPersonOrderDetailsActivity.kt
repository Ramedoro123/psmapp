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
import com.example.myapplication.com.example.whm.MainActivity2
import com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass.RemarkAdapterClass
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
    lateinit var discountPercentlevel:TextView
    lateinit var discountAmountlevel:TextView
    lateinit var shippingCharge:TextView
    lateinit var discountPersentText:TextView
    lateinit var discountAmountText:TextView
    lateinit var shipingtext:TextView
    lateinit var salestext:TextView
    lateinit var Salsetax:TextView
    lateinit var textView81:View
    lateinit var textView84:View
    lateinit var textView48:View
    lateinit var textView61:View
    lateinit var textView69:View
    lateinit var textView75:View
    lateinit var remarks: CardView
    var orderAutoId:String?=null
    var responsdata=JSONArray()
    var RemarkLists=JSONArray()
    var modelClassOrderItems: ArrayList<OrderItemModelClass> = arrayListOf()
    var remarksItems: ArrayList<remarks> = arrayListOf()
    lateinit var customerOrderItemAdapter: SalesPersonItemAdapterClass
    lateinit var remarkAdapterClass: RemarkAdapterClass
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
//        salesPersonRem=findViewById(R.id.salesPersonRem)
//        salesPreRemarkvalue=findViewById(R.id.salesPreRemarkvalue)
//        driverRemarkText=findViewById(R.id.driverRemarkText)
//        driverRemarkValue=findViewById(R.id.driverRemarkValue)
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
        discountAmountlevel=findViewById(R.id.discountAmountlevel)
        discountPercentlevel=findViewById(R.id.discountPercentlevel)
        shippingCharge=findViewById(R.id.shippingCharge)
        Salsetax=findViewById(R.id.Salsetax)
        salestext=findViewById(R.id.salsetext)
        textView81=findViewById(R.id.textView81)
        textView84=findViewById(R.id.textView84)
        textView48=findViewById(R.id.textView48)
        textView61=findViewById(R.id.textView61)
        textView69=findViewById(R.id.textView69)
        textView75=findViewById(R.id.textView75)
        remarks=findViewById(R.id.remarks)
        discountPersentText=findViewById(R.id.discountPersentText)
        discountAmountText=findViewById(R.id.discountAmountText)
        shipingtext=findViewById(R.id.shipingtext)
        btnBackOrderDetails.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@SalesPersonOrderDetailsActivity,OrderList::class.java))
            finish()
        })

        orderAutoId = intent.getStringExtra("orderAutoId")
        Log.e("orderAutoId",orderAutoId.toString())
        if (AppPreferences.internetConnectionCheck(this)) {
            val recyclerview = findViewById<RecyclerView>(R.id.OrderCustomerDetailsRecyclerView)
            val recyclerviews = findViewById<RecyclerView>(R.id.recyclerViewRemarks)
            val layoutManager1 = LinearLayoutManager(this)
            val layoutManager = LinearLayoutManager(this)
            recyclerview.layoutManager = layoutManager
            recyclerviews.layoutManager = layoutManager1
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
                            var State1=responsDataObject.getString("State1")
                            var City1=responsDataObject.getString("City1")
                            var Zipcode1=responsDataObject.getString("Zipcode1")

                            var ShipAddr=responsDataObject.getString("ShipAddr")
                            var State2=responsDataObject.getString("State2")
                            var City2=responsDataObject.getString("City2")
                            var Zipcode2=responsDataObject.getString("Zipcode2")



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
                            var BalanceAmount=responsDataObject.getString("BalanceAmount")
                            var TotalAmount=responsDataObject.getString("TotalAmount")
                            var TotalTax=responsDataObject.getString("TotalTax")
                            var DiscountAmount=responsDataObject.getString("DiscountAmount")
                            var DiscountPercent=responsDataObject.getString("DiscountPercent")
                            var ShippingCharges=responsDataObject.getString("ShippingCharges")

                            orderNumber.setText(OrderNo.toString())
                            orderdate.setText(OrderDate.toString())
                            customerNameSp.setText(CustomerName.toString())
                            shippingaddressSp.setText(ShipAddr.toString()+"\n"+City2+", "+State2+"-"+Zipcode2)
                            billingAddressSp.setText(BillAddr.toString()+"\n"+City1+", "+State1+"-"+Zipcode1)
                            shippingTypeSp.setText(ShippingTypeName)
                            status.setText(Status.toString())
                            customeridCS.setText(CustomerId.toString())
                            deliveryDateSp.setText(DeliveryDate.toString())
                            salesTaxType.setText(TaxLabel.toString())

                            if (DiscountAmount!=""&&DiscountAmount!="0"&&DiscountAmount!=null)
                            {
                                var discountAmount=DiscountAmount.toString().toFloat()
                                discountAmountlevel.visibility=View.VISIBLE
                                textView48.visibility=View.VISIBLE
                                discountAmountText.visibility=View.VISIBLE
                                discountAmountlevel.setText("$" + "%.2f".format(discountAmount.toDouble()))

                            }else{
                                discountAmountlevel.visibility=View.GONE
                                textView48.visibility=View.GONE
                                discountAmountText.visibility=View.GONE
                            }
                            if (DiscountPercent!=""&&DiscountPercent!="0"&&DiscountPercent!=null)
                            {
                                var discountPercent=DiscountPercent.toString().toFloat()
                                discountPercentlevel.visibility=View.VISIBLE
                                textView61.visibility=View.VISIBLE
                                discountPersentText.visibility=View.VISIBLE
                                discountPercentlevel.setText("$" + "%.2f".format(discountPercent.toDouble()))
                            }else{
                                discountPercentlevel.visibility=View.GONE
                                textView61.visibility=View.GONE
                                discountPersentText.visibility=View.GONE
                            }
                            if (ShippingCharges!=""&&ShippingCharges!="0"&&ShippingCharges!=null)
                            {
                                var shippingCharges=ShippingCharges.toString().toFloat()
                                shippingCharge.visibility=View.VISIBLE
                                textView69.visibility=View.VISIBLE
                                shipingtext.visibility=View.VISIBLE
                                shippingCharge.setText("$" + "%.2f".format(shippingCharges.toDouble()))
                            }else{
                                shippingCharge.visibility=View.GONE
                                textView69.visibility=View.GONE
                                shipingtext.visibility=View.GONE
                            }
                            if(TotalTax!=""&&TotalTax!=null&&TotalTax!="0"){
                                var salsetax=TotalTax.toString().toFloat()
                                Salsetax.visibility=View.VISIBLE
                                salestext.visibility=View.VISIBLE
                                textView75.visibility=View.VISIBLE
                                Salsetax.setText("$" + "%.2f".format(salsetax.toDouble()))

                            }else{
                                Salsetax.visibility=View.GONE
                                salestext.visibility=View.GONE
                                textView75.visibility=View.GONE
                            }



                            if (SubTotal!=null) {
                                var subTotal=SubTotal.toString().toFloat()
                                subTotalOrderdetails.setText("$" + "%.2f".format(subTotal.toDouble()))
                              }
                            if (PaidMaount!=null) {
                                var paidAmount=PaidMaount.toString().toFloat()
                                paidAmountValue.setText("$" + "%.2f".format(paidAmount.toDouble()).toString())
                            }
                            if (AdjustmentAmt!=null&&AdjustmentAmt!=""&&AdjustmentAmt!="0") {
                                var adjustment=AdjustmentAmt.toString().toFloat()
                                AdjAmountvalue.setText("$" + "%.2f".format(adjustment.toDouble()))
                                AdjAmountvalue.visibility=View.VISIBLE
                                adjText.visibility=View.VISIBLE
                             }
                            else{
                                AdjAmountvalue.visibility=View.GONE
                                adjText.visibility=View.GONE
                            }
                             if (PayableAmount!=null){
                                var payable=PayableAmount.toString().toFloat()
                                payableAmountValue.setText("$" + "%.2f".format(payable.toDouble()))
                             }
                            if (BalanceAmount!=null){
                                var balanceAmount=BalanceAmount.toString().toFloat()
                                balanceamountValue.setText("$" + "%.2f".format(balanceAmount.toDouble()))

                              }
                            if (TotalAmount!=null){
                                var totalAmount=TotalAmount.toString().toFloat()
                                orderTotalvalue.setText("$" + "%.2f".format(totalAmount.toDouble()))

                            }
                             if (Weigth_OZQty!=null&&Weigth_OZQty!=""&&Weigth_OZQty!="0"&&Weigth_OZTax!=null&&Weigth_OZTax!=""
                                &&Weigth_OZTaxAmount!=""&&Weigth_OZTaxAmount!=null&&Weigth_OZTaxAmount!="0"){
                                 weighttaxQty.visibility=View.VISIBLE
                                 wegitTaxValue.visibility=View.VISIBLE
                                 textView84.visibility=View.VISIBLE
                                var weightQty=Weigth_OZQty.toString().toFloat()
                                var WeigthTax=Weigth_OZTax.toString().toFloat()
                                var WeigthTaxAmount=Weigth_OZTaxAmount.toString().toFloat()
                                weighttaxQty.setText("Weight Tax ("+WeigthTax+"%) "+"(Qty : %.2f".format(weightQty.toDouble()) + ") :")
                                wegitTaxValue.setText("$" + "%.2f".format(WeigthTaxAmount.toDouble()))
                            }else{
                                 weighttaxQty.visibility=View.GONE
                                 wegitTaxValue.visibility=View.GONE
                                 textView84.visibility=View.GONE
                             }
                            if (MLQty!=""&&MLQty!="0"&&MLQty!=null&& MLTax!=null&& MLTax!=""&& MLTax!="0"&& MLTaxPer!=""&& MLTaxPer!=null)
                            {
                                mlTaxQty.visibility = View.VISIBLE
                                taxML.visibility = View.VISIBLE
                                textView81.visibility=View.VISIBLE
                               var mltax=MLTax.toString().toFloat()
                               var MLQty=MLQty.toString().toFloat()
                               var MLTaxPer=MLTaxPer.toString().toFloat()
                                mlTaxQty.setText("ML Tax ("+MLTaxPer+"%) "+"(Qty : %.2f".format(MLQty.toDouble()) + ") :")
                                taxML.setText("$" + "%.2f".format(mltax.toDouble()))

                            }
                            else{
                                mlTaxQty.visibility = View.GONE
                                taxML.visibility = View.GONE
                                textView81.visibility=View.GONE
                            }
                            if (DueAmount!=null&&DueAmount!=""&&DueAmount!="0")
                            {

                            }
                            else {
                            }
                            Log.e("responsDataObjectall",responsDataObject.toString())
                            val responseData = responsDataObject.getJSONArray("OrderItemList")
                            val responseData1 = responsDataObject.getJSONArray("DelItemList")
                            val RemarkList = responsDataObject.getJSONArray("RemarkList")
                            if (responseData!=null&&responseData.length()>0)
                            {
                                responsdata=responseData
                                Log.e("responseData3",responsdata.toString())
                            }
                            else if (responseData1!=null&&responseData1.length()>0)
                            {
                                responsdata=responseData1
                            }
                           if (RemarkList!=null&&RemarkList.length()>0)
                            {
                                RemarkLists=RemarkList
                                remarks.visibility=View.VISIBLE
                            }
                           else{
                                remarks.visibility=View.GONE
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

                            for (i in 0 until RemarkLists.length())
                            {
                                var EmpType = RemarkLists.getJSONObject(i).getString("EmpType")
                                var Remarks = RemarkLists.getJSONObject(i).getString("Remarks")
                                var EmpName = RemarkLists.getJSONObject(i).getString("EmpName")
                                allRemarks(EmpType,Remarks,EmpName)
                            }
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

    private fun allRemarks(empType: String, remarks: String, empName: String) {
        var ModelClassCustomer1 = remarks(empType,remarks,empName)
        remarksItems.add(ModelClassCustomer1)
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewRemarks)
        remarkAdapterClass = RemarkAdapterClass(remarksItems, this)
        recyclerview.adapter = remarkAdapterClass
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
public class remarks(empType:String,
                     remarks:String,
                     empName:String)
{
    private var empType: String
    private var remarks: String
    private var empName: String
    init {
       this.empType=empType
       this.remarks=remarks
       this.empName=empName
    }

    fun getempType():String?{
        return empType
    }
    fun getremarks():String?{
        return remarks
    }
    fun getempName():String?{
        return empName
    }
}