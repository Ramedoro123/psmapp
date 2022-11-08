package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class ModelClassCustomerList(AI:Int,CN:String,CId:String,CT:String,DueBalances:Float,SAdd:String,BAdd:String,CPN:String,Email:String
                             ,C1:String,MN:String,SCA:String,PLN:String,TD:String,ctype:String,OnRoute:String,CD:String) {
    private var AI:Int
    private var CN:String
    private var CId: String
    private var CT: String
    private var DueBalances: Float
    private var SAdd:String
    private var BAdd:String
    private var CPN:String
    private var Email:String
    private var C1:String
    private var MN:String
    private var SCA:String
    private var PLN:String
    private var TD:String
    private var ctype:String
    private var OnRoute:String
    private var CD:String

    init {
        this.AI=AI
        this.CN=CN
        this.CId=CId
        this.CT=CT
        this.DueBalances=DueBalances
        this.SAdd=SAdd
        this.BAdd=BAdd
        this.CPN=CPN
        this.Email=Email
        this.C1=C1
        this.MN=MN
        this.SCA=SCA
        this.PLN=PLN
        this.TD=TD
        this.ctype=ctype
        this.OnRoute=OnRoute
        this.CD=CD
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

    fun getSAdd():String?{
        return SAdd
    }
    fun getBAdd():String?{
        return BAdd
    }
    fun getCPN():String?{
        return CPN
    }
    fun getEmail():String?{
        return Email
    }
    fun getC1():String?{
        return C1
    }
    fun getMN():String?{
        return MN
    }
    fun getSCA():String?{
        return SCA
    }
    fun getPLN():String?{
        return PLN
    }
    fun getTD():String{
        return TD
    }
    fun getctype():String{
        return ctype
    }
    fun getOnRoute():String{
        return OnRoute
    }
    fun getCD():String{
        return CD
    }

}