package com.example.grama_suvidhaportal.utils

import android.content.Context
import com.example.grama_suvidhaportal.data.model.Project
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object JsonUtils {
    private val gson = Gson()

    fun getProjectsFromJson(context: Context, fileName: String): List<Project> {
        return try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            parseJson(jsonString)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            emptyList()
        }
    }

    fun parseJson(jsonString: String): List<Project> {
        return try {
            val listType = object : TypeToken<List<Project>>() {}.type
            val list: List<Project> = gson.fromJson(jsonString, listType)
            // Ensure no fields are null by using an explicit mapping if necessary, 
            // but for now, we'll just return the list and handle safety in UI
            list
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun toJson(projects: List<Project>): String {
        return gson.toJson(projects)
    }
}
