package com.su.service.utils

import java.lang.NumberFormatException
import java.text.DecimalFormat
import java.text.NumberFormat

class RpGenerator {
    fun generate(rp:String): String{
        val formatter = DecimalFormat("#,###,###,###,###") as NumberFormat
        var rupiah = ""
        try{
            rupiah =  formatter.format(rp.toLong())
        }catch (io: NumberFormatException){
            io.printStackTrace()
        }
        return rupiah
    }
}