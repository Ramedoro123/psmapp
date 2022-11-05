package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class SalesPersonProductModel(
    PId:String,
    PName:String,
    ImageUrl:String,
    CStock:String,
    BP:Float,
    UnitType:String,
    NetPrice:Float,
    IsTaxable:Int,
    ReqQty:Int,
    OUnitType:Int,
    UnitPrices:Float,
    added:Int,
    OQty: Int,
    Tax:Int,
    UnitPrice: Float,
    Total:String,
    NofItem: String,
    draftAutoID:Int) {
    private var PId:String
    private var PName:String
    private var ImageUrl:String
    private var CStock:String
    private var BP:Float
    private var OQty:Int
    private var Tax:Int
    private var draftAutoID:Int
    private var UnitType:String
    private var NetPrice:Float
    private var added:Int
    private var IsTaxable:Int
    private var ReqQty:Int
    private var OUnitType:Int
    private var UnitPrices:Float
    private var UnitPrice:Float
    private var Total:String
    private var NofItem:String
    init {
        this.PId=PId
        this.PName=PName
        this.ImageUrl=ImageUrl
        this.CStock=CStock
        this.BP=BP
        this.OQty=OQty
        this.Tax=Tax
        this.draftAutoID=draftAutoID
        this.UnitType=UnitType
        this.NetPrice=NetPrice
        this.added=added
        this.IsTaxable=IsTaxable
        this.ReqQty=ReqQty
        this.OUnitType=OUnitType
        this.UnitPrices=UnitPrices
        this.UnitPrice=UnitPrice
        this.Total=Total
        this.NofItem= NofItem
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
    fun getImageUrl():String?{
        return ImageUrl
    }
    fun setImageUrl(ImageUrl:String?){
        this.ImageUrl=ImageUrl!!
    }
    fun getCStock():String?{
        return CStock
    }
    fun setCStock(CStock:String?){
        this.CStock=CStock!!
    }
    fun getBP():Float?{
        return BP
    }
    fun getOQty():Int?{
        return OQty
    }
    fun getTax():Int?{
        return Tax
    }
    fun getadded():Int?{
        return added
    }
    fun setadded(added:Int?){
        this.added=added!!
    }
    fun setOQty(OQty:Int?){
        this.OQty=OQty!!
    }
    fun setTax(Tax:Int?){
        this.Tax=Tax!!
    }
    fun getdraftAutoID():Int?{
        return draftAutoID
    }
    fun setdraftAutoID(draftAutoId:Int?){
        this.draftAutoID=draftAutoID!!
    }
    fun setBP(BP:Float?){
        this.BP=BP!!
    }
    fun getUnitType():String?{
        return UnitType
    }
    fun setUnitType(UnitType:String?){
        this.UnitType=UnitType!!
    }
    fun getUnitPrice():Float?{
        return UnitPrice
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
fun getNetPrice():Float?{
    return NetPrice
}
fun getIsTaxable():Int{
    return IsTaxable

}
fun setIsTaxable(IsTaxable:Int?){
    this.IsTaxable=IsTaxable!!
}
fun getReqQty():Int?{
    return ReqQty
}
 fun getOUnitType():Int?{
    return OUnitType
}

fun getUnitPrices():Float?{
    return UnitPrices
}


    fun setNetPrice(NetPrice:Float?){
        this.NetPrice= NetPrice!!
    }
    fun setReqQty(ReqQty:Int?){
        this.ReqQty=ReqQty!!
    }

    fun setUnitPrices(UnitPrices:Float?){
        this.UnitPrices=UnitPrices!!
    }

    fun removeAt(position: Int) {
    return
    }


}