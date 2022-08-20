package com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.ModelClass

class Product_DetailsModel(
    ProductID: Int,
    icount: Int,
    ProductName:String,
    UnitType: String,
    Loctaion: String,
    Stock: String,
    ShipingQty: Int,
    OrderQty: Int,
    ProductImage1: String
) {
   private var  ProductID: Int
   private var  icount: Int
   private var  ProductName:String=""
    private var   UnitType: String
   private var   Loctaion: String
   private var    Stock: String
   private var    ShipingQty: Int
   private var   OrderQty: Int
   private var   ProductImage1: String
 init {
     this.ProductID=ProductID
     this.icount=icount
     this.UnitType=UnitType
     this.Loctaion=Loctaion
     this.Stock=Stock
     this.ShipingQty=ShipingQty
     this.OrderQty=OrderQty
     this.ProductImage1=ProductImage1
 }
    fun getProductID(): Int? {
        return ProductID
    }
    fun setAId(AId:Int?){
        this.ProductID=AId!!
    }

}