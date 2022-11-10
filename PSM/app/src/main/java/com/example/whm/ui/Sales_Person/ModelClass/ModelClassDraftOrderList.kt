package com.example.myapplication.com.example.whm.ui.Sales_Person.ModelClass

class ModelClassDraftOrderList(  DraftAutoId: Int,orderDate: String, customerName: String, status: String,noOfItems: String, colorCode: String, grandTotal: String,CustomerId: String,CustomerAutoId: String,)
{
    private var DraftAutoId:Int
    private var orderDate:String
    private var customerName: String
    private var status: String
    private var noOfItems: String
    private var colorCode: String
    private var grandTotal: String
    private var CustomerId: String
    private var CustomerAutoId: String
    init {
        this.DraftAutoId=DraftAutoId
        this.orderDate=orderDate
        this.customerName=customerName
        this.status=status
        this.noOfItems=noOfItems
        this.colorCode=colorCode
        this.grandTotal=grandTotal
        this.CustomerId=CustomerId
        this.CustomerAutoId=CustomerAutoId
    }
    fun getDraftAutoId():Int?{
        return DraftAutoId
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
    fun getcolorCode():String?{
        return colorCode
    }
    fun getnoOfItems():String?{
        return noOfItems
    }
    fun getgrandTotal():String?{
        return grandTotal
    }
    fun getCustomerId():String?{
        return CustomerId
    }
    fun getCustomerAutoId():String?{
        return CustomerAutoId
    }
}