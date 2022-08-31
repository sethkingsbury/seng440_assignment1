package com.example.trackit

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.adapter.LogItemAdapter
import com.example.trackit.model.LogItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.Month


class LogFragment : Fragment() {

    private var gson = Gson()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log, container, false)
        val sharedPreferences = this.activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val logItems = getLogTimes(sharedPreferences)

        val button = view.findViewById<Button>(R.id.backButton)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_logFragment_to_homeFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.logList)
        recyclerView.adapter = this.context?.let { LogItemAdapter(it, logItems) }
        recyclerView.setHasFixedSize(true)

        return view
    }

    private fun getLogTimes(sp : SharedPreferences?) : ArrayList<LogItem> {
        val jsonLog = sp?.getString("log_list", "")
        println(jsonLog)
        val logType = object : TypeToken<ArrayList<LogItem>>(){}.type

        var logTimes = gson.fromJson<ArrayList<LogItem>>(jsonLog, logType)

        if (logTimes == null) {
            logTimes = arrayListOf()
        }
        println(logTimes)
        return logTimes
    }
}