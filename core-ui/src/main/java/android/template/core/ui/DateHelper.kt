package android.template.core.ui

import java.text.SimpleDateFormat
import java.util.Date

fun timestampToDate(timeStamp: Long): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy")
    val time = Date(timeStamp)
    return sdf.format(time)
}