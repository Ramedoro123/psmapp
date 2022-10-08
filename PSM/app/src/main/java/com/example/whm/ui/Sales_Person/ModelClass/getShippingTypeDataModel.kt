package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class getShippingTypeDataModel(AutoID:Int, shippingType:String, EnabledTax:Int) {
    private var shippingType:String
    private var AutoID:Int
    private var EnabledTax:Int
    init {
        this.shippingType=shippingType
        this.AutoID=AutoID
        this.EnabledTax=EnabledTax
    }
    fun getshippingType():String?{
        return shippingType
    }
    fun getAutoID():Int?{
        return AutoID
    }
    fun setshippingType(CustomerType:String?){
        this.shippingType=shippingType!!
    }
    fun setAutoID(AutoID:Int?){
        this.AutoID=AutoID!!
    }

    fun getEnabledTax():Int?{
        return EnabledTax
    }
    fun setEnabledTax(EnabledTax:Int?){
        this.EnabledTax=EnabledTax!!
    }
}