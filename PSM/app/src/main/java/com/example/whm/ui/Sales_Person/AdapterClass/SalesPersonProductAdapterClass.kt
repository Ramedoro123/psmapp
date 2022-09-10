package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.SalesPersonProductModel
import com.squareup.picasso.Picasso

class SalesPersonProductAdapterClass(private val ProductItemList:List<SalesPersonProductModel>,
            var data: Context?):RecyclerView.Adapter<SalesPersonProductAdapterClass.ViewHolder>() {
                class ViewHolder(ItemView:View):RecyclerView.ViewHolder(ItemView){
                    var ProductName=itemView.findViewById<TextView>(R.id.productIdSalse)
                    var ProductdID=itemView.findViewById<TextView>(R.id.ProductdID)
                    var ProductPrice=itemView.findViewById<TextView>(R.id.ProductPrice)
                    var ProductStock=itemView.findViewById<TextView>(R.id.ProductStock)
                    var ProductImage=itemView.findViewById<ImageView>(R.id.ProductImage)
                    var cardView5=itemView.findViewById<CardView>(R.id.cardView5)
                }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.product_list,parent,false)

        //title.setText("hello")
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

        holder.cardView5.setOnClickListener(View.OnClickListener {
            var dilog= Dialog(it.context)
            dilog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dilog.setCancelable(false)
            dilog.setContentView(R.layout.add_to_cart_popupview)
            dilog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dilog.window!!.setGravity(Gravity.CENTER)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dilog.getWindow()!!.getAttributes())
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
          var SProductID=dilog.findViewById<TextView>(R.id.productIdSalse)
            var s_ProductName=dilog.findViewById<TextView>(R.id.text_ProductName)
            var stockProductS=dilog.findViewById<TextView>(R.id.stockProductS)
            var imageView13=dilog.findViewById<ImageView>(R.id.imageView13)
            SProductID.setText(ProductItem.getPId())
            s_ProductName.setText(ProductItem.getPName())
            stockProductS.setText("Stock : "+ProductItem.getCStock().toString())
            Picasso.get().load(ProductItem.getImageUrl()).error(R.drawable.default_pic).into(imageView13);
            var btnSearch=dilog.findViewById<Button>(R.id.btnSearch)

            var btnCancle=dilog.findViewById<TextView>(R.id.textView33)
            btnCancle.setOnClickListener(View.OnClickListener {
                dilog.dismiss()
            })
            dilog.show()
            dilog.getWindow()!!.setAttributes(lp);
        })

    }
    override fun getItemCount(): Int {
        var CustomerlistSize=ProductItemList.size.toString()
        val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(data)
        val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
        sharedLoadOrderPage.putString("ProductItemList", CustomerlistSize)
        sharedLoadOrderPage.apply()
//        if(ProductItemList.size!=0){
//            (data as AppCompatActivity)!!.supportActionBar!!.setWindowTitle("Add-On("+ProductItemList.size+")")
//        }else{
//            (data as AppCompatActivity)!!.supportActionBar!!.title = "Add-On("+ProductItemList.size+")"
//
//        }
        return ProductItemList.size
    }
}