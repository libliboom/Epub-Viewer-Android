package com.github.libliboom.epubviewer.util.resource

import android.app.Application
import com.github.libliboom.epubviewer.app.ui.ApplicationInitializer

object StringUtils : ApplicationInitializer {

  private lateinit var application: Application
  private val context get() = application.applicationContext

  override fun init(application: Application) {
    StringUtils.application = application
  }

  fun getString(stringResId: Int) = context.getString(stringResId)
}
