package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass

import android.content.Context
import android.system.Os.remove
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.CartListModleClass
import com.squareup.picasso.Picasso

class CartListAdapterClass(
    private val CartList: List<CartListModleClass>,
    var cartViewActicity: Context,
    private val listener:OnItemClickLitener,
) : RecyclerView.Adapter<CartListAdapterClass.ViewHolder>() {
    var removedPosition : Int ? = null
    var getCartListDetails: MutableList<CartListModleClass> = ArrayList()

    public interface OnItemClickLitener {
        fun OnItemClick(position: Int)
        fun OnDeleteClick(position: Int)
    }
   inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var cartPname=itemView.findViewById<TextView>(R.id.cartPname)
        val cartPid=itemView.findViewById<TextView>(R.id.cartPid)
        val cartPpiece=itemView.findViewById<TextView>(R.id.cartPpiece)
        val cartTotalAmount=itemView.findViewById<TextView>(R.id.cartTotalAmount)
        val ProductImageCart=itemView.findViewById<ImageView>(R.id.ProductImageCart)
        val btnDeleteCartList=itemView.findViewById<TextView>(R.id.btnDeleteCartList)

       init {

           itemView.setOnClickListener(View.OnClickListener {
               val position = adapterPosition
               if (position != RecyclerView.NO_POSITION) {
                   listener.OnItemClick(position)
               }
           })

           btnDeleteCartList.setOnClickListener(View.OnClickListener {
               val position = adapterPosition
               if (position != RecyclerView.NO_POSITION) {
                   listener.OnDeleteClick(position)
               }
           })
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
        var priceProduct = cartListData.getpUnitPrice()!!.toFloat()
        var UnitType = cartListData.getUnitType().toString()
        var ReqQty = cartListData.getReqQty().toString()
        var NetPrice = cartListData.getNetPrice()!!.toFloat()
        holder.cartTotalAmount.setText("$" + "%.2f".format(NetPrice))
        holder.cartPpiece.setText("$" + "%.2f".format(priceProduct)+"/" +UnitType+ " X" +ReqQty)
        Picasso.get().load(cartListData.getImgPath()).error(R.drawable.default_pic).into(holder.ProductImageCart);

//        holder.btnDeleteCartList.setOnClickListener {
//        //   deleteItem(index = position)
//            val actualPosition = holder.adapterPosition
//            CartList.remove(actualPosition)
//            notifyItemRemoved(actualPosition)
//            notifyItemRangeChanged(actualPosition, CartList.size)
//        }

    }
    fun deleteItem(index: Int){
        getCartListDetails.removeAt(index)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {

      return CartList.size
    }
}

