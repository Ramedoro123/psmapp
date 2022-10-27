package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.OrderItemModelClass
import com.squareup.picasso.Picasso

class SalesPersonItemAdapterClass(private val OrderItemListData: ArrayList<OrderItemModelClass>, var Listdata: Context?,
//                                  private val listener: SalesPersonItemAdapterClass.OnItemClickListeners,
): RecyclerView.Adapter<SalesPersonItemAdapterClass.ViewHolder>() {

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView), View.OnClickListener {
        var cartPname=itemView.findViewById<TextView>(R.id.cartPname)
        val cartPid=itemView.findViewById<TextView>(R.id.cartPid)
        val cartPpiece=itemView.findViewById<TextView>(R.id.cartPpiece)
        val cartTotalAmount=itemView.findViewById<TextView>(R.id.cartTotalAmount)
        val isFreeAndExchenge=itemView.findViewById<TextView>(R.id.isFreeAndExchenge)
        val ProductImageCart=itemView.findViewById<ImageView>(R.id.ProductImageCart)
        val btnDeleteCartList=itemView.findViewById<TextView>(R.id.btnDeleteCartList)
        override fun onClick(v: View?) {

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SalesPersonItemAdapterClass.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cartlist_viewdetails, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalesPersonItemAdapterClass.ViewHolder, position: Int) {
        var orderItemPosition=OrderItemListData[position]
          holder.cartPname.text=orderItemPosition.getproductName().toString()
          holder.cartPid.text=orderItemPosition.getproductId().toString()
          holder.btnDeleteCartList.visibility=View.GONE
        var unitprice=orderItemPosition.getunitPrice().toString()
        var unitType=orderItemPosition.getunitType().toString()
        var image=orderItemPosition.getimageUrl().toString()
        var ReqQty=orderItemPosition.getrequiredQty().toString()
        var netPrice=orderItemPosition.getnetPrice().toString()

        if (unitprice!=null&&unitprice!=""&&unitType!=null&&unitType!="")
        {
            var unitprice=unitprice.toString().toFloat()
            //holder.ProductPrice.setText("$" + "%.2f".format(unitprice) + "(" + unitType + ")")
            holder.cartPpiece.setText("$" + "%.2f".format(unitprice)+"/" +unitType+ " X" +ReqQty)
        }
        if (netPrice!=""&&netPrice!=null)
        {
            var netprice=netPrice.toString().toFloat()
            holder.cartTotalAmount.setText("$" + "%.2f".format(netprice))
        }

        if (image!=null&&image!=""){
            holder.ProductImageCart.height.minus(30)
            Picasso.get().load(image).error(R.drawable.default_pic).into(holder.ProductImageCart);
        }else{
            holder.ProductImageCart.maxHeight.rangeTo(30)
            holder.ProductImageCart.setImageResource(R.drawable.default_pic)
        }
    }

    override fun getItemCount(): Int {
       return OrderItemListData.size
    }
}