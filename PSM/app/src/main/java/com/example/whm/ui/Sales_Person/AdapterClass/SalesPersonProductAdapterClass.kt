package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.SalesPersonProductModel
import com.squareup.picasso.Picasso
class SalesPersonProductAdapterClass(private val ProductItemList:List<SalesPersonProductModel>,
            var context: Context?):RecyclerView.Adapter<SalesPersonProductAdapterClass.ViewHolder>() {
                class ViewHolder(ItemView:View):RecyclerView.ViewHolder(ItemView){
                    var ProductName=itemView.findViewById<TextView>(R.id.SProductName)
                    var ProductdID=itemView.findViewById<TextView>(R.id.ProductdID)
                    var ProductPrice=itemView.findViewById<TextView>(R.id.ProductPrice)
                    var ProductStock=itemView.findViewById<TextView>(R.id.ProductStock)
                    var ProductImage=itemView.findViewById<ImageView>(R.id.ProductImage)
                }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.product_list,parent,false)
    return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var ProductItem=ProductItemList[position]
        holder.ProductName.text=ProductItem.getPName().toString()
        holder.ProductdID.text=ProductItem.getPId().toString()
        var priceProduct=ProductItem.getBP()!!.toFloat()
        var UnitType=ProductItem.getUnitType().toString()
        holder.ProductPrice.setText("$"+"%.2f".format(priceProduct)+"("+UnitType+")")
        var productStoct=ProductItem.getCStock()!!.toInt()
        if (productStoct==0)
        {
            holder.ProductStock.setText("Stock : "+productStoct.toString())
            holder.ProductStock.setTextColor(Color.parseColor("#DC3545"))
        }else{
            holder.ProductStock.setText("Stock : "+productStoct.toString())
            holder.ProductStock.setTextColor(Color.parseColor("#000000"))
        }
        Picasso.get().load(ProductItem.getImageUrl()).error(R.drawable.default_pic).into(holder.ProductImage);
    }
    override fun getItemCount(): Int {

        return ProductItemList.size
    }
}