package com.example.grama_suvidhaportal

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.grama_suvidhaportal.data.model.Project
import com.example.grama_suvidhaportal.data.repository.ProjectRepository
import com.example.grama_suvidhaportal.databinding.ActivityAdminBinding
import com.example.grama_suvidhaportal.utils.LocaleHelper

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var repository: ProjectRepository

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = ProjectRepository(this)

        setSupportActionBar(binding.toolbarAdmin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.admin_panel)

        binding.btnSave.setOnClickListener {
            saveNewProject()
        }
    }

    private fun saveNewProject() {
        val name = binding.etTitle.text.toString()
        val budget = binding.etBudget.text.toString()
        val progressStr = binding.etProgress.text.toString()
        
        val status = when (binding.rgStatus.checkedRadioButtonId) {
            R.id.rbCompleted -> "Completed"
            R.id.rbInProgress -> "In Progress"
            else -> "Ongoing"
        }

        if (name.isBlank() || budget.isBlank() || progressStr.isBlank()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val progress = progressStr.toIntOrNull() ?: 0
        
        val newProject = Project(
            id = (System.currentTimeMillis() / 1000).toInt(),
            name = name,
            description = "Newly added project via Admin Panel.",
            budget = budget,
            status = status,
            progress = progress,
            expectedCompletionDate = "TBD",
            imageBefore = "https://images.unsplash.com/photo-1541963463532-d68292c34b19?q=80&w=600&auto=format&fit=crop",
            imageAfter = "https://images.unsplash.com/photo-1541963463532-d68292c34b19?q=80&w=600&auto=format&fit=crop"
        )

        repository.addProject(newProject)
        Toast.makeText(this, "Project Added!", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
