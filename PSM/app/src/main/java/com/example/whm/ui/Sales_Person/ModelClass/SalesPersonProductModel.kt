package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class SalesPersonProductModel(PId:String,PName:String,ImageUrl:String,CStock:Int,BP:Float,UnitType:String) {
    private var PId:String
    private var PName:String
    private var ImageUrl:String
    private var CStock:Int
    private var BP:Float
    private var UnitType:String
    init {
        this.PId=PId
        this.PName=PName
        this.ImageUrl=ImageUrl
        this.CStock=CStock
        this.BP=BP
        this.UnitType=UnitType
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
    fun getCStock():Int?{
        return CStock
    }
    fun setCStock(CStock:Int?){
        this.CStock=CStock!!
    }
    fun getBP():Float?{
        return BP
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

}