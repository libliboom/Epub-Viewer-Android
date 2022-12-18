package com.github.libliboom.epubviewer.datasource.utils

import android.app.Application
import com.github.libliboom.epubviewer.app.ui.ApplicationInitializer
import com.github.libliboom.epubviewer.datasource.R

object FetchUtils : ApplicationInitializer {

  private lateinit var application: Application
  private val context get() = application.applicationContext

  override fun init(application: Application) {
    FetchUtils.application = application
  }

  fun getRankFromFile(): String {
    return context.resources.openRawResource(R.raw.home_rank)
      .bufferedReader()
      .use { it.readText() }
  }

  fun getSectionFromFile(): String {
    return context.resources.openRawResource(R.raw.home_section)
      .bufferedReader()
      .use { it.readText() }
  }
}
