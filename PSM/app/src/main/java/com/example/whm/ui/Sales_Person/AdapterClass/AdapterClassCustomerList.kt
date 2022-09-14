package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassCustomerList
import com.example.whm.ui.Sales_Person.CustomerDetailsActivity


class AdapterClassCustomerList(private val CustomerList:List<ModelClassCustomerList>,
    var Listdata: Context?
    ):RecyclerView.Adapter<AdapterClassCustomerList.ViewHolder>(){
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var customerID: TextView = itemView.findViewById(R.id.customerID)
        var CustomerName: TextView = itemView.findViewById(R.id.customerName1)
        var Duevalue: TextView = itemView.findViewById(R.id.Duevalue)
        var CustomerListCard: CardView = itemView.findViewById(R.id.CustomerListCard)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.customer_list, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        var CustomerList = CustomerList[position]
        holder.CustomerName.text = CustomerList.getCN().toString()
        holder.customerID.text = CustomerList.getCId().toString()
        var ContectPerson=CustomerList.getCPN()
        var MobileNO=CustomerList.getMN()
        var Email=CustomerList.getEmail()
        var BlingAddress=CustomerList.getBAdd()
        var SippingAddress=CustomerList.getSAdd()
        var priceLavle=CustomerList.getPLN()
        var storeNo=CustomerList.getC1()
        var dueblance=CustomerList.getDueBalances()
        var storeCredit=CustomerList.getSCA()
        var customerTerms=CustomerList.getCT()
        var ctype=CustomerList.getctype()
        var OnRoute=CustomerList.getOnRoute()
        var deuBalance=CustomerList.getDueBalances()!!.toFloat()
        holder.CustomerListCard.setOnClickListener(View.OnClickListener {
            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(Listdata)
            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putString("CustomerName", CustomerList.getCN().toString())
            sharedLoadOrderPage.putString("customerId", CustomerList.getCId().toString())
            //sharedLoadOrderPage.putString("UpdateLocation",ValueUpdate.toString())

            sharedLoadOrderPage.apply()
            var intent: Intent = Intent(Listdata, CustomerDetailsActivity::class.java)
            intent.putExtra("ContectPerson",ContectPerson)
            intent.putExtra("MobileNO",MobileNO)
            intent.putExtra("Email",Email)
            intent.putExtra("BlingAddress",BlingAddress)
            intent.putExtra("SippingAddress",SippingAddress)
            intent.putExtra("priceLavle",priceLavle)
            intent.putExtra("storeNo",storeNo)
            intent.putExtra("dueblance",deuBalance)
            intent.putExtra("storeCredit",storeCredit)
            intent.putExtra("customerTerms",customerTerms)
            intent.putExtra("ctype",ctype)
            intent.putExtra("OnRoute",OnRoute)
            Listdata?.startActivity(intent)
            (Listdata as Activity).finish()
        })

        if (deuBalance>0.00) {
                          holder.Duevalue.setText("Due : $"+"%.2f".format(deuBalance))
                              holder.Duevalue.visibility=View.VISIBLE
                           }
            //Toast.makeText(UpdateLocation,UpdateLocationList.getAId().toString(),Toast.LENGTH_LONG).show()
        }
    override fun getItemCount(): Int {
        var CustomerlistSize=CustomerList.size.toString()
        val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(Listdata)
        val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
        sharedLoadOrderPage.putString("CustomerlistSize", CustomerlistSize)
        sharedLoadOrderPage.apply()

        return CustomerList.size
    }
}