package com.example.chatapp.domain.Util

import android.util.Log
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.*


object DateTimeUtil {
    fun now() : LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun toEpochToMillis(dateTime: LocalDateTime) : Long {
        return dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun fullDate(dateTime: LocalDateTime): String{
        val month = dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val day = if (dateTime.dayOfMonth < 10) "0${dateTime.dayOfMonth}" else dateTime.dayOfMonth
        val year = dateTime.year
        val hour = if (dateTime.hour < 10) "0${dateTime.hour}" else dateTime.hour
        val minute = if (dateTime.minute < 10) "0${dateTime.minute}" else dateTime.minute

        return buildString {
            append(month)
            append(" ")
            append(day)
            append(" ")
            append(year)
            append(", ")
            append(hour)
            append(":")
            append(minute)
        }
    }

    fun hourAndMinute(time_sending: Long) : String {
        val dateTime = Instant.fromEpochMilliseconds(time_sending).toLocalDateTime(TimeZone.currentSystemDefault())

        val month = dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val day = if (dateTime.dayOfMonth < 10) "0${dateTime.dayOfMonth}" else dateTime.dayOfMonth
        val hour = if (dateTime.hour < 10) "0${dateTime.hour}" else dateTime.hour
        val minute = if (dateTime.minute < 10) "0${dateTime.minute}" else dateTime.minute


        return buildString {
            append(day)
            append(" ")
            append(month)
            append(" ")
            append(hour)
            append(":")
            append(minute)
        }
    }
}