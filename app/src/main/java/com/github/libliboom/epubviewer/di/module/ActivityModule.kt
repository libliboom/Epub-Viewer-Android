package com.github.libliboom.epubviewer.di.module

import com.github.libliboom.epubviewer.app.di.scope.ActivityScope
import com.github.libliboom.epubviewer.ui.bookshelf.BookshelfActivity
import com.github.libliboom.epubviewer.ui.contents.ContentsActivity
import com.github.libliboom.epubviewer.ui.settings.SettingsActivity
import com.github.libliboom.epubviewer.ui.splash.SplashActivity
import com.github.libliboom.epubviewer.ui.viewer.EPubReaderActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [FragmentModule::class])
interface ActivityModule {

  @ActivityScope
  @ContributesAndroidInjector
  fun contributesSplashActivity(): SplashActivity

  @ActivityScope
  @ContributesAndroidInjector
  fun contributesBookshelfActivity(): BookshelfActivity

  @ActivityScope
  @ContributesAndroidInjector
  fun contributesEPubReaderActivity(): EPubReaderActivity

  @ActivityScope
  @ContributesAndroidInjector
  fun contributesContentsActivity(): ContentsActivity

  @ActivityScope
  @ContributesAndroidInjector
  fun contributesSettingsActivity(): SettingsActivity
}
