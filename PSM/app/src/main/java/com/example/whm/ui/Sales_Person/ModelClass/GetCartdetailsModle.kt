package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

import kotlin.math.min

class getCartdetailsModle(uiDs: Int, uName:String, frees: Int, price: String, MinPric: String, isdefault: Int,dispValue:String,
disaValue:String) {

    private var uiDs:Int
    private var uName:String
    private var frees: Int
    private var price:String
    private var MinPric: String
    private var isdefault:Int
    private var dispValue:String
    private var disaValue:String
    init{
        this.uiDs=uiDs
        this.uName=uName
        this.frees=frees
        this.price=price
        this.MinPric=MinPric
        this.isdefault=isdefault
        this.dispValue=dispValue
        this.disaValue=disaValue
    }
    fun getuiDs(): Int? {
        return uiDs
    }
    fun getuName():String?{
        return uName
    }
    fun getfrees(): Int? {
        return frees
    }
    fun getprice():String?{
        return price
    }
    fun getMinPric(): String? {
        return MinPric
    }
    fun getisdefault(): Int? {
        return isdefault
    }
fun getdispValue():String?{
    return dispValue
}
fun getdisaValue():String?{
    return disaValue
}
    fun setdispValue(dispValue:String?){
        this.dispValue=dispValue!!

    }
    fun setdisaValue(disaValue:String?){
        this.disaValue=disaValue!!

    }
}