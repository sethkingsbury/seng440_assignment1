package com.example.trackit

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trackit.model.LogItem
import com.example.trackit.model.Project
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime


class HomeFragment : Fragment() {

    private var gson = Gson()
    @RequiresApi(Build.VERSION_CODES.O)
    private var start : String = LocalDateTime.now().toString()
    @RequiresApi(Build.VERSION_CODES.O)
    private var end : String = LocalDateTime.now().toString()
    private var selectedProject : String = ""


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
        println("#######################HERE WE RRRRRR##########################")
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
            if (startStopButton.text == "start") {
                start = LocalDateTime.now().toString()
                startStopButton.text = "stop"
            } else {
                end = LocalDateTime.now().toString()
                startStopButton.text = "start"
                var logTimes = getLogTimes(sharedPreferences)
                println(logTimes.size)
                logTimes.add(LogItem(selectedProject, start, end))
                logTime(sharedPreferences, logTimes)
            }
        }

        return view
    }

    private fun getProjects(sp : SharedPreferences?) : ArrayList<String> {
        println("#######################HERE WE RRRRRR##########################")
        var jsonProject = sp?.getString("project_list", "")
        val projectType = object : TypeToken<ArrayList<Project>>(){}.type
        if (jsonProject == "") {
            jsonProject = "[{\"name\" : \"My First Project\"}]"
            val editor = sp?.edit()
            editor?.putString("project_list", jsonProject)
            editor?.apply()
        }

        var projects = gson.fromJson<ArrayList<Project>>(jsonProject, projectType).toList()
        val projectStrings = arrayListOf<String>()
        for (project in projects) {
            projectStrings.add(project.name)
        }

        return projectStrings
    }

    private fun getLogTimes(sp : SharedPreferences?) : ArrayList<LogItem> {
        val jsonLog = sp?.getString("log_list", "")
        val logType = object : TypeToken<ArrayList<LogItem>>(){}.type
        var logTimes = gson.fromJson<ArrayList<LogItem>>(jsonLog, logType)
        if (logTimes == null) {
            logTimes = arrayListOf()
        }
        return logTimes
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun logTime(sp : SharedPreferences?,logTimes : ArrayList<LogItem>) {
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