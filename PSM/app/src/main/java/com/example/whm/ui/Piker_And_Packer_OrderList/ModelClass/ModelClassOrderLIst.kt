package com.example.myapplication.com.example.whm.ui.Piker_And_Packer_OrderList.ModelClass

class ModelClassOrderLIst(
    AId:Int,
    OrderNo: String,
    Customer: String,
    salesPerson: String,
    ShippingType: String,
    Products: String,
    Status: String,
    OrderDate: String,
    ColorCode:String,
    StatusAutoId:Int,
    TotalNoOfItem:Int,
    PrintURL:String,
    ResumeO:Int
) {
    private var AId:Int
    private var OrderNo: String
    private var Customer: String
    private var salesPerson: String
    private var ShippingType: String
    private var Products: String
    private var Status: String
    private var OrderDate: String
    private var ColorCode:String
    private var StatusAutoId:Int
    private var TotalNoOfItem:Int
    private var PrintURL:String
    private var ResumeO:Int
    init {
        this.AId=AId
        this.OrderNo = OrderNo
        this.Customer = Customer
        this.salesPerson = salesPerson
        this.ShippingType= ShippingType
        this.Products= Products
        this.Status= Status
        this.OrderDate= OrderDate
        this.ColorCode=ColorCode
        this.StatusAutoId=StatusAutoId
        this.TotalNoOfItem=TotalNoOfItem
        this.PrintURL=PrintURL
        this.ResumeO=ResumeO
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
    fun getCustomer():String?{
        return Customer
    }
    fun setCustomer(Customer:String?){
        this.Customer = Customer!!
    }
    fun getsalesPerson():String?{

        return salesPerson
    }
    fun setsalesPerson(salesPerson:String?){

        this.salesPerson = salesPerson!!
    }
    fun getShippingType():String?{
        return ShippingType
    }
    fun setShippingType(ShippingType:String?){
        this.ShippingType = ShippingType!!
    }
    fun getProducts():String?{
        return Products
    }
    fun setProducts(Products:String?){
        this.Products = Products!!
    }
    fun getStatus():String?{
        return Status
    }
    fun setStatus(Status:String?){
        this.Status=Status!!
    }
    fun getOrderDate():String?{
        return OrderDate
    }
    fun setOrderDate(OrderDate:String?){
        this.OrderDate = OrderDate!!
    }
    fun getColorCode():String?{
        return ColorCode
    }
    fun setColorCode(ColorCode:String?){
        this.ColorCode = ColorCode!!
    }
    fun getStatusAutoId():Int?{
        return StatusAutoId
    }
    fun setStatusAutoId(StatusAutoId:Int?){
        this.StatusAutoId = StatusAutoId!!
    }
    fun getTotalNoOfItem():Int?{
        return TotalNoOfItem
    }
    fun setTotalNoOfItem(TotalNoOfItem:Int?){
        this.TotalNoOfItem = TotalNoOfItem!!
    }
    fun getPrintURL():String?{
        return PrintURL
    }
    fun setPrintURL(PrintUR:String?){
        this.PrintURL=PrintURL!!
    }
    fun getResumeO():Int?{
        return ResumeO
    }
    fun setResumeO(ResumeO:Int?){
        this.ResumeO = ResumeO!!
    }
}