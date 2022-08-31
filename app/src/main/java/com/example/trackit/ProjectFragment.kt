package com.example.trackit

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.adapter.ProjectItemAdapter
import com.example.trackit.model.Project
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ProjectFragment : Fragment() {

    private var gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var sp = this.activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project, container, false)

        val button = view.findViewById<Button>(R.id.homeButton)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_projectFragment_to_addProjectFragment)
        }

        val jsonProject = sp?.getString("project_list", "")
        val projectType = object : TypeToken<ArrayList<Project>>(){}.type
        val projects = gson.fromJson<ArrayList<Project>>(jsonProject, projectType).toList()

        val recyclerView = view.findViewById<RecyclerView>(R.id.projectList)
        recyclerView.adapter = this.context?.let { ProjectItemAdapter(it, projects) }
        recyclerView.setHasFixedSize(true)

        val backButton = view.findViewById<Button>(R.id.projectsBackButton)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_projectFragment_to_homeFragment)
        }

        return view
    }


}
