package com.example.collegeschedule.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


fun getWeekDateRange(): Pair<String, String> {

    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ISO_DATE

    var start = if (today.dayOfWeek == DayOfWeek.SUNDAY)
        today.plusDays(1)
    else
        today

    var daysAdded = 0
    var end = start

    while (daysAdded < 5) {
        end = end.plusDays(1)
        if (end.dayOfWeek != DayOfWeek.SUNDAY) {
            daysAdded++
        }
    }

    return start.format(formatter) to end.format(formatter)
}

fun formatTimeRange(time: String): Pair<String, String> {
    return try {
        val parts = time.split("-")
        val formatter = DateTimeFormatter.ofPattern("H:mm")

        var start = LocalTime.parse(parts[0].trim(), formatter)
        var end = LocalTime.parse(parts[1].trim(), formatter)

        // если время выглядит как ночное — делаем его дневным
        if (start.hour in 1..6) {
            start = start.plusHours(12)
        }
        if (end.hour in 1..6) {
            end = end.plusHours(12)
        }

        val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")

        Pair(
            start.format(outputFormatter),
            end.format(outputFormatter)
        )
    } catch (e: Exception) {
        Pair(time, "")
    }
}


fun formatDate(date: String): String {
    return try {
        val cleanDate = date.substring(0, 10)
        val parsed = LocalDate.parse(cleanDate)
        val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
        parsed.format(formatter)
    } catch (e: Exception) {
        date
    }
}

fun isLessonNow(time: String): Boolean {
    return try {
        val parts = time.split("-")
        val formatter = DateTimeFormatter.ofPattern("H:mm")

        var start = LocalTime.parse(parts[0].trim(), formatter)
        var end = LocalTime.parse(parts[1].trim(), formatter)

        if (start.hour in 1..6) start = start.plusHours(12)
        if (end.hour in 1..6) end = end.plusHours(12)

        val now = LocalTime.now()

        now.isAfter(start) && now.isBefore(end)
    } catch (e: Exception) {
        false
    }
}
