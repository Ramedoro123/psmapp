package com.example.myapplication.com.example.whm.ui.inventoryreceive

import android.widget.Toast

class ReceiveModel (
    PID: Int, PNAME: String,
    UNITTYPE: String,UnitQTY:Int,POQTY:Int,Totalpieces:Int,DraftID:Int,qtyperunit:Int) {

    private  var PNAME: String
    private var PID: Int = 0
    private var UNIT_TYPE: String
    private var Unit_QTY: Int = 0
    private var PO_QTY: Int=0
    private var Total_Pieces: Int=0
    private var DRAFT_ID: Int=0
    private var qtyper_unit: Int=0

    init {
        this.PID = PID
        this.PNAME = PNAME
        this.UNIT_TYPE = UNITTYPE
        this.Unit_QTY = UnitQTY
        this.PO_QTY = POQTY
        this.Total_Pieces = Totalpieces
        this.DRAFT_ID = DraftID
        this.qtyper_unit = qtyperunit
    }

    fun getPID(): Int? {
        return PID
    }
    fun getPNAME(): String {
        return PNAME
    }

    fun getUnitType(): String? {
        return UNIT_TYPE
    }
    fun getUnitQTY(): Int? {
        return Unit_QTY
    }

    fun getPOQTY(): Int? {

        return PO_QTY
    }
    fun setPOQTY(POQTY: Int?) {
         this.PO_QTY=POQTY!!
    }
    fun getTotalPieces():Int?{
       return Total_Pieces
    }
    fun setTotalPieces(newpoqty:Int?) {
        if (newpoqty != null) {
            this.Total_Pieces= newpoqty*Unit_QTY
        }
    }
    fun getasperunitqty(): Int? {
        return qtyper_unit
    }
    fun setasperunitqty(newpoqty1:Int?){
        this.qtyper_unit=newpoqty1!!
    }
    fun getTotalPiece(QTYPerUint:Int?, POQTY:Int? ) {
        if (QTYPerUint != null) {
            this.Total_Pieces=QTYPerUint*POQTY!!
        }
    }
    fun getDraftID():Int?{
        return DRAFT_ID
    }

}

