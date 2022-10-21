package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class OrderItemModelClass(  productId: Int,
                              productName: String,
                              unitType: String,
                              unitPrice: String,
                              netPrice: String,
                              imageUrl: String,
                              requiredQty: String)
{

       private var productId: Int
       private var productName: String
       private var unitType: String
       private var unitPrice: String
       private var netPrice: String
       private var imageUrl: String
       private var requiredQty: String
       init {
           this.productId=productId
           this.productName=productName
           this.unitType=unitType
           this.unitPrice=unitPrice
           this.netPrice=netPrice
           this.imageUrl=imageUrl
           this.requiredQty=requiredQty
       }
    fun getproductId():Int?{
        return productId
    }
    fun getproductName():String?{
        return productName
    }
    fun getunitType():String?{
        return unitType
    }
    fun getunitPrice():String?{
        return unitPrice
    }
    fun getnetPrice():String?{
        return netPrice
    }
    fun getimageUrl():String?{
        return imageUrl
    }
    fun getrequiredQty():String?{
        return requiredQty
    }
}