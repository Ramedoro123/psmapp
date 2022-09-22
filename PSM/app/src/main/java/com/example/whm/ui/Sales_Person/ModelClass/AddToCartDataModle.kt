package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class AddToCartDataModle(DraftAutoId:Int,Total:String,NofItem:String) {
    private var DraftAutoId:Int
    private var Total:String
    private var NofItem: String
    init {
        this.DraftAutoId=DraftAutoId
        this.Total=Total
        this.NofItem=NofItem
    }
    fun getDraftAutoId(): Int? {
        return DraftAutoId
    } fun getTotal(): String? {
        return Total
    } fun getNofItem(): String? {
        return NofItem
    }

}