package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassCustomerList
class AdapterClassCustomerList(
    private val CustomerList:List<ModelClassCustomerList>,
    var CustomerListActivity:Context?
    ):RecyclerView.Adapter<AdapterClassCustomerList.ViewHolder>(){
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var customerID: TextView = itemView.findViewById(R.id.customerID)
        var CustomerName: TextView = itemView.findViewById(R.id.customerName1)
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

            //Toast.makeText(UpdateLocation,UpdateLocationList.getAId().toString(),Toast.LENGTH_LONG).show()
        }
    override fun getItemCount(): Int {

        return CustomerList.size
    }
}