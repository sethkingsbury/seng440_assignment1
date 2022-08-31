package com.example.trackit.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class LogItem(val project: String, val start : LocalDateTime, val end : LocalDateTime) {


    @RequiresApi(Build.VERSION_CODES.O)
    fun getLogString() :String {
        var hours = ChronoUnit.HOURS.between(start, end)
        var minutes = ChronoUnit.MINUTES.between(start, end) - (hours * 60)
        var seconds = ChronoUnit.SECONDS.between(start, end) - (hours * 3600) - (minutes * 60)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDurationString() : String {
        val startDate = dateToString(start)
        val endDate = dateToString(end)
        return "$startDate - $endDate"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateToString(date : LocalDateTime) : String {
        val h = date.hour
        val m = date.minute
        val s = date.second
        return String.format("%02d:%02d:%02d", h, m, s)
    }

}