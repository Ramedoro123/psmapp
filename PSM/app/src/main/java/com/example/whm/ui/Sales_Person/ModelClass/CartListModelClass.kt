package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class CartListModelClass(
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
    OQty:Int
)


{
    private var PId:String
    private var PName:String
    private var ImgPath:String
    private var UnitType:String
    private var QtyPerUnit:Int
    private var UnitPrice:Float
    private var ReqQty:Int
    private var NetPrice:Float
    private var Free:Int
    private var Exchange:Int
    private var tax:Int
    private var UnitAutoId:Int
    private var Total: String
    private var NofItem:String
    private var draftAutoId: Int
    private var OQty:Int

    init {
        this.PId=PId
        this.PName=PName
        this.ImgPath=ImgPath
        this.UnitType=UnitType
        this.QtyPerUnit=QtyPerUnit
        this.UnitPrice=UnitPrice
        this.ReqQty=ReqQty
        this.NetPrice=NetPrice
        this.Free=Free
        this.Exchange=Exchange
        this.tax=tax
        this.UnitAutoId=UnitAutoId
       this.Total=Total
       this.NofItem=NofItem
       this.draftAutoId=draftAutoId
       this.OQty=OQty


    }
    fun getPId():String?{
        return PId
    }
    fun setPId(PId:String?){
        this.PId=PId!!
    }
    fun getPName():String?{
        return PName
    }
    fun setPName(PName:String?){
        this.PName=PName!!
    }
    fun getImgPath():String?{
        return ImgPath
    }
    fun setImgPath(ImageUrl:String?){
        this.ImgPath=ImageUrl!!
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
    fun getUnitPrice():Float?{
        return UnitPrice
    }
    fun setUnitPrice(UnitPrice:Float?){
        this.UnitPrice=UnitPrice!!
    }
    fun getReqQty():Int?{
        return ReqQty
    }
    fun setReqQty(ReqQty:Int?){
        this.ReqQty=ReqQty!!
    }
    fun getNetPrice():Float?{
        return NetPrice
    }
    fun setNetPrice(NetPrice:Float?){
        this.NetPrice=NetPrice!!
    }


    fun getFree():Int?{
        return Free
    }
    fun setFree(Free:Int?){
        this.Free=Free!!
    } fun getExchange():Int?{
        return Exchange
    }
    fun setExchange(Exchange:Int?){
        this.Exchange=Exchange!!
    }
    fun gettax():Int?{
        return tax
    }
    fun settax(tax:Int?){
        this.tax=tax!!
    }
    fun getUnitAutoId():Int?{
        return UnitAutoId
    }
    fun setUnitAutoId(UnitAutoId:Int?){
        this.UnitAutoId=UnitAutoId!!
    }

    fun getdraftAutoId():Int?{
        return draftAutoId
    }
    fun setdraftAutoId(draftAutoId:Int?){
        this.draftAutoId=draftAutoId!!
    }
    fun getOQty():Int?{
        return OQty
    }
    fun setOQty(OQty:Int?){
        this.draftAutoId=OQty!!
    }
    fun getTotal():String?{
        return Total
    }
    fun setTotal(Total:String?){
        this.Total=Total!!
    }fun getNofItem():String?{
        return NofItem
    }
    fun setNofItem(NofItem:String?){
        this.NofItem=NofItem!!
    }

}