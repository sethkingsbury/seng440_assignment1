package com.example.trackit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trackit.model.Project
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HomeFragment : Fragment() {

    private var gson = Gson()

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

        var sp = this.activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val jsonProject = sp?.getString("project_list", "")
        val projectType = object : TypeToken<ArrayList<Project>>(){}.type
        val projects = gson.fromJson<ArrayList<Project>>(jsonProject, projectType).toList()
        var projectStrings = arrayListOf<String>()
        for (project in projects) {
            projectStrings.add(project.name)
        }

        val spinner : Spinner = view.findViewById(R.id.spinner)
        val spinnerArrayAdapter: ArrayAdapter<String>? = context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, projectStrings) }

        spinnerArrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerArrayAdapter

        println("################################################################")
        println(spinner)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(context, getString(R.string.selected_item) + " " + "" + projectStrings[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                Toast.makeText(context, R.string.no_selected_item, Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}