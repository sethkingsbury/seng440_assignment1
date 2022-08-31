package com.example.trackit.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class LogItem(val project: String, val start : String, val end : String) {


    @RequiresApi(Build.VERSION_CODES.O)
    fun getLogString() :String {
        val startTime = LocalDateTime.parse(start)
        val endTime = LocalDateTime.parse(end)
        var hours = ChronoUnit.HOURS.between(startTime, endTime)
        var minutes = ChronoUnit.MINUTES.between(startTime, endTime) - (hours * 60)
        var seconds = ChronoUnit.SECONDS.between(startTime, endTime) - (hours * 3600) - (minutes * 60)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDurationString() : String {
        val startTime = LocalDateTime.parse(start)
        val endTime = LocalDateTime.parse(end)
        val startDate = dateToString(startTime)
        val endDate = dateToString(endTime)
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