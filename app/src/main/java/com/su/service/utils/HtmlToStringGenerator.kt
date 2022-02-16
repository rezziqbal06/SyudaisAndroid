package com.su.service.utils

import android.os.Build
import android.text.Html

class HtmlToStringGenerator {
    companion object{
        fun generate(html: String) : String{
            var string = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE).toString()
            } else {
                Html.fromHtml(html).toString()
            }
            return string
        }
    }
}