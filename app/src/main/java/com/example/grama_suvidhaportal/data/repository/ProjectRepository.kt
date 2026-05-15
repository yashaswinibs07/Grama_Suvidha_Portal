package com.example.grama_suvidhaportal.data.repository

import android.content.Context
import com.example.grama_suvidhaportal.data.model.Project
import com.example.grama_suvidhaportal.utils.JsonUtils
import java.io.File


class ProjectRepository(private val context: Context) {

    private val cachedFileName = "projects_v3_cache.json"

    fun getProjects(): List<Project> {
        val cacheFile = File(context.filesDir, cachedFileName)
        
        return if (cacheFile.exists()) {
            val jsonString = cacheFile.readText()
            JsonUtils.parseJson(jsonString)
        } else {
            val projects = JsonUtils.getProjectsFromJson(context, "projects.json")
            saveProjectsToCache(projects)
            projects
        }
    }

    private fun saveProjectsToCache(projects: List<Project>) {
        val jsonString = JsonUtils.toJson(projects)
        val cacheFile = File(context.filesDir, cachedFileName)
        cacheFile.writeText(jsonString)
    }

    fun addProject(project: Project) {
        val projects = getProjects().toMutableList()
        projects.add(project)
        saveProjectsToCache(projects)
    }

    fun refreshProjectsFromServer(): List<Project> {
        // Simulation: in real app, fetch from API then cache
        return getProjects()
    }
}
