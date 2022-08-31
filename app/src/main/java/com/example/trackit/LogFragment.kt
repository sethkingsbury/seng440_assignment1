package com.example.trackit

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
import java.time.LocalDateTime
import java.time.Month


class LogFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val start : LocalDateTime = LocalDateTime.of(2022, Month.AUGUST, 31, 9, 43, 15)
    @RequiresApi(Build.VERSION_CODES.O)
    private val end : LocalDateTime = LocalDateTime.of(2022, Month.AUGUST, 31, 16, 45,5)

    @RequiresApi(Build.VERSION_CODES.O)
    private val logItems = listOf(
        LogItem("Something", start, end),
        LogItem("Something", start, end),
        LogItem("Something", start, end),
        LogItem("Something", start, end),
        LogItem("Something", start, end),
        LogItem("Something", start, end),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log, container, false)

        val button = view.findViewById<Button>(R.id.backButton)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_logFragment_to_homeFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.logList)
        recyclerView.adapter = this.context?.let { LogItemAdapter(it, logItems) }
        recyclerView.setHasFixedSize(true)

        return view
    }
}