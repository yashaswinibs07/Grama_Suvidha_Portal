package com.example.grama_suvidhaportal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.grama_suvidhaportal.data.model.DashboardSummary
import com.example.grama_suvidhaportal.data.model.Project
import com.example.grama_suvidhaportal.data.repository.ProjectRepository
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProjectRepository(application)
    
    private val _projects = MutableLiveData<List<Project>>()
    private val _filteredProjects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> = _filteredProjects

    private var currentSearchQuery: String = ""
    private var currentFilter: String = "All"

    val dashboardSummary: LiveData<DashboardSummary> = _projects.map { projectList ->
        calculateSummary(projectList)
    }

    init {
        loadProjects()
    }

    fun loadProjects() {
        val data = repository.getProjects()
        _projects.value = data
        applyFilters()
    }

    fun setSearchQuery(query: String) {
        currentSearchQuery = query
        applyFilters()
    }

    fun setFilter(filter: String) {
        currentFilter = filter
        applyFilters()
    }

    private fun applyFilters() {
        val allProjects = _projects.value ?: return
        
        val filtered = allProjects.filter { project ->
            // Use safe access because Gson can bypass Kotlin non-nullability
            val projectName = project.name ?: ""
            val projectStatus = project.status ?: ""
            
            val matchesSearch = projectName.contains(currentSearchQuery, ignoreCase = true)
            
            val matchesFilter = when (currentFilter) {
                "In Progress" -> projectStatus.equals("In Progress", ignoreCase = true)
                "Ongoing" -> projectStatus.equals("Ongoing", ignoreCase = true)
                "Completed" -> projectStatus.equals("Completed", ignoreCase = true)
                else -> true // "All"
            }
            matchesSearch && matchesFilter
        }
        _filteredProjects.value = filtered
    }

    private fun calculateSummary(projectList: List<Project>): DashboardSummary {
        val total = projectList.size
        val completed = projectList.count { (it.status ?: "").equals("Completed", ignoreCase = true) }
        val inProgress = projectList.count { (it.status ?: "").equals("In Progress", ignoreCase = true) }
        val ongoing = projectList.count { (it.status ?: "").equals("Ongoing", ignoreCase = true) }
        
        var totalBudgetValue = 0L
        projectList.forEach { project ->
            val budget = project.budget ?: ""
            val cleanBudget = budget.replace(Regex("[^0-9]"), "")
            if (cleanBudget.isNotEmpty()) {
                try {
                    totalBudgetValue += cleanBudget.toLong()
                } catch (e: NumberFormatException) {
                    // Ignore malformed budgets
                }
            }
        }
        
        val formattedBudget = "₹" + String.format(Locale.getDefault(), "%,d", totalBudgetValue)

        return DashboardSummary(total, completed, ongoing, inProgress, formattedBudget)
    }

    fun refreshData() {
        val data = repository.refreshProjectsFromServer()
        _projects.value = data
        applyFilters()
    }
}
