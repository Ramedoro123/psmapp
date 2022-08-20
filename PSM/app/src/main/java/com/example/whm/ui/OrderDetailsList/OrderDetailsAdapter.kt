package com.example.myapplication.com.example.whm.ui.OrderDetailsList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.whm.ui.OrderDetailsList.OrderDetailsActicity
import com.squareup.picasso.Picasso


class OrderDetailsAdapter(private val mList: List<ModelOrderDetails>, orderDetailsActicity: OrderDetailsActicity) : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
           val view = LayoutInflater.from(parent.context).inflate(R.layout.order_details_list, parent, false)
           return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ModelOrder= mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.ProductId.text= ModelOrder.getPId().toString()
        holder.Product_Name.text=ModelOrder.getPName()
        holder.Unit_details.text=ModelOrder.getUnitTyper()
        holder.StatusCode=ModelOrder.getStatusCode()
        holder.changestatusfreeandExchange.visibility=View.GONE
        holder.YesFreeAndExchange.visibility=View.GONE

       var isFree=ModelOrder.getisFreeItem()
       var isExchange=ModelOrder.getIsExchange()
        //Picasso.get().load(ModelOrder.getImageUrl()).into(holder.imageOrderDetails);
            Picasso.get().load(ModelOrder.getImageUrl()).error(R.drawable.default_pic).into(holder.imageOrderDetails);
      /// System.out.println(holder.Product_Name.text.toString())
       // modelAssetLists.get(position).getAsset_image())
        val shape = GradientDrawable()
        shape.cornerRadius = 8f
        if (isFree==1)
        {
            shape.setColor(Color.parseColor("#079320"));
            holder.changestatusfreeandExchange.visibility=View.VISIBLE
            holder.YesFreeAndExchange.visibility=View.VISIBLE
            holder.changestatusfreeandExchange.setText("Free")
            //holder.changestatusfreeandExchange.setBackgroundColor(Color.parseColor("#079320"))
            holder.changestatusfreeandExchange.setWidth(80);
            holder.changestatusfreeandExchange.setBackground(shape)
        }else if (isExchange==1){
            shape.setColor(Color.parseColor("#710193"));
            holder.changestatusfreeandExchange.visibility=View.VISIBLE
            holder.YesFreeAndExchange.visibility=View.VISIBLE
            holder.changestatusfreeandExchange.setText("Exchange")
            //holder.changestatusfreeandExchange.setBackgroundColor(Color.parseColor("#710193"))
            holder.changestatusfreeandExchange.setWidth(130)
            holder.changestatusfreeandExchange.setBackground(shape)
        }else{
            holder.changestatusfreeandExchange.visibility=View.GONE
            holder.YesFreeAndExchange.visibility=View.GONE
        }
           if (holder.StatusCode==9)
           {
               holder.Quantity_Details.text=ModelOrder.getQtyShip().toString()+" Out of "+ModelOrder.getRequiredQty()
           }else if (holder.StatusCode==2)
           {
               holder.Quantity_Details.text=ModelOrder.getQtyShip().toString()+" Out of "+ModelOrder.getRequiredQty()
           }else{
               holder.Quantity_Details.text=ModelOrder.getQtyShip().toString()+" Out of "+ModelOrder.getRequiredQty()
           }
        // sets the text to the textview from our itemHolder class
        
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ProductId: TextView = itemView.findViewById(R.id.Product_ID)
        val Product_Name: TextView = itemView.findViewById(R.id.Product_Name)
        val Unit_details: TextView = itemView.findViewById(R.id.Unit_details)
        val Quantity_Details: TextView = itemView.findViewById(R.id.Quantity_Details)
        val imageOrderDetails:ImageView =itemView.findViewById(R.id.imageOrderDetails)
        val changestatusfreeandExchange:TextView =itemView.findViewById(R.id.changestatusfreeandExchange)
        val YesFreeAndExchange:TextView =itemView.findViewById(R.id.YesFreeAndExchange)
        var RequiredQty:Int?=null
        var QtyShip:Int?=null
        var StatusCode:Int?=null
    }
}





