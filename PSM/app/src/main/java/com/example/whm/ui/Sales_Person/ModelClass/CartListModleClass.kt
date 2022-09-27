package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class CartListModleClass( PName:String, PID:String, pPrice:String,TotalAmount:String){

    private var PName:String
    private var PID:String
    private var pPrice:String
    private var TotalAmount:String
    init {
        this.PName=PName
        this.PID=PID
        this.pPrice=pPrice
        this.TotalAmount=TotalAmount
    }
    fun getPName():String?{
        return PName
    }fun getPID():String?{
        return PID
    }
    fun getpPrice():String?{
        return pPrice
    }fun getTotalAmount():String?{
        return TotalAmount
    }
    fun setPName(PName:String?){
        this.PName=PName!!
    }
    fun setpPrice(pPrice:String?){
        this.pPrice=pPrice!!
    }
}
