package com.example.grama_suvidhaportal.data.model

import java.io.Serializable

/**
 * Data model for a Village Development Project.
 * Implements Serializable so it can be passed between Activities via Intent.
 */
data class Project(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val budget: String = "",
    val status: String = "Ongoing",
    val progress: Int = 0,
    val expectedCompletionDate: String = "",
    val imageBefore: String? = null,
    val imageAfter: String? = null
) : Serializable
