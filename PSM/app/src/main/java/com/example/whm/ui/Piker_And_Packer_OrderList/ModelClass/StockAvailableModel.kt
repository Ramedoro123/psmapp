package com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.ModelClass

class StockAvailableModel(ProductId:String,ProductName:String,AvilQty:String,QtyShip:String,RequiredQty:String) {
    private var ProductId:String
    private var ProductName:String
    private var AvilQty:String
    private var QtyShip:String
    private var RequiredQty:String
    init {
        this.ProductId=ProductId
        this.ProductName=ProductName
        this.AvilQty=AvilQty
        this.QtyShip=QtyShip
        this.RequiredQty=RequiredQty
    }
    fun getProductId(): String? {
        return ProductId
    }
    fun setProductId(ProductId:String?){
        this.ProductId=ProductId!!
    }
    fun getProductName(): String? {
        return ProductName
    }
    fun setProductName(ProductName:String?){
        this.ProductName=ProductName!!
    }
    fun getAvilQty(): String? {
        return AvilQty
    }
    fun setAvilQty(AvilQty:String?){
        this.AvilQty=AvilQty!!
    }
    fun getQtyShip(): String? {
        return QtyShip
    }
    fun setQtyShip(QtyShip:String?){
        this.QtyShip=QtyShip!!
    }fun getRequiredQty(): String? {
        return RequiredQty
    }
    fun setRequiredQty(RequiredQty:String?){
        this.RequiredQty=RequiredQty!!
    }
}