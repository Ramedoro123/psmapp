package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class ModelClassCustomerList(AI:Int,CN:String,CId:String,CT:String,DueBalances:Float) {
    private var AI:Int
    private var CN:String
    private var CId: String
    private var CT: String
    private var DueBalances: Float
    init {
        this.AI=AI
        this.CN=CN
        this.CId=CId
        this.CT=CT
        this.DueBalances=DueBalances
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
    }fun getDueBalances(): Float? {
        return DueBalances
    }


}