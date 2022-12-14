package com.example.whm.ui.inventoryreceive
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.inventoryreceive.DatePickerFragment
import org.json.JSONObject
import org.json.JSONArray
import android.widget.ArrayAdapter
import androidx.annotation.Nullable
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myapplication.com.example.whm.ui.home.HomeFragment


class FragmentInventory  : Fragment(R.layout.fragment_inventory_fragment){
    lateinit var mView: View
    var txtbildate: TextView? = null
    var Bill_No: TextView? = null
    var edtbillNo: EditText? = null
    var spvendorText:AutoCompleteTextView?= null
    var adapter: ArrayAdapter<String>? = null
    var spvendorid: String? = null
    var btnNext: Button?=null
    var test:Int?=0


    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_inventory_fragment, container, false)
        mView.requestFocus()
        mView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                }
            }
            true
        })
        txtbildate = mView.findViewById(R.id.txtbildate)
        edtbillNo = mView.findViewById(R.id.txtbillno)
        spvendorText = mView.findViewById(R.id.ddlvenderlist)
        btnNext = mView.findViewById(R.id.btnnext)
        edtbillNo!!.requestFocus()
        if (AppPreferences.internetConnectionCheck(context)) {
            spvendorText!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
               bindvenderlist()
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    bindvenderlist()
                }

                override fun afterTextChanged(s: Editable) {
                    // BindProductList()
                    bindvenderlist()
                }
            })
        }
        else {
            CheckInterNetDailog()
        }

        txtbildate?.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.setFragmentResultListener("REQUEST_KEY", viewLifecycleOwner) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        txtbildate?.text =  date
                        txtbildate!!.clearFocus()
                      //  spvendor!!.requestFocus()
                        edtbillNo!!.clearFocus()
                    }

                }
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
                datePickerFragment.enterTransition

            }
        if (AppPreferences.internetConnectionCheck(context)) {
            btnNext?.setOnClickListener {
                Bill_No = edtbillNo
                val t=spvendorid
                var vendorname = spvendorText!!.text.toString()
                val Bill_Date: CharSequence? = txtbildate?.text
                if (TextUtils.isEmpty(Bill_No?.text.toString())) {
                    EnertBill_No()
                }
                else if (TextUtils.isEmpty(Bill_Date.toString())) {
                    EnertBill_Date()

                }
                else if (vendorname.trim()==""||  spvendorid?.toIntOrNull()?.let{it}==null) {
                    Select_Vendor()
                }
                else {
                    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                    val editor = preferences.edit()
                    editor.putString("Bill_No", Bill_No?.text.toString())
                    editor.putString("Bill_Date", Bill_Date.toString())
                    spvendorid?.toIntOrNull()?.let { it1 -> editor.putInt("VENDORID", it1) }
                    editor.putString("VENDORName", spvendorText.toString())
                    editor.remove("DAutoid")
                    editor.remove("Status")

                    editor.apply()
                    CheckBillNo()

                }
            }
        }
        else {
            CheckInterNetDailog()
        }

   return mView

    }
    fun CheckInterNetDailog(){
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
        val btDismiss = dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
        btDismiss?.setOnClickListener {
            dialog.dismiss()
            this.findNavController().navigate(R.id.nav_home)
//            var intent = Intent(context, HomeFragment::class.java)
//            startActivity(intent)
            }
        dialog?.show()
    }
    fun bindvenderlist(){
        val Jsonarra = JSONObject()
        val JSONObj = JSONObject()
        val Jsonarraplist = JSONObject()
        val queues = Volley.newRequestQueue(context)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("UserAutoId", EmpAutoId))
        Log.e("EmpAutoId",EmpAutoId.toString())
        Log.e("EmpAutoId",accessToken.toString())
        JSONObj.put("requestContainer", Jsonarra.put("deviceID", Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)))
        var vendorvalue=spvendorText!!.text.toString().trim()
        JSONObj.put("cObj", Jsonarraplist.put("search",vendorvalue))

        val BINDPRODUCTLISTm = JsonObjectRequest(
            Request.Method.POST, AppPreferences.Bind_VENDER_LIST, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                Log.e("responseCode",responseCode)
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "200") {
                    val venderlist: JSONArray = resultobj.getJSONArray("responseData")
                    val n = venderlist.length()
                    val spinnerArray = arrayOfNulls<String>(n)
                    val spinnerArrayId = arrayOfNulls<String>(n)
                //   spinnerArray[0] = "Select Vendor"
                    for (i in 0 until n) {
                        val BINDLIST = venderlist.getJSONObject(i)
                        val VID = BINDLIST.getInt("Aid")
                        val VNAME = BINDLIST.getString("VName")
                        spinnerArray[i] = VNAME.toString()
                        spinnerArrayId[i] = VID.toString()
                    }
//                    val adapter = ArrayAdapter(this,
//                        android.R.layout.simple_dropdown_item_1line, spinnerArrayId)
//                    //  autotextView?.showDropDown()
                    adapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_dropdown_item_1line, spinnerArray
                        )
                    }
                    spvendorText?.threshold = 2
                    spvendorText?.setAdapter(adapter)
                    adapter?.setNotifyOnChange(true)
                    adapter?.notifyDataSetChanged()
                    spvendorText?.onItemClickListener =
                        AdapterView.OnItemClickListener { _, _, j, _ ->
                            spvendorid = spinnerArrayId[j].toString()
//                            qty!!.isEnabled =true
//                            btnpoadd?.isEnabled = true
//                            btnpoqtyminus?.isEnabled = true
                            //BindUnitList()


                        }
                }
//                else {
////                    spvendorText!!.text.clear()
////                    SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setContentText(responseMessage).show()
////                    spvendorText!!.text.clear()
//                }

            },
            { response ->

                Log.e("onError", error(response.toString()))
            })
        BINDPRODUCTLISTm.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(BINDPRODUCTLISTm)

    }


    fun CheckBillNo(){
        val Jsonarra = JSONObject()
        val Jsonarrabill = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this.context)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer",Jsonarra.put("deviceID",Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)))
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("UserAutoId", EmpAutoId)
        )
        JSONObj.put("cObj", Jsonarrabill.put("billNo", Bill_No?.text.toString()))
        JSONObj.put("cObj", Jsonarrabill.put("vendorAutoId", spvendorid))
        val BINDVENDERLIST = JsonObjectRequest(
            Request.Method.POST, AppPreferences.CHECK_BILL_NO, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    val intent = Intent(context, ReceivePO::class.java)
                    startActivity(intent)
                } else {

                    SweetAlertDialog(this.context, SweetAlertDialog.ERROR_TYPE).setContentText(responseMessage).show()


                }
            }, { response ->

                Log.e("onError", error(response.toString()))
            })
        BINDVENDERLIST.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(BINDVENDERLIST)
    }

    fun EnertBill_No() {
        SweetAlertDialog(this.context, SweetAlertDialog.ERROR_TYPE).setContentText("Enter Bill No").show()
    }
    fun EnertBill_Date() {
        SweetAlertDialog(this.context, SweetAlertDialog.ERROR_TYPE).setContentText("Select Bill Date").show()
    }
    fun Select_Vendor() {
        SweetAlertDialog(this.context, SweetAlertDialog.ERROR_TYPE).setContentText("Select Vendor").show()
    }
}

