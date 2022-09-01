package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class ModelClassCustomerList(AI:Int,CN:String,CId:String,CT:String,DueBalance:String) {
    private var AI:Int
    private var CN:String
    private var CId: String
    private var CT: String
    private var DueBalance: String
    init {
        this.AI=AI
        this.CN=CN
        this.CId=CId
        this.CT=CT
        this.DueBalance=DueBalance
    }
    fun getAI(): Int? {
        return AI
    } fun getCN(): String? {
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
    fun getCT(): String? {
        return CT
    }fun getDueBalance(): String? {
        return DueBalance
    }


}