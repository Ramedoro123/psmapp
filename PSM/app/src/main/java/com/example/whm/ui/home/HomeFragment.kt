package com.example.myapplication.com.example.whm.ui.home

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var mView: View
    private var _binding: FragmentHomeBinding? = null
    private var StatusD: Int = 1
    private var StatusS: Int = 2
    private var StatusR: Int = 3

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(com.example.myapplication.R.layout.fragment_home, container, false)
        mView.requestFocus()
        mView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    false
                }
                true
            }
            true
        })
        val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        val btnInvCheck = mView.findViewById<Button>(com.example.myapplication.R.id.btnInvCheck)
        val btnAddBar = mView.findViewById<Button>(com.example.myapplication.R.id.btnAddBar)
        val btnDriLoad = mView.findViewById<Button>(com.example.myapplication.R.id.btnDriLoad)
        val btnUnLoadList =
            mView.findViewById<Button>(com.example.myapplication.R.id.btnunloadOrderlist)
        val btnOrderList = mView.findViewById<Button>(com.example.myapplication.R.id.btnOrderList)
        val btninventoryreceiv =
            mView.findViewById<Button>(com.example.myapplication.R.id.btninventoryreceiv)
        val btninventorypodraftlist =
            mView.findViewById<Button>(com.example.myapplication.R.id.btninventorypodraftlist)
        val btnsubmitpolist =
            mView.findViewById<Button>(com.example.myapplication.R.id.btnsubmitpolist)
        val btnrevertpolist =
            mView.findViewById<Button>(com.example.myapplication.R.id.btnrevertpolist)
        val btnpointernallist =
            mView.findViewById<Button>(com.example.myapplication.R.id.btnpointernallist)
        val btnProcessedOrder=mView.findViewById<Button>(com.example.myapplication.R.id.nav_Processed_order_list_Picker)
        val btnPacked_order=mView.findViewById<Button>(com.example.myapplication.R.id.nav_Packed_order_list_Picker)
        val btnAddOn_order=mView.findViewById<Button>(com.example.myapplication.R.id.nav_Add_On_order_list_Picker)
        val btnAddOnPacked_order=mView.findViewById<Button>(com.example.myapplication.R.id.nav_Add_OnPacked_order_list_Picker)
         val Update_Location=mView.findViewById<Button>(com.example.myapplication.R.id.nav_Update_Location)

        var Usertype = preferences.getString("EmpTypeNo", "")
        val editor = preferences.edit()
        if (Usertype.toString() != "2") {
            btnInvCheck.visibility = View.GONE
            btnAddBar.visibility = View.GONE
        }
        if (Usertype.toString() == "9") {

            btnInvCheck.visibility = View.VISIBLE
            btnAddBar.visibility = View.VISIBLE
            btnDriLoad.visibility = View.GONE
            btnUnLoadList.visibility = View.GONE
            btnOrderList.visibility = View.GONE

        }
        if (Usertype.toString() == "11") {
            //   btnInvCheck.visibility=   View.VISIBLE
            btnInvCheck.visibility = View.VISIBLE
            btnAddBar.visibility = View.VISIBLE
            btnDriLoad.visibility = View.GONE
            btnUnLoadList.visibility = View.GONE
            btnOrderList.visibility = View.GONE
            btninventoryreceiv.visibility = View.VISIBLE
            btninventorypodraftlist.visibility = View.VISIBLE
            btnsubmitpolist.visibility = View.VISIBLE
            btnrevertpolist.visibility = View.VISIBLE
            btnpointernallist.visibility = View.VISIBLE
        }
        if (Usertype.toString() == "14") {
            btnInvCheck.visibility = View.GONE
            btnAddBar.visibility = View.GONE
            btnDriLoad.visibility = View.GONE
            btnUnLoadList.visibility = View.GONE
            btnOrderList.visibility = View.GONE
            btninventoryreceiv.visibility = View.GONE
            btninventorypodraftlist.visibility = View.GONE
            btnsubmitpolist.visibility = View.GONE
            btnrevertpolist.visibility = View.GONE
            btnpointernallist.visibility = View.GONE
            btnProcessedOrder.visibility= View.VISIBLE
            btnPacked_order.visibility= View.VISIBLE
            btnAddOn_order.visibility= View.VISIBLE
            btnAddOnPacked_order.visibility= View.VISIBLE
            Update_Location.visibility= View.VISIBLE

        }
        btnInvCheck.setOnClickListener {

            this.findNavController().navigate(com.example.myapplication.R.id.nav_product)
        }

        btnAddBar.setOnClickListener {

            this.findNavController().navigate(com.example.myapplication.R.id.nav_addbar)
        }

        btnDriLoad.setOnClickListener {
            this.findNavController().navigate(com.example.myapplication.R.id.nav_productlist)
        }
        btnUnLoadList.setOnClickListener {
            this.findNavController().navigate(com.example.myapplication.R.id.nav_assignorder)
        }
        btnOrderList.setOnClickListener {
            this.findNavController().navigate(com.example.myapplication.R.id.nav_orderlist)
        }
        btninventoryreceiv.setOnClickListener {
            this.findNavController().navigate(com.example.myapplication.R.id.nav_inventory)
        }
        btninventorypodraftlist.setOnClickListener {

            this.findNavController().navigate(com.example.myapplication.R.id.nav_drfatpolist)
        }
        btnsubmitpolist.setOnClickListener {

            this.findNavController().navigate(com.example.myapplication.R.id.nav_submitpolist)
        }
        btnrevertpolist.setOnClickListener {

            this.findNavController().navigate(com.example.myapplication.R.id.nav_revertpolist)
        }

        btnpointernallist.setOnClickListener {

            this.findNavController().navigate(com.example.myapplication.R.id.nav_internalpos)
        }

        btnProcessedOrder.setOnClickListener{
            this.findNavController().navigate(com.example.myapplication.R.id.nav_Processed_order_list_Picker)
        }
        btnPacked_order.setOnClickListener{
            this.findNavController().navigate(com.example.myapplication.R.id.nav_Packed_order_list_Picker)
        }
        btnAddOn_order.setOnClickListener{

            this.findNavController().navigate(com.example.myapplication.R.id.nav_Add_On_order_list_Picker)

        }
        btnAddOnPacked_order.setOnClickListener{
            this.findNavController().navigate(com.example.myapplication.R.id.nav_Add_OnPacked_order_list_Picker)
        }
        Update_Location.setOnClickListener{
            this.findNavController().navigate(com.example.myapplication.R.id.nav_Update_Location)
        }
        return mView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


