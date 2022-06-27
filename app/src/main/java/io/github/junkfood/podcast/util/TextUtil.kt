package io.github.junkfood.podcast.util

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
}