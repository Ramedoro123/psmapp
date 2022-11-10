package com.example.myapplication.com.example.whm.ui.Sales_Person.AdapterClass

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass.ModelClassDraftOrderList
import de.hdodenhof.circleimageview.CircleImageView
class DraftOrderListAdapter(private val OrderListData: ArrayList<ModelClassDraftOrderList>, var Listdata: Context?,
                            private val listener: OnItemClickListeners,
): RecyclerView.Adapter<DraftOrderListAdapter.ViewHolder>()
{
    public interface OnItemClickListeners {
        fun OnItemsClick(position: Int)
        fun OnDeleteClicks(position: Int)
    }
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView), View.OnClickListener {
        var customername: TextView = itemView.findViewById(R.id.customername)
        var imageFarword: CircleImageView = itemView.findViewById(R.id.imageFarword)
        var orderType: TextView = itemView.findViewById(R.id.orderType)
        var orderDate: TextView = itemView.findViewById(R.id.orderDate)
        var itemsAndAmount: TextView = itemView.findViewById(R.id.itemsAndAmount)
        var orderNo: TextView = itemView.findViewById(R.id.orderNoAndDate)
        var colorSide: TextView = itemView.findViewById(R.id.colorSide)

        init {

            itemView.setOnClickListener(View.OnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.OnItemsClick(position)
                }
            })

        }

        override fun onClick(v: View?) {

        }
    }
fun deleteItem(i: Int){
    OrderListData.removeAt(i)
    notifyDataSetChanged()
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.salseperson_orderlist_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var orderListdata=OrderListData[position]
        holder.customername.text=orderListdata.getcustomerName()
        var colorCode=orderListdata.getcolorCode()
        var orderdate=orderListdata.getorderDate()
        var NoOfItems=orderListdata.getnoOfItems()
        var grandTotal=orderListdata.getgrandTotal()
        var grandTotals:Float=grandTotal!!.toFloat()
        holder.itemsAndAmount.visibility=View.GONE
        holder.orderNo.visibility=View.VISIBLE
        holder.orderNo.setText(NoOfItems+" Item "+"$%.2f".format(grandTotals))
        holder.orderDate.setText(orderdate)
        Log.e("colorCode",colorCode.toString())
            holder.colorSide.setBackgroundColor(Color.parseColor(colorCode.toString()))
            holder.imageFarword.setCircleBackgroundColor(Color.parseColor(colorCode.toString()))
            holder.orderType.setTextColor(Color.parseColor(colorCode.toString()))
            holder.orderType.text=orderListdata.getstatus()
    }

    override fun getItemCount(): Int {
        return OrderListData.size
    }
}