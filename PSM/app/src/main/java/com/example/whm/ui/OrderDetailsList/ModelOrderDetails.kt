package com.example.myapplication.com.example.whm.ui.OrderDetailsList

class ModelOrderDetails(PId:Int,PName:String,UnitType:String,QtyPerUnit:Int,RequiredQty:Int,QtyShip:String,StatusCode:Int,IsExchange:Int,isFreeItem:Int,ImageUrl:String) {
    private var PId:Int
    private var PName: String
    private var UnitType: String
    private var QtyPerUnit: Int
    private var RequiredQty: Int
    private var QtyShip: String
    private var StatusCode: Int
    private var IsExchange: Int
    private var isFreeItem: Int


    private var ImageUrl:String
    init{
        this.PId=PId
        this.PName=PName
        this.UnitType=UnitType
        this.QtyPerUnit= QtyPerUnit
        this.RequiredQty= RequiredQty
        this.QtyShip= QtyShip
        this.StatusCode= StatusCode
        this.IsExchange= IsExchange
        this.isFreeItem= isFreeItem
        this.ImageUrl=ImageUrl
    }
    fun getPId(): Int? {
        return PId
    }
    fun setPId(PId:Int?){
        this.PId=PId!!
    }
    fun getPName(): String? {
        return PName
    }
    fun setPName(PName: String?) {
        this.PName = PName!!
    }
    fun getUnitTyper():String?{
        return UnitType
    }
    fun setUnitType(UnitType:String?){
        this.UnitType = UnitType!!
    }
    fun getQtyPerUnit():Int?{

        return QtyPerUnit
    }
    fun setsalesPerson(QtyPerUnit:Int?){

        this.QtyPerUnit = QtyPerUnit!!
    }
    fun getRequiredQty():Int?{

        return RequiredQty
    }
    fun setRequiredQty(RequiredQtyt:Int?){

        this.RequiredQty = RequiredQty!!
    }
    fun getQtyShip():String?{

        return QtyShip
    }
    fun setQtyShip(QtyShip:String?){

        this.QtyShip = QtyShip!!
    }

    fun getStatusCode():Int?{

        return StatusCode
    }
    fun setStatusCode(StatusCode:Int?){

        this.StatusCode = StatusCode!!
    }
    fun getIsExchange():Int?{

        return IsExchange
    }
    fun setIsExchange(IsExchange:Int?){

        this.IsExchange = IsExchange!!
    }
    fun getisFreeItem():Int?{

        return isFreeItem
    }
    fun setisFreeItem(isFreeItem:Int?){

        this.isFreeItem = isFreeItem!!
    }
    fun getImageUrl():String?{

        return ImageUrl
    }
    fun setImageUrl(ImageUrl:String?){

        this.ImageUrl = ImageUrl!!
    }
}