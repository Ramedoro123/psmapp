package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class CartListModleClass( PName:String, PID:String, pPrice:Int,TotalAmount:Int){

    private var PName:String
    private var PID:String
    private var pPrice:Int
    private var TotalAmount:Int
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
    fun getpPrice():Int?{
        return pPrice
    }fun getTotalAmount():Int?{
        return TotalAmount
    }
    fun setPName(PName:String?){
        this.PName=PName!!
    }
    fun setpPrice(pPrice:Int?){
        this.pPrice=pPrice!!
    }
}
