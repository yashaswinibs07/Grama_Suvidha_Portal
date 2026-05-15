package com.example.grama_suvidhaportal.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.grama_suvidhaportal.DetailActivity
import com.example.grama_suvidhaportal.R
import com.example.grama_suvidhaportal.data.model.Project
import com.example.grama_suvidhaportal.databinding.ItemProjectBinding

class ProjectAdapter(private val projects: List<Project>) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(val binding: ItemProjectBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemProjectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        val context = holder.itemView.context
        
        holder.binding.apply {
            textViewTitle.text = project.name
            
            // Format status label
            textViewStatus.text = context.getString(R.string.status_label, project.status)
            
            // Dynamic Status Color
            val statusColor = when (project.status) {
                "Completed" -> R.color.status_completed
                "In Progress" -> R.color.status_in_progress
                "Ongoing" -> R.color.status_ongoing
                else -> R.color.status_pending
            }
            textViewStatus.setTextColor(ContextCompat.getColor(context, statusColor))

            textViewBudget.text = context.getString(R.string.budget_label, project.budget)
            textViewCompletionDate.text = context.getString(R.string.expected_completion, project.expectedCompletionDate)
            
            // --- UPDATED IMAGE LOADING LOGIC ---
            // 1. Try to load from drawable if the name matches
            // 2. If not found or null, load from URL
            val imageResId = context.resources.getIdentifier(project.imageAfter, "drawable", context.packageName)
            
            if (imageResId != 0) {
                imageViewProject.load(imageResId) {
                    crossfade(enable = true)
                    placeholder(android.R.drawable.progress_horizontal)
                }
            } else {
                imageViewProject.load(project.imageAfter) {
                    crossfade(enable = true)
                    placeholder(android.R.drawable.progress_horizontal)
                    error(android.R.drawable.ic_menu_report_image)
                }
            }

            // Click listener
            root.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("PROJECT_DATA", project)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = projects.size
}
