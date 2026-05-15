package com.example.grama_suvidhaportal.data.model

data class DashboardSummary(
    val totalProjects: Int,
    val completedProjects: Int,
    val ongoingProjects: Int,
    val inProgressProjects: Int,
    val totalBudget: String
)
