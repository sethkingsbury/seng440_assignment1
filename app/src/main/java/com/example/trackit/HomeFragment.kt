package com.example.trackit

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentHostCallback
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.example.trackit.model.*
import com.example.trackit.service.TimerService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import kotlin.math.roundToInt


class HomeFragment : Fragment() {

    private var gson = Gson()
    @RequiresApi(Build.VERSION_CODES.O)
    private var start : String = LocalDateTime.now().toString()
    @RequiresApi(Build.VERSION_CODES.O)
    private var end : String = LocalDateTime.now().toString()
    private var selectedProject : String = ""

    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    private lateinit var timeText : TextView

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private val notified = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val context = this.context
        val projectsButton = view.findViewById<Button>(R.id.toProjectsButton)
        projectsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_projectFragment)
        }

        val logsButton = view.findViewById<Button>(R.id.toLogsButton)
        logsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_logFragment)
        }

        val sharedPreferences = this.activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
//        clearAllSharedPreferences(sharedPreferences)
        val projectStrings = getProjects(sharedPreferences)

        val spinner : Spinner = view.findViewById(R.id.spinner)
        val spinnerArrayAdapter: ArrayAdapter<String>? = context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, projectStrings) }

        spinnerArrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerArrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                selectedProject = projectStrings[position]
                Toast.makeText(context, getString(R.string.selected_item) + " " + "" + projectStrings[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                Toast.makeText(context, R.string.no_selected_item, Toast.LENGTH_SHORT).show()
            }
        }

        val startStopButton = view.findViewById<Button>(R.id.startStopButton)
        startStopButton.setOnClickListener {
            if (startStopButton.text == resources.getString(R.string.start)) {
                startTimer()
                start = LocalDateTime.now().toString()
                startStopButton.text = resources.getString(R.string.stop)
            } else {
                stopTimer()
                end = LocalDateTime.now().toString()
                startStopButton.text = resources.getString(R.string.start)
                var logTimes = getLogTimes(sharedPreferences)
                logTimes.addFirst(LogItem(selectedProject, start, end))
                logTime(sharedPreferences, logTimes)
            }
        }

//        Visual Timer Code
        timeText = view.findViewById(R.id.timeText)
        serviceIntent = Intent(this.context, TimerService::class.java)
        this.activity?.registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
        this.activity?.let { LocalBroadcastManager.getInstance(it).registerReceiver(updateTime,  IntentFilter(TimerService.TIMER_UPDATED)) };

        return view
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        this.activity?.startService(serviceIntent)
        timerStarted = true
    }

    private fun stopTimer() {
        this.activity?.stopService(serviceIntent)
        timerStarted = false
        time = 0.0
        timeText.text = getTimeStringFromDouble(time)
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            timeText.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val h = resultInt % 86400 / 3600
        val m = resultInt % 86400 % 3600 / 60
        val s = resultInt % 86400 % 3600 % 60
        return String.format("%02d:%02d:%02d", h, m, s)
    }

    private fun getProjects(sp : SharedPreferences?) : ArrayList<String> {
        var jsonProject = sp?.getString("project_list", "")
        val projectType = object : TypeToken<ArrayList<Project>>(){}.type
        if (jsonProject == "") {
            val defaultProject = resources.getString(R.string.defaultProject)
            jsonProject = "[{\"name\" : \"$defaultProject\"}]"
            val editor = sp?.edit()
            editor?.putString("project_list", jsonProject)
            editor?.apply()
        }

        val projects = gson.fromJson<ArrayList<Project>>(jsonProject, projectType).toList()
        val projectStrings = arrayListOf<String>()
        for (project in projects) {
            projectStrings.add(project.name)
        }

        return projectStrings
    }

    private fun getLogTimes(sp : SharedPreferences?) : ArrayDeque<LogItem> {
        val jsonLog = sp?.getString("log_list", "")
        val logType = object : TypeToken<ArrayDeque<LogItem>>(){}.type
        var logTimes = gson.fromJson<ArrayDeque<LogItem>>(jsonLog, logType)
        if (logTimes == null) {
            logTimes = ArrayDeque()
        }
        return logTimes
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun logTime(sp : SharedPreferences?,logTimes : ArrayDeque<LogItem>) {
        val editor = sp?.edit()
        val jsonString = gson.toJson(logTimes)
        editor?.putString("log_list", jsonString)
        editor?.apply()
    }

    fun clearAllSharedPreferences(sp : SharedPreferences?) {
        val editor = sp?.edit()
        editor?.clear()
        editor?.commit()
    }

}