package com.example.whm.ui.Sales_Person

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.example.myapplication.R

class CustomerDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_details)
        var btnBackarrow=findViewById<TextView>(R.id.btnBackOrderCustomer)
        var customerID=findViewById<TextView>(R.id.CSTID)
        var customerType=findViewById<TextView>(R.id.CstType)
        var customerNameCst=findViewById<TextView>(R.id.customerNameCst)
        var onRoute=findViewById<TextView>(R.id.onRoute)
        var storeCreditBalence=findViewById<TextView>(R.id.StoreCredit)
        var shippingAddressCst=findViewById<TextView>(R.id.shippingAddressCst)
        var CstBillingAddress=findViewById<TextView>(R.id.CstBillingAddress)
        var contectPerson=findViewById<TextView>(R.id.Contactperson)
        var storePhoneNo=findViewById<TextView>(R.id.storePhoneNo)
        var CstEmail=findViewById<TextView>(R.id.CstEmail)
        var priceLavelCst=findViewById<TextView>(R.id.priceLavelCst)
        var TermsId =findViewById<TextView>(R.id.termsId)
        var cstNumber=findViewById<TextView>(R.id.storeMobile)
        var customerDiscountValue=findViewById<TextView>(R.id.customerDiscountValue)
        var dueBalenceId=findViewById<TextView>(R.id.dueBlance)
        var moreOption=findViewById<ImageView>(R.id.moreOption)

        btnBackarrow.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,CustomerListActivity::class.java))
            finish()
        })
        val preferences= PreferenceManager.getDefaultSharedPreferences(this@CustomerDetailsActivity)
              var CustomerNames=preferences.getString("CustomerName","")
              var customerId=preferences.getString("customerId","")
        var Intent1: Intent
        Intent1= getIntent()
        contectPerson.setText(Intent1.getStringExtra("ContectPerson"))
        customerType.setText(Intent1.getStringExtra("ctype"))
        onRoute.setText(Intent1.getStringExtra("OnRoute"))
        var Strorebalence=Intent1.getStringExtra("storeCredit")
        var discount=Intent1.getStringExtra("CD")!!.toFloat()
        var balence=Intent1.getFloatExtra("dueblance",Float.MAX_VALUE)

        customerDiscountValue.setText("%.2f".format(discount.toFloat())+"%")
        cstNumber.setText(Intent1.getStringExtra("MobileNO"))
        TermsId.setText(Intent1.getStringExtra("customerTerms"))

        dueBalenceId.setText(("$"+"%.2f".format(balence)))
        CstEmail.setText(Intent1.getStringExtra("Email"))
        priceLavelCst.setText(Intent1.getStringExtra("priceLavle"))
        storePhoneNo.setText(Intent1.getStringExtra("storeNo"))
        shippingAddressCst.setText(Intent1.getStringExtra("SippingAddress"))
        CstBillingAddress.setText(Intent1.getStringExtra("BlingAddress"))

        storeCreditBalence.setText("$"+Strorebalence)
        customerNameCst.setText(CustomerNames)
        customerID.setText(customerId)

        moreOption.setOnClickListener(View.OnClickListener {
        val popupMenu=PopupMenu(this,it)
            val inflater: MenuInflater = popupMenu.getMenuInflater()
            inflater.inflate(R.menu.more_optionmenu,popupMenu.getMenu())
            var ChangeBillNo = popupMenu.menu?.findItem(R.id.NewOrder)
            if (ChangeBillNo != null) {
                ChangeBillNo.isVisible = true
            }
            popupMenu.setOnMenuItemClickListener { item->
                when(item.itemId){
                R.id.NewOrder->{
                    startActivity(Intent(this, SalesPersonProductList::class.java))
                 true
                }
                    else->false
                }
            }
            popupMenu.inflate(R.menu.more_optionmenu)
            popupMenu.show()
        })
    }
}



