package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class ModelClassCustomerList(CN:String,CId:String) {
    private var CN:String
    private var CId: String
    init {
        this.CN=CN
        this.CId=CId
    }
    fun getCN(): String? {
        return CN
    }
    fun setCN(CN:String?){
        this.CN=CN!!
    }
    fun getCId(): String? {
        return CId
    }
    fun setCId(CId: String?) {
        this.CId = CId!!
    }


}