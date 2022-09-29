package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class AddToCartDataModle(DraftAutoId:Int,Total:String,NofItem:String,OQty:Int,UnitPrice:String,UnitType:String) {
    private var DraftAutoId:Int
    private var OQty:Int
    private var Total:String
    private var NofItem: String
    private var UnitPrice: String
    private var UnitType: String
    init {
        this.DraftAutoId=DraftAutoId
        this.OQty=OQty
        this.Total=Total
        this.NofItem=NofItem
        this.UnitPrice=UnitPrice
        this.UnitType=UnitType
    }
    fun getDraftAutoId(): Int? {
        return DraftAutoId
    } fun getTotal(): String? {
        return Total
    } fun getNofItem(): String? {
        return NofItem
    } fun getOQty(): Int? {
        return OQty
    } fun getUnitPrice(): String? {
        return UnitPrice
    } fun getUnitType(): String? {
        return UnitType
    }

}