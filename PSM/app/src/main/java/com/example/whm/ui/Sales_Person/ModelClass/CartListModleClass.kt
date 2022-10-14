package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class CartListModleClass(
    PId:String,
    PName:String,
    UnitType:String,
    QtyPerUnit:Int,
    UnitPrice:Float,
    ReqQty:Int,
    NetPrice:Float,
    Free:Int,
    Exchange:Int,
    tax:Int,
    UnitAutoId:Int,
    ImgPath:String,
    Total: String,
    NofItem:String,
    draftAutoId: Int,
    OQty:Int){

    private var PName:String
    private var PId:String
    private var UnitPrice:Float
    private var NetPrice:Float
    private var UnitType:String
    private var QtyPerUnit:Int
    private var ReqQty:Int
    private var Free:Int
    private var Exchange:Int
    private var tax:Int
    private var UnitAutoId:Int
    private var ImgPath:String
    private var Total:String
    private var NofItem:String
    private var draftAutoId:Int
    private var  OQty:Int
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
        this.tax=tax
        this.UnitAutoId=UnitAutoId
        this.ImgPath=ImgPath
        this.Total=Total
        this.NofItem=NofItem
        this.draftAutoId=draftAutoId
        this.OQty=OQty
    }
    fun getPName():String?{
        return PName
    }

    fun getPId():String?{
        return PId
    }

    fun getUnitPrice():Float?{
        return UnitPrice
    }
    fun getNetPrice():Float?{
        return NetPrice
    }
    fun setNetPrice(NetPrice:Float?){
        this.NetPrice=NetPrice!!
    }
    fun getUnitType():String?{
        return UnitType
    }
    fun setUnitType(UnitType:String?){
        this.UnitType=UnitType!!
    }
    fun getQtyPerUnit():Int?{
        return QtyPerUnit
    }
    fun setQtyPerUnit(QtyPerUnit:Int?){
        this.QtyPerUnit=QtyPerUnit!!
    }
    fun getReqQty():Int?{
        return ReqQty
    }
    fun setReqQty(ReqQty:Int?){
        this.ReqQty=ReqQty!!
    }
    fun getFree():Int?{
        return Free
    }
    fun setFree(Free:Int?){
        this.Free=Free!!
    }
    fun getExchange():Int?{
        return Exchange
    }
    fun setExchange(Exchange:Int?){
        this.Exchange=Exchange!!
    }
    fun gettax():Int?{
        return tax
    }
    fun setTax(Tax:Int?){
        this.tax=tax!!
    }
   fun getUnitAutoId():Int?{
        return UnitAutoId
    }
    fun setUnitAutoId(UnitAutoId:Int?){
        this.UnitAutoId=UnitAutoId!!
    }

    fun getImgPath():String?{
        return ImgPath
    }

    fun setImgPath(ImgPath:String?){
        this.ImgPath=ImgPath!!
    }
    fun setPName(PName:String?){
        this.PName=PName!!
    }
    fun setUnitPrice(UnitPrice:Float?){
        this.UnitPrice=UnitPrice!!
    }

    fun getTotal():String?{
        return Total
    }
    fun setTotal(Total:String?){
        this.Total=Total!!
    }
    fun getNofItem():String?{
        return NofItem
    }
    fun setNofItem(NofItem:String?){
        this.NofItem=NofItem!!
    }
    fun getdraftAutoId():Int?{
        return draftAutoId
    }
    fun draftAutoId(draftAutoId:Int?){
        this.draftAutoId=draftAutoId!!
    }
    fun getOQty():Int?{
        return OQty
    }
    fun setOQty(OQty:Int?){
        this.OQty=OQty!!
    }
}
