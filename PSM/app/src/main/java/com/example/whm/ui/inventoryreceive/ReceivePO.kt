package com.example.whm.ui.inventoryreceive

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
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
import com.example.myapplication.com.example.whm.ui.UpdateLocation.setCanceledOnTouchOutside
import com.example.myapplication.com.example.whm.ui.inventoryreceive.DatePickerFragment
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceiveModel
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceivePOAdapter1
import com.example.whm.ui.draftpolist.draftpolistFragment
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class ReceivePO : AppCompatActivity() {
    // var backBTN: ImageView?=null
    var addbarcode: EditText? = null
    var backarrow: ImageView? = null
    var spUnitType: Spinner? = null
    var sproductid: String? = null
    var spunitypeid: String? = null
    var toolbar: Toolbar? = null
    var LinearLayoutV: LinearLayout? = null
    var noofitems: TextView? = null
    var autotextView: AutoCompleteTextView? = null
    var qty: TextView? = null
    var btnpoadd: ImageButton? = null
    var btnpoqtyminus: ImageButton? = null
    var totalpicesqty: TextView? = null
    var list: ArrayList<String>? = null
    var adapter: ArrayAdapter<String>? = null
    var ReceiverpoList: ArrayList<ReceiveModel> = arrayListOf()
    var spvendorid: String? = null
    var Quantity: Int = 0
    var totalpices: Int = 0
    var Qty: Int = 0
    var DUnit: Int = 0
    var DAutoid: Int = 0
    var Status: Int = 0
    var getdefault: String? = null
    var tqty: Int = 0
    var qtyperunit: Int = 0
    var BillDate: String? = ""
    var bill_No1: String? = ""
    val myCalendar = Calendar.getInstance()
    lateinit var Bill_Date: TextView
    lateinit var billNo1: EditText
    lateinit var mSilog: Dialog
    var ChangeBillNo: MenuItem? = null
    private lateinit var ReceivePOAdapterl: ReceivePOAdapter1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication.R.layout.activity_receive_po)
        toolbar = findViewById(R.id.toolbarAction)
        val preferencesid = PreferenceManager.getDefaultSharedPreferences(this@ReceivePO)
        Status = preferencesid.getInt("Status", 0)

        setSupportActionBar(toolbar)
        val editor = getSharedPreferences("CheckShared", MODE_PRIVATE).edit()
        if (Status == 3) {
            supportActionBar?.setTitle("Revert PO ")
            editor.putString("header", "Revert PO ")
        } else if (Status == 1) {
            fun onCreateOptionsMenu(menu: Menu?): Boolean {

                val inflater = menuInflater
                var ChangeBillNo: MenuItem? = null
                inflater.inflate(R.menu.menuitem, menu)
                ChangeBillNo = menu?.findItem(R.id.ChangeBillNo)
                if (ChangeBillNo != null) {
                    ChangeBillNo.isVisible = false
                }
                return super.onCreateOptionsMenu(menu)
            }
            supportActionBar?.setTitle("Draft PO ")
            editor.putString("header", "Draft PO ")

        } else {
            supportActionBar?.setTitle("Generate PO ")
            editor.putString("header", "Generate PO ")
        }
        editor.apply()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        addbarcode = findViewById(com.example.myapplication.R.id.enterbarcode)

        backarrow = findViewById(com.example.myapplication.R.id.imgbackbtm)
        LinearLayoutV = findViewById(com.example.myapplication.R.id.LinearFragmentContainer)
        btnpoadd = findViewById(R.id.btnpoadd)
        btnpoqtyminus = findViewById(R.id.btnpoqtyminus)
        DAutoid = preferencesid.getInt("DAutoid", 0)

        if (AppPreferences.internetConnectionCheck(this)) {
            if (DAutoid != null && DAutoid != 0) {
                val recyclerView: RecyclerView = findViewById(com.example.myapplication.R.id.POLIST)
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager

                Draftproductlist()

                ReceivePOAdapterl = ReceivePOAdapter1(ReceiverpoList, this)
                recyclerView.adapter = ReceivePOAdapterl
                ReceivePOAdapterl.notifyItemRangeChanged(0, ReceiverpoList.size);
            }
        } else {
            CheckInterNetDailog()
        }

        if (AppPreferences.internetConnectionCheck(this)) {
            backarrow?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@ReceivePO)
                    val editor = preferences.edit()
                    editor.remove("DAutoid")
                    val intent = Intent(this@ReceivePO, MainActivity2::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        } else {
            CheckInterNetDailog()
        }

        if (AppPreferences.internetConnectionCheck(this)) {

            addbarcode!!.requestFocus()
            addbarcode!!.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->

                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {

                    var scanbarcodeproduct = addbarcode!!.text.toString()

                    val recyclerView: RecyclerView =
                        findViewById(com.example.myapplication.R.id.POLIST)
                    val layoutManager = LinearLayoutManager(this)
                    recyclerView.layoutManager = layoutManager

                    if (scanbarcodeproduct.trim().isEmpty()) {
                        addbarcode!!.text.clear()
                        addbarcode!!.setText("")
                        Toast.makeText(this, "Scan product", Toast.LENGTH_SHORT).show()
                        addbarcode!!.requestFocus()
                    } else {
                        Addproductlist()
                    }
                    ReceivePOAdapterl = ReceivePOAdapter1(ReceiverpoList, this)
                    recyclerView.adapter = ReceivePOAdapterl
                }

                false

            })
        } else {
            CheckInterNetDailog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        var ChangeBillNo: MenuItem? = null
        var deletePo: MenuItem? = null
        inflater.inflate(R.menu.menuitem, menu)
        ChangeBillNo = menu?.findItem(R.id.ChangeBillNo)
        deletePo = menu?.findItem(R.id.deletePo)
        if (ChangeBillNo != null) {
            ChangeBillNo.isVisible = true
        }
        if (deletePo!=null){
            deletePo.isVisible = true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.savesubmit -> {
                if (ReceiverpoList.size > 0) {
                    SubmitPoList(2)
                    ReceiverpoList.clear()
                    true
                } else {
                    Itemlist()
//                    SubmitPoList(2)
                    false
                }

            }
            R.id.draftsubmit -> {
                if (ReceiverpoList.size > 0) {
                    SubmitPoList(1)
                    ReceiverpoList.clear()
                    true
                } else {
                    Itemlist()
//                    SubmitPoList(1)
                    false
                }
            }
            R.id.manualaddproduct -> {
                manualproductadd()
                true
            }
            R.id.ChangeBillNo -> {
                ChangeBillNo()
                false
            }
            R.id.deletePo -> {
                deletePoOrder()
                false
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deletePoOrder() {
        if (AppPreferences.internetConnectionCheck(this)) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            var accessToken = preferences.getString("accessToken", "")
            var empautoid = preferences.getString("EmpAutoId", "")
            var pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
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
                        this!!.contentResolver,
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
            if (DAutoid!=0) {
                sendRequestObject.put("cObj", pObj.put("draftAutoId", DAutoid))
            }

            Log.e("sendRequestObject order ordersubmit", sendRequestObject.toString())

            //send request queue in vally
            val queue = Volley.newRequestQueue(this)
            val JsonObjectRequest = JsonObjectRequest(Request.Method.POST,
                AppPreferences.deletePOOrder, sendRequestObject,
                { response ->
                    val responseResult = JSONObject(response.toString())
                    val responsedData = JSONObject(responseResult.getString("d"))
                    var responseMessage2 = responsedData.getString("responseMessage")
                    val responseStatus = responsedData.getInt("responseCode")
                    if (responseStatus == 201) {

                        var popUp = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        popUp.setContentText(responseMessage2.toString())
                        popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
                        popUp.setConfirmClickListener()
                        { sDialog ->
                            sDialog.dismissWithAnimation()
                            startActivity(Intent(this,MainActivity2::class.java))
//                            SalesDraftOrder.setText(" Draft Order List"+"("+modelClassDraftOrder.size+")")
//                            customerOrderAdapter.notifyDataSetChanged()
                             finish()
                            popUp.dismiss()
                            pDialog.dismiss()
                        }
                        popUp.show()
                        popUp.setCanceledOnTouchOutside(false)
                        popUp.setCancelable(false)
                        pDialog.setCancelable(false)

                    } else {
                        var popUp = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
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
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        } else {
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

    private fun ChangeBillNo() {
        var cal = Calendar.getInstance()
        val Dilogviews = View.inflate(this, R.layout.popupbox_updatelocation, null)
        val builder = AlertDialog.Builder(this).setView(Dilogviews)
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);
        mSilog = builder.show()
        var textView16 = Dilogviews.findViewById<TextView>(R.id.textView16)
        var textView27 = Dilogviews.findViewById<TextView>(R.id.textView27)
        billNo1 = Dilogviews.findViewById<EditText>(R.id.textValue)
        var ChangePotext = Dilogviews.findViewById<TextView>(R.id.ChangePotext)
        var view3 = Dilogviews.findViewById<View>(R.id.view3)
        Bill_Date = Dilogviews.findViewById<TextView>(R.id.Bill_Date)
        var OrderNumber = Dilogviews.findViewById<TextView>(R.id.OrderNoValue)
        var BoxValue = Dilogviews.findViewById<TextView>(R.id.BoxValue)
        var btnSave = Dilogviews.findViewById<TextView>(R.id.btnSave)
        var btnClose = Dilogviews.findViewById<TextView>(R.id.btnClose)
        Bill_Date?.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = this.supportFragmentManager
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                this
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    Bill_Date?.text = date
                    Bill_Date!!.clearFocus()
                    //  spvendor!!.requestFocus()
                    billNo1!!.clearFocus()
                }

            }
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            datePickerFragment.enterTransition

        }


        var bill_No1 = billNo1.text.trim().toString()
        var BillDate = Bill_Date.text.trim().toString()


        billNo1.hint = "Enter Bill No"
        textView16.visibility = View.GONE
        textView27.visibility = View.GONE
        view3.visibility = View.GONE
        Bill_Date.visibility = View.VISIBLE
        ChangePotext.visibility = View.VISIBLE
        OrderNumber.visibility = View.VISIBLE
        BoxValue.visibility = View.VISIBLE

        btnSave.setOnClickListener(View.OnClickListener {
            ChangeBillAndDate()
        })

        btnClose.setOnClickListener(View.OnClickListener { mSilog.dismiss() })

    }

    private fun ChangeBillAndDate() {
        if (AppPreferences.internetConnectionCheck(this)) {

            if (billNo1.text.trim().toString() == "") {
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(
                    "Please Enter Bill No."
                ).show()
                billNo1.requestFocus()
            } else if (Bill_Date.text.trim().toString() == "") {
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(
                    "Please Enter Bill Date."
                ).show()
                Bill_Date.requestFocus()
            } else {
                val Jsonarra = JSONObject()
                val Jsonarrabarcode = JSONObject()
                val JSONObj = JSONObject()
                val queues = Volley.newRequestQueue(this)
                val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                JSONObj.put(
                    "requestContainer",
                    Jsonarra.put("appVersion", AppPreferences.AppVersion)
                )
                JSONObj.put(
                    "requestContainer",
                    Jsonarra.put(
                        "deviceID",
                        Settings.Secure.getString(
                            this.contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    )
                )
                var accessToken = preferences.getString("accessToken", "")
                var EmpAutoId = preferences.getString("EmpAutoId", "")
                JSONObj.put(
                    "requestContainer",
                    Jsonarra.put("accessToken", accessToken)
                )
                JSONObj.put("requestContainer", Jsonarra.put("UserAutoId", EmpAutoId))
                JSONObj.put(
                    "cObj", Jsonarrabarcode.put("billNo", billNo1.text.toString())
                )
                JSONObj.put(
                    "cObj",
                    Jsonarrabarcode.put("billDate", Bill_Date.text.toString())
                )
                if (DAutoid != 0) {
                    JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", DAutoid))
                } else {
                    JSONObj.put(
                        "cObj",
                        Jsonarrabarcode.put("draftAutoId", DAutoid.toString().toInt())
                    )
                }
                Log.e("billNo", billNo1.text.toString())
                Log.e("Bill_Date", Bill_Date.text.toString())
                Log.e("DAutoid", DAutoid.toString())
                //Log.e("billNo",EmpAutoId.toString())

                val ChangeBillNo = JsonObjectRequest(Request.Method.POST,
                    AppPreferences.ChangeBillNo_And_Date,
                    JSONObj,
                    { response ->
                        val resobj = (response.toString())
                        val responsemsg = JSONObject(resobj)
                        val resultobj = JSONObject(responsemsg.getString("d"))
                        val responseCode = resultobj.getString("responseCode")
                        val responseMessage = resultobj.getString("responseMessage")
                        if (responseCode == "201") {
                            val dialog =
                                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            dialog.contentText = responseMessage
                            dialog.setCancelable(false)
                            dialog.show()
                            mSilog.dismiss()
                        } else {
                            val dialog2 =
                                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            dialog2.contentText = responseMessage
                            dialog2.setCancelable(false)
                            dialog2.show()
                            // mSilog.dismiss()
                        }
                    },
                    Response.ErrorListener { response ->

                        Log.e("onError", error(response.toString()))
                    })
                ChangeBillNo.retryPolicy = DefaultRetryPolicy(
                    1000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
                try {
                    queues.add(ChangeBillNo)
                } catch (e: java.lang.Exception) {
                    Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            val dialog = this?.let { Dialog(it) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
            val btDismiss =
                dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
            btDismiss?.setOnClickListener {
                dialog.dismiss()
                this.findNavController((com.example.myapplication.R.id.nav_home))
            }
        }
    }

    fun Itemlist() {
        var popUp = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        popUp.setContentText("Atleast One Item Required")
        popUp.cancelButtonBackgroundColor = Color.parseColor("#DC3545")
        popUp.setConfirmClickListener()
        { sDialog ->
            sDialog.dismissWithAnimation()
            popUp.dismiss()
        }
        popUp.show()
        popUp.setCanceledOnTouchOutside(false)
        popUp.setCancelable(false)
    }

    fun Addproductlist() {
        val barcodeadd: EditText = findViewById(com.example.myapplication.R.id.enterbarcode)
        val draftAutoIdTV: TextView = findViewById(com.example.myapplication.R.id.draftAutoId)
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            )
        )
        var accessToken = preferences.getString("accessToken", "")
        var Bill_No = preferences.getString("Bill_No", "")
        var Bill_Date = preferences.getString("Bill_Date", "")
        var VENDORID = preferences.getInt("VENDORID", 0)
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put(
            "requestContainer", Jsonarra.put("UserAutoId", EmpAutoId)
        )
        if (DAutoid != 0) {
            JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", DAutoid))
        } else {
            JSONObj.put(
                "cObj",
                Jsonarrabarcode.put("draftAutoId", draftAutoIdTV.text.toString().toInt())
            )
        }
        JSONObj.put("cObj", Jsonarrabarcode.put("billNo", Bill_No))
        JSONObj.put("cObj", Jsonarrabarcode.put("billDate", Bill_Date))
        JSONObj.put("cObj", Jsonarrabarcode.put("vendorAutoId", VENDORID.toInt()))
        JSONObj.put("cObj", Jsonarrabarcode.put("barcode", barcodeadd.text.toString()))
        JSONObj.put("cObj", Jsonarrabarcode.put("Remark", ""))

        val BARCODEADDPRODUCT =
            JsonObjectRequest(Request.Method.POST, AppPreferences.SCAND_BARCODE_PADD, JSONObj,
                Response.Listener { response ->
                    val resobj = (response.toString())
                    val responsemsg = JSONObject(resobj)
                    val resultobj = JSONObject(responsemsg.getString("d"))
                    val responseCode = resultobj.getString("responseCode")
                    val responseMessage = resultobj.getString("responseMessage")
                    if (responseCode == "201") {
                        val jsondata = resultobj.getString("responseData")
                        val JSONOBJ = JSONObject(jsondata.toString())
                        val draftAutoId = JSONOBJ.getInt("draftAutoId")
                        val ProductId = JSONOBJ.getInt("ProductId")
                        val ProductName = JSONOBJ.getString("ProductName")
                        val UnitType = JSONOBJ.getString("UnitType")
                        val Qty = JSONOBJ.getInt("Qty")
                        draftAutoIdTV.text = draftAutoId.toString()
                        var check = false
                        var poreqqty: Int = 0
                        for (n in 0..ReceiverpoList.size - 1) {
                            if (ReceiverpoList[n].getPID() == ProductId) {
                                check = true

                                if (ReceiverpoList[n].getPOQTY() != null) {
                                    poreqqty = ReceiverpoList[n].getPOQTY()!! + 1

                                    qtyperunit = Qty * poreqqty
                                    ReceivePOAdapterl.notifyItemChanged(n)
                                    ReceiverpoList.removeAt(n)
                                    DataBindPOLIST(
                                        ProductId,
                                        ProductName,
                                        UnitType,
                                        qtyperunit,
                                        poreqqty,
                                        draftAutoId,
                                        Qty
                                    )

                                }

                            }
                        }
                        if (!check) {
                            DataBindPOLIST(
                                ProductId,
                                ProductName,
                                UnitType,
                                Qty,
                                1,
                                draftAutoId,
                                Qty
                            )
                        }
                        if (ReceiverpoList.size != 0) {
                            noofitems?.text = "Total Items: " + ReceiverpoList.size.toString()
                            noofitems?.visibility = View.VISIBLE
                        }
                    } else {
                        val dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        dialog.contentText = responseMessage
                        dialog.setCancelable(false)
                        dialog.show()
                        var d = responseMessage.split("\\s".toRegex())[0]
                        if (d == "Barcode") {
                            AppPreferences.playSoundbarcode()
                        }
//                    Toast.makeText(this, , Toast.LENGTH_SHORT).show()

                    }
                    barcodeadd.setText("")
                    barcodeadd.requestFocus()
                }, Response.ErrorListener { response ->

                    Log.e("onError", error(response.toString()))
                })
        BARCODEADDPRODUCT.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(BARCODEADDPRODUCT)
    }


    private fun DataBindPOLIST(
        PID: Int, PNAME: String,
        UNITTYPE: String,
        UnitQTY: Int,
        POQTY: Int,
        DRaftID: Int,
        QtyperUnit: Int
    ) {
        var POLIST =
            ReceiveModel(PID, PNAME, UNITTYPE, UnitQTY, POQTY, UnitQTY, DRaftID, QtyperUnit)
        ReceiverpoList.add(0, POLIST)
        ReceivePOAdapterl.notifyDataSetChanged()
        ReceivePOAdapterl.notifyItemChanged(ReceiverpoList.size)
//        mAdapter.notifyItemRangeChanged(0, yourData.size());
    }


    fun SubmitPoList(Status: Int) {
         val draftAutoIdTV: TextView = findViewById(com.example.myapplication.R.id.draftAutoId)
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            )
        )
        JSONObj.put(
            "requestContainer", Jsonarra.put("UserAutoId", EmpAutoId)
        )
        if (DAutoid != 0) {
            JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", DAutoid))
            Log.e("draftAutoId1", DAutoid.toString())
        } else {
            JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId",  draftAutoIdTV.text.toString().toInt()))

        }

        JSONObj.put("cObj", Jsonarrabarcode.put("status", Status))

        Log.e("Status", Status.toString())
        Log.e("UserAutoId", EmpAutoId.toString())
        Log.e("accessToken", accessToken.toString())


        val SUBMITPOLITS = JsonObjectRequest(
            Request.Method.POST, AppPreferences.SUBMIT_PO_LIST, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))

                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    var alertbox = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    alertbox.contentText = responseMessage
                    alertbox.confirmText = "ok"
                    alertbox.setConfirmClickListener { sDialog ->
                        var intent = Intent(this, MainActivity2::class.java)
                        sDialog.dismiss()
                        finish()
                        startActivity(intent)

                    }
                    alertbox.setCanceledOnTouchOutside(false)
                    alertbox.show()
                    alertbox.setCancelable(false)

                } else {
                    var alertbox = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    alertbox.contentText = responseMessage
                    alertbox.cancelButtonBackgroundColor = Color.parseColor("#4cae4c")
                    alertbox.confirmText = "ok"
                    alertbox.confirmButtonBackgroundColor = Color.parseColor("#E60606")
                    alertbox.setConfirmClickListener { sDialog ->
//                        var intent = Intent(this, draftpolistFragment::class.java)
                        finish();
                        startActivity(getIntent());
                        sDialog.dismiss()
//                        startActivity(intent)


                    }
                    alertbox.setCanceledOnTouchOutside(false)
                    alertbox.show()

                }

            }, Response.ErrorListener { response ->

                Log.e("onError", error(response.toString()))
            })
        SUBMITPOLITS.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(SUBMITPOLITS)
    }


    fun CheckInterNetDailog() {
        val dialog = this.let { Dialog(it) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.example.myapplication.R.layout.dailog_log)
        val btDismiss =
            dialog.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
        btDismiss?.setOnClickListener {
            dialog.dismiss()
            var intent = Intent(this, ReceivePO::class.java)
            startActivity(intent)
            finish()
        }
        dialog.show()
    }


    fun manualproductadd() {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.manualproductadddailog, null)
        val btnpoqty: Button = view.findViewById(R.id.btnaddbarcode)
        val btncancel: Button = view.findViewById(R.id.btncancel)
        btnpoadd = view.findViewById(R.id.btnpoadd)
        btnpoqtyminus = view.findViewById(R.id.btnpoqtyminus)
        autotextView = view.findViewById<AutoCompleteTextView>(R.id.txtmpid)
        qty = view.findViewById<TextView>(R.id.qtym)
        qty!!.isEnabled =false
        btnpoadd?.isEnabled  = false
        btnpoqtyminus?.isEnabled  = false
        totalpicesqty = view.findViewById<TextView>(R.id.totalpicesqty)
        autotextView!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                BindProductList()
            }

            override fun afterTextChanged(s: Editable) {
                BindProductList()
            }
        })
        qty!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (qty!!.text.toString() != "") {
                    tqty=getdefault!!.trim().toInt()
                    Quantity = qty!!.text.toString().toInt()
                    totalpices = tqty  * Quantity
                    totalpicesqty!!.text = totalpices.toString()
                }
                else{
                    totalpicesqty!!.text = ""
                }
            }
        })

        spUnitType = view.findViewById(R.id.spunity)


        btnpoqty.setOnClickListener(View.OnClickListener {
            var name: String? = null
            name = spUnitType!!.selectedItem as String?
            var productname=autotextView!!.text.toString()
            if (spUnitType!!.selectedItem != null && qty!!.text.toString()!="" && qty!!.text.toString().toInt()!=0 && productname!="") {
                val recyclerView: RecyclerView =
                    findViewById(com.example.myapplication.R.id.POLIST)
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                AddproductlistManual()
                ReceivePOAdapterl = ReceivePOAdapter1(ReceiverpoList, this)
                recyclerView.adapter = ReceivePOAdapterl
                dialog?.dismiss()
            }
            else {
                if (productname=="")  {
                    Select_product()
                }
                else if(name == null ){
                    Select_UnitType()
                }
                else{
                    Select_Qty()
                }
            }
        })
        btncancel.setOnClickListener(View.OnClickListener {
            dialog?.dismiss()
        })
        builder.setView(view)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        btnpoadd?.setOnClickListener {
            if(qty!!.text.toString()!=""){
                if (qty!!.text.toString().toInt() >=0) {
                    tqty=getdefault!!.trim().toInt()
                    Quantity = qty!!.text.toString().toInt()+1
                    totalpices = tqty * Quantity
                    totalpicesqty!!.text = totalpices.toString()
                    qty!!.text = Quantity.toString()
                }
                else{
                    totalpicesqty!!.text = "0"
                    qty!!.text = "0"
                }
            }
            else{
                qty!!.text = "1"
                tqty=getdefault!!.trim().toInt()
                Quantity = qty!!.text.toString().toInt()
                totalpicesqty!!.text = ((tqty * Quantity).toString())

            }

        }
        btnpoqtyminus!!.setOnClickListener {
            if (qty!!.text.toString() != "") {
                if (qty!!.text.toString().toInt() >0) {
                    tqty = getdefault!!.trim().toInt()
                    Quantity = qty!!.text.toString().toInt() - 1
                    totalpices = tqty * Quantity
                    totalpicesqty!!.text = totalpices.toString()
                    qty!!.text = Quantity.toString()
                }

            }
            else {
                totalpicesqty!!.text = "0"
                qty!!.text = "0"
            }
        }
    }

    fun Select_UnitType() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Select Unit Type")
            .show()
    }

    fun Select_Qty() {
        SweetAlertDialog(
            this,
            SweetAlertDialog.ERROR_TYPE
        ).setContentText("Quantity can not be left empty or zero.").show()
    }

    fun Select_product() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Select Product ").show()
    }

    fun BindUnitList() {
        val Jsonarra = JSONObject()
        val Jsonarryproduct = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            )
        )
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("UserAutoId", EmpAutoId))
        JSONObj.put("requestContainer", Jsonarra.put("filterkeyword", Jsonarryproduct))
        JSONObj.put("filterkeyword", Jsonarryproduct.put("productId", sproductid))
        val BINDUNITTYPE = spUnitType
        val BindProductDetails = JsonObjectRequest(
            Request.Method.POST, AppPreferences.GET_PRODUCT_DETAILS, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    var UnitType: Int = 0
                    val unitlist = resultobj.getJSONArray("responseData")
                    val n = unitlist.length()
                    val spinnerArray = arrayOfNulls<String>(n)
                    val spinnerArrayId = arrayOfNulls<String>(n)
                    for (i in 0 until unitlist.length()) {
                        var unittype = unitlist.getJSONObject(i).getString("UName")

                        Qty = unitlist.getJSONObject(i).getInt("Qty")
                        UnitType = unitlist.getJSONObject(i).getInt("UnitType")
                        DUnit = unitlist.getJSONObject(i).getInt("DUnit")
                        if (DUnit == UnitType) {
                            spinnerArray[i] = unittype + " (" + Qty + "pcs) *"
                        } else {
                            spinnerArray[i] = unittype + " (" + Qty + "pcs)"
                        }
                        spinnerArrayId[i] = UnitType.toString()

                        BINDUNITTYPE?.adapter = this.let {
                            ArrayAdapter(
                                it,
                                R.layout.support_simple_spinner_dropdown_item,
                                spinnerArray
                            )
                        }
                        BINDUNITTYPE!!.setSelection(spinnerArrayId.indexOf(DUnit.toString()))
                        BINDUNITTYPE.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                }

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    spunitypeid = spinnerArrayId[position].toString()
                                    var spunitypename = spinnerArray[position].toString()
                                    if (spunitypename.contains("(")) {
                                        val result1 =
                                            spunitypename.trim().split("(").toMutableList()
                                        getdefault = result1[1].replace("pcs)", "").replace("*", "")
                                        // Toast.makeText(this@ReceivePO,  getdefault!!.trim().toString(),Toast.LENGTH_SHORT).show()
                                        totalpicesqty!!.text = getdefault.toString()
                                        var defaultqty: Int = 1
                                        qty!!.text = defaultqty.toString()

                                    }
                                }
                            }
                    }
                } else {
                    Toast.makeText(this@ReceivePO, responseMessage, Toast.LENGTH_SHORT).show()

                }

            }, { response ->

                Log.e("onError", error(response.toString()))
            })
        BindProductDetails.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(BindProductDetails)
    }

    fun BindProductList() {

        val Jsonarra = JSONObject()
        val JSONObj = JSONObject()
        val Jsonarraplist = JSONObject()
        val queues = Volley.newRequestQueue(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("UserAutoId", EmpAutoId))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            )
        )
        JSONObj.put("cObj", Jsonarraplist.put("search", autotextView!!.text))
        val BINDPRODUCTLISTm = JsonObjectRequest(
            Request.Method.POST, AppPreferences.BIND_PRODUCT_IDNAME_BY_SEARCH, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    val ProductList: JSONArray = resultobj.getJSONArray("responseData")
                    val n = ProductList.length()
                    val productArray = arrayOfNulls<String>(n)
                    val productArrayId = arrayOfNulls<String>(n)

                    for (i in 0 until n) {
                        val BINDLIST = ProductList.getJSONObject(i)
                        val PID = BINDLIST.getInt("PId")
                        val PNAME = BINDLIST.getString("PName")
                        productArray[i] = PID.toString() + "-" + PNAME
                        productArrayId[i] = PID.toString()
                    }
//                    val adapter = ArrayAdapter(this,
//                        android.R.layout.simple_dropdown_item_1line, productArray)
                    //  autotextView?.showDropDown()
                    adapter = this?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_dropdown_item_1line, productArray
                        )
                    }
                    autotextView?.threshold = 2
                    autotextView?.setAdapter(adapter)
                    adapter?.setNotifyOnChange(true)
                    adapter?.notifyDataSetChanged()
                    autotextView?.onItemClickListener =
                        OnItemClickListener { _, _, j, _ ->
                            sproductid = productArrayId[j].toString()
                            qty!!.isEnabled =true
                            btnpoadd?.isEnabled = true
                            btnpoqtyminus?.isEnabled = true
                            BindUnitList()
                        }
                } else {
//                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(
//                        responseMessage
                    //).show()
                }
            }, { response ->

                Log.e("onError", error(response.toString()))
            })
        BINDPRODUCTLISTm.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(BINDPRODUCTLISTm)
    }

    fun AddproductlistManual() {
        val draftAutoIdTV: TextView = findViewById(com.example.myapplication.R.id.draftAutoId)
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer",Jsonarra.put("deviceID", Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)))
        var accessToken = preferences.getString("accessToken", "")
        var Bill_No = preferences.getString("Bill_No", "")
        var Bill_Date = preferences.getString("Bill_Date", "")
        var VENDORID = preferences.getInt("VENDORID", 0)
        var VENDORName   = preferences.getString("VENDORName", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("UserAutoId", EmpAutoId)
        )

        if(DAutoid!=0){
            JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", DAutoid))
        }else{
            JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", draftAutoIdTV.text.toString().toInt()))
        }

        JSONObj.put("cObj", Jsonarrabarcode.put("billNo", Bill_No))
        JSONObj.put("cObj", Jsonarrabarcode.put("billDate", Bill_Date))
        JSONObj.put("cObj", Jsonarrabarcode.put("vendorAutoId", VENDORID.toInt()))
        JSONObj.put("cObj", Jsonarrabarcode.put("productId", sproductid))
        JSONObj.put("cObj", Jsonarrabarcode.put("UnitAutoId", spunitypeid))
        JSONObj.put("cObj", Jsonarrabarcode.put("Remark", ""))
        JSONObj.put("cObj", Jsonarrabarcode.put("pOQty", qty!!.text.toString().toInt()))
        val BARCODEADDPRODUCT = JsonObjectRequest(
            Request.Method.POST, AppPreferences.ADD_PRODUCT_MANAUL, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    val jsondata = resultobj.getString("responseData")
                    val JSONOBJ = JSONObject(jsondata.toString())
                    val draftAutoId = JSONOBJ.getInt("draftAutoId")
                    val ProductId = JSONOBJ.getInt("ProductId")
                    val ProductName = JSONOBJ.getString("ProductName")
                    val UnitType = JSONOBJ.getString("UnitType")
                    val Qty = JSONOBJ.getInt("Qty")
                    //  Toast.makeText(this,Qty.toString(),Toast.LENGTH_SHORT).show()
                    draftAutoIdTV.text=draftAutoId.toString()
                    var check=false
                    var poreqqty:Int=0

                    for (n in 0..ReceiverpoList.size-1) {
                        if(ReceiverpoList[n].getPID()==ProductId){
                            check=true
                            if (ReceiverpoList[n].getPOQTY() != null) {
                                poreqqty = ReceiverpoList[n].getPOQTY()!! + qty!!.text.toString().toInt()
                                qtyperunit = Qty * poreqqty
                                ReceivePOAdapterl.notifyItemChanged(n)
                                ReceiverpoList.removeAt(n)
                                DataBindPOLIST(
                                    ProductId,
                                    ProductName,
                                    UnitType,
                                    qtyperunit,
                                    poreqqty,
                                    draftAutoId,
                                    Qty
                                )
                            }

                        }
                    }
                    if(!check) {
                        DataBindPOLIST(
                            ProductId,
                            ProductName,
                            UnitType,
                            totalpices,
                            qty!!.text.toString().toInt(),
                            draftAutoId,
                            Qty
                        )

                        //Toast.makeText(this,totalpices.toString(),Toast.LENGTH_SHORT).show()
                    }
                    if(ReceiverpoList.size!=0) {
                        noofitems?.text = "Total Items: " + ReceiverpoList.size.toString()
                        noofitems?.visibility=View.VISIBLE
                    }
                } else {
                    val dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    dialog.contentText = responseMessage
                    dialog.setCancelable(false)
                    dialog.show()
                }

            }, { response ->

                Log.e("onError", error(response.toString()))
            })
        BARCODEADDPRODUCT.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(BARCODEADDPRODUCT)
    }

    fun Draftproductlist() {
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            )
        )
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("userAutoId", EmpAutoId)
        )

        JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", DAutoid))

        val DRAFTADDPRODUCT = JsonObjectRequest(
            Request.Method.POST, AppPreferences.Draft_PRODUCT_List, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                var PID: Int = 0
                var UnitAutoId: Int = 0
                var TotalPieces: Int = 0
                var Quantity: Int = 0
                var QtyPerUnit: Int = 0
                var PName: String? = null
                var UnitType: String? = null
                // val BillNo:String?=null
                if (responseCode == "201") {
                    val jsondata = resultobj.getString("responseData")
                    val jsonrepdu = JSONObject(jsondata.toString())
                    val VendorAutoId = jsonrepdu.getInt("VendorAutoId")
                    val BillNo = jsonrepdu.getString("BillNo")
                    val draftAutoId = jsonrepdu.getInt("DAutoId")
                    val BillDate = jsonrepdu.getString("BillDate")
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                    val editor = preferences.edit()
                    editor.putString("Bill_No", BillNo)
                    editor.putString("Bill_Date", BillDate.toString())
                    editor.putInt("VENDORID", VendorAutoId.toString().toInt())
                    editor.apply()

                    val Remarks = jsonrepdu.getString("Remarks")
                    val Status = jsonrepdu.getString("Status")
                    val POItems = jsonrepdu.getJSONArray("POItems")
                    for (i in 0 until POItems.length()) {
                        PName = POItems.getJSONObject(i).getString("PName")
                        PID = POItems.getJSONObject(i).getInt("PID")
                        UnitAutoId = POItems.getJSONObject(i).getInt("UnitAutoId")
                        UnitType = POItems.getJSONObject(i).getString("UnitType")
                        TotalPieces = POItems.getJSONObject(i).getInt("TotalPieces")
                        Quantity = POItems.getJSONObject(i).getInt("Quantity")
                        QtyPerUnit = POItems.getJSONObject(i).getInt("QtyPerUnit")
                        var check = false
                        var poreqqty: Int = 0

                        for (n in 0..ReceiverpoList.size - 1) {

                            if (ReceiverpoList[n].getPID() == PID) {
                                check = true
                                if (ReceiverpoList[n].getPOQTY() != null) {
                                    poreqqty = ReceiverpoList[n].getPOQTY()!! + 1
                                    ReceivePOAdapterl.notifyItemChanged(n)
                                    ReceivePOAdapterl.notifyItemRangeChanged(0, n);
                                    ReceiverpoList.removeAt(n)
                                    if (UnitType != null) {
                                        if (PName != null) {
                                            DataBindPOLIST(
                                                PID,
                                                PName,
                                                UnitType,
                                                QtyPerUnit,
                                                Quantity,
                                                draftAutoId,
                                                Qty
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        if (!check) {
                            if (UnitType != null) {
                                if (PName != null) {
                                    DataBindPOLIST(
                                        PID,
                                        PName,
                                        UnitType,
                                        TotalPieces,
                                        Quantity,
                                        draftAutoId,
                                        QtyPerUnit
                                    )
                                }

                            }

                        }

                    }
                    if (ReceiverpoList.size != 0) {
                        noofitems?.text = "Total Items: " + ReceiverpoList.size.toString()
                        noofitems?.visibility = View.VISIBLE


                    }
                } else {
                    var alertbox = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    alertbox.contentText = responseMessage
                    alertbox.confirmText = "Ok"
                    alertbox.confirmButtonBackgroundColor = Color.parseColor("#E60606")
                    alertbox.setConfirmClickListener { sDialog ->
                        val intent = Intent(this@ReceivePO, draftpolistFragment::class.java)
                        startActivity(intent)
                    }
                    alertbox.setCanceledOnTouchOutside(false)
                    alertbox.show()
                }
            }, { response ->

                Log.e("onError", error(response.toString()))
            })
        DRAFTADDPRODUCT.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(DRAFTADDPRODUCT)
    }

}













