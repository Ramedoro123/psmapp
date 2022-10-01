package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class CartListModleClass( PId:Int,PName:String,UnitType:String,QtyPerUnit:Int,UnitPrice:Int,ReqQty:Int,NetPrice:Int,Free:Int,Exchange:Int,Tax:Int,UnitAutoId:Int,ImgPath:String){

    private var PName:String
    private var PId:Int
    private var UnitPrice:Int
    private var NetPrice:Int
    private var UnitType:String
    private var QtyPerUnit:Int
    private var ReqQty:Int
    private var Free:Int
    private var Exchange:Int
    private var Tax:Int
    private var UnitAutoId:Int
    private var ImgPath:String
    init {
        this.PName=PName
        this.PId=PId
        this.UnitPrice=UnitPrice
        this.NetPrice=NetPrice
        this.UnitType=UnitType
        this.QtyPerUnit=QtyPerUnit
        this.ReqQty=ReqQty
        this.Free=Free
        this.Exchange=Exchange
        this.Tax=Tax
        this.UnitAutoId=UnitAutoId
        this.ImgPath=ImgPath
    }
    fun getPName():String?{
        return PName
    }fun getPId():Int?{
        return PId
    }
    fun getpUnitPrice():Int?{
        return UnitPrice
    }
    fun getNetPrice():Int?{
        return NetPrice
    }fun getUnitType():String?{
        return UnitType
    }fun getQtyPerUnit():Int?{
        return QtyPerUnit
    }fun getReqQty():Int?{
        return ReqQty
    }fun getFree():Int?{
        return Free
    }fun getExchange():Int?{
        return Exchange
    }
    fun getTax():Int?{
        return Tax
    }
   fun getUnitAutoId():Int?{
        return UnitAutoId
    }
    fun getImgPath():String?{
        return ImgPath
    }

    fun setPName(PName:String?){
        this.PName=PName!!
    }
    fun setUnitPrice(UnitPrice:Int?){
        this.UnitPrice=UnitPrice!!
    }
}
