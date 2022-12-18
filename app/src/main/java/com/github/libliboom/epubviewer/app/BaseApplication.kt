package com.github.libliboom.epubviewer.app

import com.github.libliboom.epubviewer.datasource.settings.SettingsPreference
import com.github.libliboom.epubviewer.datasource.utils.FetchUtils
import com.github.libliboom.epubviewer.di.DaggerApplicationComponent
import com.github.libliboom.epubviewer.util.resource.StringUtils
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BaseApplication : DaggerApplication() {

  private val initializers = listOf(
    FetchUtils,
    StringUtils,
    SettingsPreference
  )

  override fun onCreate() {
    super.onCreate()
    initializers.forEach { it.init(this) }
  }

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerApplicationComponent
      .factory()
      .create(this)
  }
}
