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
        var customername: TextView = itemView.findViewById(R.id.productIdSalse)
        var ProductImage: ImageView = itemView.findViewById(R.id.ProductImage)
        var ProductdID: TextView = itemView.findViewById(R.id.ProductdID)
        var ProductPrice: TextView = itemView.findViewById(R.id.ProductPrice)


//        init {
//
//            itemView.setOnClickListener(View.OnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    listener.OnItemsClick(position)
//                }
//            })
//
//        }

        override fun onClick(v: View?) {

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SalesPersonItemAdapterClass.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalesPersonItemAdapterClass.ViewHolder, position: Int) {
        var orderItemPosition=OrderItemListData[position]
          holder.customername.text=orderItemPosition.getproductName().toString()
        var image=orderItemPosition.getimageUrl().toString()
        if (image!=null&&image!=""){
            Picasso.get().load(image).error(R.drawable.default_pic).into(holder.ProductImage);
        }else{
            holder.ProductImage.setImageResource(R.drawable.default_pic)
        }
    }

    override fun getItemCount(): Int {
       return OrderItemListData.size
    }
}