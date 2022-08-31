package com.example.trackit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.R
import com.example.trackit.model.Project

class ProjectItemAdapter(
    private val context: Context,
    private val dataset: List<Project>,
    ) : RecyclerView.Adapter<ProjectItemAdapter.ProjectItemViewHolder>() {

    class ProjectItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.project_list_item, parent, false)
        return ProjectItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ProjectItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text =  item.name
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}