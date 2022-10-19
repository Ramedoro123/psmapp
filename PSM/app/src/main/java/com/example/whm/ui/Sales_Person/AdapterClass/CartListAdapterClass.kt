package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.CartListModelClass
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.SalesPersonProductModel
import com.squareup.picasso.Picasso

class CartListAdapterClass(
    private val CartList: List<CartListModelClass>,
    var cartViewActicity: Context,
    private val listener:OnItemClickListener,
) : RecyclerView.Adapter<CartListAdapterClass.ViewHolder>(),View.OnClickListener {
    public interface OnItemClickListener {
        fun OnItemsClick(position: Int)
        fun OnDeleteClicks(position: Int)
    }
   inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView),View.OnClickListener{
        var cartPname=itemView.findViewById<TextView>(R.id.cartPname)
        val cartPid=itemView.findViewById<TextView>(R.id.cartPid)
        val cartPpiece=itemView.findViewById<TextView>(R.id.cartPpiece)
        val cartTotalAmount=itemView.findViewById<TextView>(R.id.cartTotalAmount)
        val isFreeAndExchenge=itemView.findViewById<TextView>(R.id.isFreeAndExchenge)
        val ProductImageCart=itemView.findViewById<ImageView>(R.id.ProductImageCart)
        val btnDeleteCartList=itemView.findViewById<TextView>(R.id.btnDeleteCartList)

       init {

           itemView.setOnClickListener(View.OnClickListener {
               val position = adapterPosition
               if (position != RecyclerView.NO_POSITION) {
                   listener.OnItemsClick(position)
               }
           })

           btnDeleteCartList.setOnClickListener(View.OnClickListener {
               val position = adapterPosition
               if (position != RecyclerView.NO_POSITION) {
                   listener.OnDeleteClicks(position)
               }
           })
       }

       override fun onClick(v: View?) {

       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cartlist_viewdetails, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var cartListData=CartList[position]
        holder.cartPname.text=cartListData.getPName()
        holder.cartPid.text=cartListData.getPId().toString()
        var priceProduct = cartListData.getUnitPrice()!!.toFloat()
        var UnitType = cartListData.getUnitType().toString()
        var ReqQty = cartListData.getReqQty().toString()
        var NetPrice = cartListData.getNetPrice()!!.toFloat()
        var isFree=cartListData.getFree()
        var isTaxable=cartListData.gettax()
        var isExchange=cartListData.getExchange()
        if (isTaxable!=0 &&isTaxable!=null)
        {
            holder.isFreeAndExchenge.visibility=View.VISIBLE
            holder.isFreeAndExchenge.setText("Taxable")
        }
        else if (isFree!=0&&isFree!=null){
            holder.isFreeAndExchenge.visibility=View.VISIBLE
            holder.isFreeAndExchenge.background.setTint(ContextCompat.getColor(cartViewActicity, R.color.Kelly_Green))
            holder.isFreeAndExchenge.setText("Free")
        }
        else if (isExchange!=0&&isExchange!=null)
        {
            holder.isFreeAndExchenge.visibility=View.VISIBLE
//            holder.isFreeAndExchenge.setBackgroundColor(Color.parseColor("#666EE8"))
            holder.isFreeAndExchenge.background.setTint(ContextCompat.getColor(cartViewActicity, R.color.purpleLite))
            holder.isFreeAndExchenge.setText("Exchange")
        }
        else{
            holder.isFreeAndExchenge.visibility=View.GONE
        }
        holder.cartTotalAmount.setText("$" + "%.2f".format(NetPrice))
        holder.cartPpiece.setText("$" + "%.2f".format(priceProduct)+"/" +UnitType+ " X" +ReqQty)
        var image=cartListData.getImgPath()
        if (image!=null &&image!="") {
            Picasso.get().load(image).error(R.drawable.default_pic).into(holder.ProductImageCart);
        }else{
            holder.ProductImageCart.setImageResource(R.drawable.default_pic)
        }

    }
    override fun getItemCount(): Int {

      return CartList.size
    }

    override fun onClick(v: View?) {
    }
}

