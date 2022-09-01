package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassCustomerList
import com.example.whm.ui.Sales_Person.CustomerListActivity as CustomerListActivity1

class AdapterClassCustomerList(
    private val CustomerList:List<ModelClassCustomerList>,
    var Listdata: Context?
    ):RecyclerView.Adapter<AdapterClassCustomerList.ViewHolder>(){
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var customerID: TextView = itemView.findViewById(R.id.customerID)
        var CustomerName: TextView = itemView.findViewById(R.id.customerName1)
        var Duevalue: TextView = itemView.findViewById(R.id.Duevalue)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterClassCustomerList.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.customer_list, parent, false)
        return AdapterClassCustomerList.ViewHolder(view)
    }
    override fun onBindViewHolder(holder: AdapterClassCustomerList.ViewHolder, position: Int) {
        var CustomerList = CustomerList[position]
        holder.CustomerName.text = CustomerList.getCN().toString()
        holder.customerID.text = CustomerList.getCId().toString()
        var deuBalance=CustomerList.getDueBalance().toString()
        if (deuBalance>"0") {
                          holder.Duevalue.setText("Due : $"+deuBalance.toFloat())
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