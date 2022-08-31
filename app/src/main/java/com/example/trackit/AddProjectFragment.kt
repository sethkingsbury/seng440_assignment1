package com.example.trackit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trackit.model.Project
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AddProjectFragment : Fragment() {

    private var gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_project, container, false)

        val button = view.findViewById<Button>(R.id.addProjectButton)
        val projectName = view.findViewById<TextInputEditText>(R.id.projectName)

        button.setOnClickListener {
            var sp = this.activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
            var editor = sp?.edit()

            val jsonProject = sp?.getString("project_list", "")
            val projectType = object : TypeToken<ArrayList<Project>>(){}.type
            val projects = gson.fromJson<ArrayList<Project>>(jsonProject, projectType)
            var project = Project(projectName.text.toString())
            projects.add(project)
            val jsonString = gson.toJson(projects)
            editor?.putString("project_list", jsonString)
            editor?.apply()

            Toast.makeText(view.context, "Project Added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addProjectFragment_to_projectFragment)
        }
        return view
    }
}

