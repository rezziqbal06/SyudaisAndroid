package com.su.service.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateGenerator {
    companion object{

        fun getTanggal(
            pattern: String?,
            patternOutput: String?,
            date: String?
        ): String? {
            val tanggalFormat =
                SimpleDateFormat(pattern, Locale.getDefault())
            var tanggal: Date? = Date()
            try {
                tanggal = tanggalFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val formatOutput = SimpleDateFormat(patternOutput, Locale("id"))
            return formatOutput.format(tanggal)
        }

        const val DATE_FORMAT_1 = "yy-MM-dd HH:mm:ss"

        fun getCurrentDate(): String? {
            val dateFormat = SimpleDateFormat(DATE_FORMAT_1)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val today = Calendar.getInstance().time
            return dateFormat.format(today)
        }
    }
}