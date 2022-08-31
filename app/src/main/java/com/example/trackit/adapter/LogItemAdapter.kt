package com.example.trackit.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.R
import com.example.trackit.model.LogItem

class LogItemAdapter(
    private val context: Context,
    private val dataset: List<LogItem>
) : RecyclerView.Adapter<LogItemAdapter.LogItemViewHolder>() {

    class LogItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.log_title)
        val loggedTimeText: TextView = view.findViewById(R.id.loggedTime)
        val durationText: TextView = view.findViewById(R.id.durationText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.log_list_item, parent, false)
        return LogItemViewHolder(adapterLayout)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: LogItemViewHolder, position: Int) {
        val item= dataset[position]
        holder.textView.text = item.project
        holder.loggedTimeText.text = item.getLogString()
        holder.durationText.text = item.getDurationString()
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}