package com.readthym.doesapp.utils

import android.text.Html

import android.text.Spanned
import java.lang.StringBuilder
import android.os.Build
import java.text.SimpleDateFormat

class DateUtils {

    fun getReadableDate(unix:String): String? {
        val redr = unix.toLong()
        val sdf = SimpleDateFormat("MMMM d, yyyy 'at' h:mm a")
        val date = sdf.format(redr)
        return date
    }


}