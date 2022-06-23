package io.github.junkfood.podcast.util

import java.text.DateFormat
import java.util.Date

object TextUtil {

    fun parseDate(date: Date): String {
        val df: DateFormat =
            DateFormat.getDateInstance(
                DateFormat.MEDIUM,
                java.util.Locale.getDefault()
            )
        return df.format(date)
    }
}