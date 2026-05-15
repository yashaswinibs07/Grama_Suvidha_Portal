package com.example.grama_suvidhaportal

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import com.example.grama_suvidhaportal.data.model.Project
import com.example.grama_suvidhaportal.databinding.ActivityDetailBinding
import com.example.grama_suvidhaportal.utils.LocaleHelper
import java.io.Serializable

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val project = intent.getSerializable("PROJECT_DATA", Project::class.java)

        project?.let {
            populateUI(it)
        }
    }

    private fun <T : Serializable> android.content.Intent.getSerializable(key: String, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(key, clazz)
        } else {
            @Suppress("DEPRECATION", "UNCHECKED_CAST")
            this.getSerializableExtra(key) as? T
        }
    }

    private fun populateUI(project: Project) {
        binding.apply {
            detailTitle.text = project.name
            detailDescription.text = project.description
            
            detailBudget.text = getString(R.string.budget_label, project.budget)
            detailStatus.text = getString(R.string.status_label, project.status)
            
            val statusColor = when (project.status) {
                "Completed" -> R.color.status_completed
                "In Progress" -> R.color.status_in_progress
                "Ongoing" -> R.color.status_ongoing
                else -> R.color.status_pending
            }
            detailStatus.setTextColor(ContextCompat.getColor(this@DetailActivity, statusColor))

            detailDate.text = getString(R.string.expected_completion, project.expectedCompletionDate)

            detailProgressBar.progress = project.progress
            detailProgressText.text = getString(R.string.progress_label, project.progress)

            // --- REFINED IMAGE LOADING ---
            loadImage(project.imageBefore, imageBefore)
            loadImage(project.imageAfter, imageAfter)
            
            buttonSubmitFeedback.setOnClickListener {
                Toast.makeText(this@DetailActivity, R.string.feedback_success, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadImage(imageSource: String?, imageView: android.widget.ImageView) {
        if (imageSource == null) return
        
        val imageResId = resources.getIdentifier(imageSource, "drawable", packageName)
        if (imageResId != 0) {
            imageView.load(imageResId) {
                crossfade(enable = true)
                placeholder(android.R.drawable.progress_horizontal)
            }
        } else {
            imageView.load(imageSource) {
                crossfade(enable = true)
                placeholder(android.R.drawable.progress_horizontal)
                error(android.R.drawable.ic_menu_report_image)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
