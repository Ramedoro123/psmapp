package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.SalesPersonProductModel
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.getCartdetailsModle
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.lang.reflect.Field
import java.util.regex.Pattern


class SalesPersonProductAdapterClass(
    private val ProductItemList: List<SalesPersonProductModel>,
    var data: Context?,
    private val listener: OnItemClickLitener,
) : RecyclerView.Adapter<SalesPersonProductAdapterClass.ViewHolder>(), View.OnClickListener {
    var TotalPrice:Float=0.0f
    var itemCounts: Int = 0
    var NetPrice:Float=0.0f
    public interface OnItemClickLitener {
        fun OnItemClick(position: Int)
        fun OnDeleteClick(position: Int)
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView),
        View.OnClickListener {

        var ProductName = itemView.findViewById<TextView>(R.id.productIdSalse)
        var ProductdID = itemView.findViewById<TextView>(R.id.ProductdID)
        var ProductPrice = itemView.findViewById<TextView>(R.id.ProductPrice)
        var ProductStock = itemView.findViewById<TextView>(R.id.ProductStock)
        var ProductImage = itemView.findViewById<ImageView>(R.id.ProductImage)
        var btnDeleteItem = itemView.findViewById<Button>(R.id.btnDeleteItem)
        var orderQtyProduct = itemView.findViewById<TextView>(R.id.orderQtyText)
        var OrderQtyValue = itemView.findViewById<TextView>(R.id.OrderQtyValue)
        var taxableLevel = itemView.findViewById<TextView>(R.id.taxableLevel)
        var netPrice = itemView.findViewById<TextView>(R.id.netPrice)
        var cardView5 = itemView.findViewById<CardView>(R.id.cardView5)

        init {

            itemView.setOnClickListener(View.OnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.OnItemClick(position)
                }
            })

            btnDeleteItem.setOnClickListener(View.OnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.OnDeleteClick(position)
                }
            })
        }

        override fun onClick(v: View?) {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.product_list, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var ProductItem = ProductItemList[position]
        holder.ProductName.text = ProductItem.getPName().toString()
        holder.ProductdID.text = ProductItem.getPId().toString()
        holder.OrderQtyValue.text = ProductItem.getNofItem().toString()
        var taxValue = ProductItem.getIsTaxable().toInt()
        var netPrices = ProductItem.getNetPrice().toString()
        var priceProduct = ProductItem.getBP()!!.toFloat()
        var UnitType = ProductItem.getUnitType().toString()
        holder.ProductPrice.setText("$" + "%.2f".format(priceProduct) + "(" + UnitType + ")")

        var unitPrice = ProductItem.getUnitPrice()!!.toFloat()

        var UnitTypes = ProductItem.getUnitType().toString()

        var ReQty=ProductItem.getReqQty().toString()

        var Qty = ProductItem.getOQty().toString()

        var Tax=ProductItem.getTax().toString()

        holder.netPrice.visibility = View.GONE
        holder.taxableLevel.visibility = View.GONE
        holder.OrderQtyValue.visibility = View.GONE
        holder.btnDeleteItem.visibility = View.GONE
        holder.orderQtyProduct.visibility = View.GONE
             Log.e("unitPrice",unitPrice.toString())
             Log.e("UnitTypes",UnitTypes.toString())
             Log.e("TaxAble Tax",taxValue.toString())

        if (Qty != null && Qty != "" && unitPrice != 0.2f && unitPrice != null && UnitTypes != null && UnitTypes != "" && Qty != "0"&&netPrices != null && netPrices!="" &&netPrices!="0.0" ||taxValue!=0 &&taxValue!=null &&ReQty!=null &&ReQty!="" &&ReQty!="0")
        {
            var netPrice=netPrices.toFloat()
            holder.OrderQtyValue.setText(Qty.toString())
            holder.orderQtyProduct.visibility = View.VISIBLE
            holder.OrderQtyValue.visibility = View.VISIBLE
            holder.netPrice.visibility = View.VISIBLE
            holder.btnDeleteItem.visibility = View.VISIBLE
            if (taxValue!=0 &&ReQty!="0") {
                holder.taxableLevel.visibility = View.VISIBLE
                holder.OrderQtyValue.setText(ReQty.toString())
                holder.OrderQtyValue.visibility = View.VISIBLE
                holder.taxableLevel.setText("Taxable")
            }else{
                holder.taxableLevel.visibility = View.GONE
            }
            holder.netPrice.setText("$" + "%.2f".format(netPrice))
            holder.ProductPrice.setText("$" + "%.2f".format(unitPrice) + "(" + UnitTypes + ")")
        }
        else if ((netPrices != null && netPrices!="" &&netPrices!="0.0" &&ReQty!="0" &&ReQty!=""&&ReQty!=null))
        {
            holder.OrderQtyValue.setText(ReQty.toString())
//            Log.e("ReQty 456465   d",Qty)
            holder.netPrice.visibility = View.VISIBLE
            holder.OrderQtyValue.visibility = View.VISIBLE
            holder.orderQtyProduct.visibility = View.VISIBLE
            holder.btnDeleteItem.visibility = View.VISIBLE
            var netPrice=netPrices.toFloat()
            holder.netPrice.setText("$" + "%.2f".format(netPrice))
        }
        else if (taxValue==1)
        {

            holder.taxableLevel.visibility = View.VISIBLE
            holder.taxableLevel.setText("Taxable")
        }
        else {
            holder.netPrice.visibility = View.GONE
            holder.taxableLevel.visibility = View.GONE
            holder.OrderQtyValue.visibility = View.GONE
            holder.btnDeleteItem.visibility = View.GONE
            holder.orderQtyProduct.visibility = View.GONE
        }

        var productStoct = ProductItem.getCStock()
        if (productStoct == "0") {
            holder.ProductStock.setText("Stock : " + productStoct.toString())
            holder.ProductStock.setTextColor(Color.parseColor("#DC3545"))
        } else {
            holder.ProductStock.setText("Stock : " + productStoct.toString())
            holder.ProductStock.setTextColor(Color.parseColor("#000000"))
        }
        var image=ProductItem.getImageUrl()
        if (image!=""&&image!=null)
        {
            holder.ProductImage.height.minus(30)
            Picasso.get().load(image).error(R.drawable.default_pic).into(holder.ProductImage);
        }
        else{
            holder.ProductImage.height.minus(30)
            holder.ProductImage.setImageResource(R.drawable.default_pic)
        }
        NetPrice= NetPrice+(ProductItem.getNetPrice()!!)
        if (ProductItem.getadded()!!>0){
            itemCounts++
        }
    }





    private fun finish() {
    }

    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {
        //                                             digitsBeforeZero  or       digitsBeforeZero + dot + digitsAfterZero
        private val pattern =
            Pattern.compile("(\\d{0,$digitsBeforeZero})|(\\d{0,$digitsBeforeZero}\\.\\d{0,$digitsAfterZero})")

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int,
        ): CharSequence? {
            return if (source.isEmpty()) {
                // When the source text is empty, we need to remove characters and check the result
                if (pattern.matcher(dest.removeRange(dstart, dend)).matches()) {
                    // No changes to source
                    null
                } else {
                    // Don't delete characters, return the old subsequence
                    dest.subSequence(dstart, dend)
                }
            } else {
                // Check the result
                if (pattern.matcher(dest.replaceRange(dstart, dend, source)).matches()) {
                    // No changes to source
                    null
                } else {
                    // Return nothing
                    ""
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return ProductItemList.size
    }

    override fun onClick(v: View?) {

    }


}










