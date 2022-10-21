package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class ModelClassOrderCustomerList(
    autoId: Int,
    orderNo: String,
    orderDate: String,
    customerName: String,
    status: String,
    statusCode: String,
    grandTotal: String,
    salesPerson: String,
    shippingType: String,
    shipId: String,
    noOfItems: String,
    creditMemo: String,
    colorCode: String
) {
 private var autoId:Int
 private var orderNo:String
 private var orderDate:String
 private var customerName: String
 private var status: String
 private var statusCode: String
 private var grandTotal: String
 private var salesPerson: String
 private var shippingType: String
 private var shipId: String
 private var noOfItems: String
 private var creditMemo: String
 private var colorCode: String
init {

    this.autoId=autoId
    this.orderNo=orderNo
    this.orderDate=orderDate
    this.customerName=customerName
    this.status=status
    this.statusCode=statusCode
    this.grandTotal=grandTotal
    this.salesPerson=salesPerson
    this.shippingType=shippingType
    this.shipId=shipId
    this.noOfItems=noOfItems
    this.colorCode=colorCode
    this.creditMemo=creditMemo

}
    fun getautoId():Int?{
        return autoId
    }
fun getorderNo():String?{
    return orderNo
}
fun getorderDate():String?{
    return orderDate
}
fun getcustomerName():String?{
    return customerName
}
fun getstatus():String?{
    return status
}
fun getstatusCode():String?{
    return statusCode
}
fun getgrandTotal():String?{
    return grandTotal
}
fun getsalesPerson():String?{
    return salesPerson
}
fun getshippingType():String?{
    return shippingType
}
fun getshipId():String?{
    return shipId
}
fun getnoOfItems():String?{
    return noOfItems
}
fun getcreditMemo():String?{
    return creditMemo
}
fun getcolorCode():String?{
    return colorCode
}
}