package com.example.myapplication.com.example.whm.ui.UpdateLocation

class UpdateLocationModel(AId:Int,OrderNo:String,ODate:String,CName:String,PackedBoxes:Int,ColorCode:String,OL:String,Status:String) {
    private var AId:Int
    private var OrderNo:String
    private var ODate:String
    private var CName:String
    private var PackedBoxes:Int
    private var ColorCode:String
    private var OL:String
    private var Status:String

    init {
        this.AId=AId
        this.OrderNo=OrderNo
        this.ODate=ODate
        this.CName=CName
        this.PackedBoxes=PackedBoxes
        this.ColorCode=ColorCode
        this.OL=OL
        this.Status=Status
    }
    fun getAId(): Int? {
        return AId
    }
    fun setAId(AId:Int?){
        this.AId=AId!!
    }
    fun getOrderNo(): String? {
        return OrderNo
    }
    fun setOrderNo(OrderNo: String?) {
        this.OrderNo = OrderNo!!
    }
    fun getCName():String?{
        return CName
    }
    fun setCName(CName:String?){
        this.CName = CName!!
    }
    fun getODate():String?{
        return ODate
    }
    fun setODate(ODate:String?){
        this.ODate = ODate!!
    }

    fun getPackedBoxes(): Int? {
        return PackedBoxes
    }
    fun setPackedBoxes(PackedBoxes:Int?){
        this.PackedBoxes=PackedBoxes!!
    }
    fun getColorCode():String?{
        return ColorCode

    } fun getOL():String?{
        return OL
    }
    fun setOL(OL:String?){
        this.OL = OL!!
    }
    fun getStatus():String?{
        return Status
    }
    fun setStatus(Status:String?){
        this.Status = Status!!
    }
}