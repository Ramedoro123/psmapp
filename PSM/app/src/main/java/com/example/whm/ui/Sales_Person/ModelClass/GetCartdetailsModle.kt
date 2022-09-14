package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

import kotlin.math.min

class getCartdetailsModle(uiDs: Int, uName:String, frees: Int, price: Double, minPrices: Int, isdefault: Int) {

    private var uiDs:Int
    private var uName:String
    private var frees: Int
    private var price:Double
    private var minPrices: Int
    private var isdefault:Int
    init{
        this.uiDs=uiDs
        this.uName=uName
        this.frees=frees
        this.price=price
        this.minPrices= minPrices
        this.isdefault=isdefault
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
    fun getprice():Double?{
        return price
    }
    fun getminPrices(): Int? {
        return minPrices
    }
    fun getisdefault(): Int? {
        return isdefault
    }


}