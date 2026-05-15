package com.example.grama_suvidhaportal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grama_suvidhaportal.databinding.ActivityMainBinding
import com.example.grama_suvidhaportal.ui.ProjectAdapter
import com.example.grama_suvidhaportal.utils.LocaleHelper
import com.example.grama_suvidhaportal.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.project_list)

        setupRecyclerView()
        setupSearchAndFilter()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProjects()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewProjects.layoutManager = LinearLayoutManager(this)
    }

    private fun setupSearchAndFilter() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })

        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            val filter = when (checkedIds.firstOrNull()) {
                R.id.chipOngoing -> "Ongoing"
                R.id.chipCompleted -> "Completed"
                R.id.chipInProgress -> "In Progress"
                else -> "All"
            }
            viewModel.setFilter(filter)
        }

        binding.fabAdmin.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.projects.observe(this) { projectList ->
            binding.recyclerViewProjects.adapter = ProjectAdapter(projectList)
        }

        viewModel.dashboardSummary.observe(this) { summary ->
            binding.apply {
                tvTotalProjects.text = getString(R.string.total_projects, summary.totalProjects)
                tvCompletedProjects.text = getString(R.string.completed_projects, summary.completedProjects)
                tvOngoingProjects.text = getString(R.string.ongoing_projects, summary.ongoingProjects)
                tvInProgressProjects.text = getString(R.string.in_progress_projects, summary.inProgressProjects)
                tvTotalBudget.text = getString(R.string.total_budget, summary.totalBudget)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_language -> {
                toggleLanguage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleLanguage() {
        val currentLang = LocaleHelper.getLanguage(this)
        val newLang = if (currentLang == "en") "kn" else "en"
        LocaleHelper.setLocale(this, newLang)
        
        // Refresh to apply changes immediately
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
