package io.github.junkfood.heal.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object TextUtil {

    private val pattern = "EEE MMM dd HH:mm:ss zzz yyyy"

    fun formatDate(date: Date): String {
        val calender = Calendar.getInstance()
        calender.time = date
        return calender.time.toString()
    }

    fun formatString(string: String): Date? {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
        return simpleDateFormat.parse(string)
    }

    fun parseDate(date: Date): String {
        val df: DateFormat =
            DateFormat.getDateInstance(
                DateFormat.MEDIUM,
                java.util.Locale.getDefault()
            )
        return df.format(date)
    }

    fun compareDate(s1: String, s2: String): Int {
        if (s1 == s2) return 0
        return if (formatString(s1)?.after(formatString(s2)) == true) 1 else -1
    }
}