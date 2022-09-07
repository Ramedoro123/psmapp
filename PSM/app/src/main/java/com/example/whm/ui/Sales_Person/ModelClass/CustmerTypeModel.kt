package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class CustmerTypeModel(CustomerType:String,autoId:Int) {
    private var CustomerType:String
    private var autoId:Int
    init {
        this.CustomerType=CustomerType
        this.autoId=autoId
    }
    fun getCustomerType():String?{
        return CustomerType
    }fun getautoId():Int?{
        return autoId
    }
    fun setCustomerType(CustomerType:String?){
        this.CustomerType=CustomerType!!
    }
    fun setautoId(autoId:Int?){
        this.autoId=autoId!!
    }
}