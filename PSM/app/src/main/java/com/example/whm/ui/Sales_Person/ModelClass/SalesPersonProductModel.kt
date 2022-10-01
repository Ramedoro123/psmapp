package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class SalesPersonProductModel(PId:String,PName:String,ImageUrl:String,CStock:String,BP:Float,UnitType:String,OQty:Int,
                              UnitPrice:String,Total:String,NofItem:String,draftAutoId:Int) {
    private var PId:String
    private var PName:String
    private var ImageUrl:String
    private var CStock:String
    private var BP:Float
    private var OQty:Int
    private var draftAutoId:Int
    private var UnitType:String
    private var UnitPrice:String
    private var Total:String
    private var NofItem:String
    init {
        this.PId=PId
        this.PName=PName
        this.ImageUrl=ImageUrl
        this.CStock=CStock
        this.BP=BP
        this.OQty=OQty
        this.draftAutoId=draftAutoId
        this.UnitType=UnitType
        this.UnitPrice=UnitPrice
        this.Total=Total
        this.NofItem=NofItem
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
    } fun getOQty():Int?{
        return OQty
    } fun getdraftAutoId():Int?{
        return draftAutoId
    }
    fun setBP(BP:Float?){
        this.BP=BP!!
    }
    fun getUnitType():String?{
        return UnitType
    }  fun getUnitPrice():String?{
        return UnitPrice
    }  fun getTotal():String?{
        return Total
    }  fun getNofItem():String?{
        return NofItem
    }
    fun setUnitType(UnitType:String?){
        this.UnitType=UnitType!!
    }

    fun removeAt(position: Int) {

    }

}