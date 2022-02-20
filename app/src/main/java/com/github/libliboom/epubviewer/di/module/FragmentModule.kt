package com.github.libliboom.epubviewer.di.module

import com.github.libliboom.epubviewer.app.di.scope.FragmentScope
import com.github.libliboom.epubviewer.ui.bookshelf.BookshelfFragment
import com.github.libliboom.epubviewer.ui.contents.ContentsFragment
import com.github.libliboom.epubviewer.ui.settings.SettingsFragment
import com.github.libliboom.epubviewer.ui.viewer.EPubReaderFragment
import com.github.libliboom.epubviewer.ui.viewer.ReaderMeasureFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {

  @FragmentScope
  @ContributesAndroidInjector
  fun contributesBookshelfFragment(): BookshelfFragment

  @FragmentScope
  @ContributesAndroidInjector
  fun contributesEPubReaderFragment(): EPubReaderFragment

  @FragmentScope
  @ContributesAndroidInjector
  fun contributesReaderMeasureFragment(): ReaderMeasureFragment

  @FragmentScope
  @ContributesAndroidInjector
  fun contributesContentsFragment(): ContentsFragment

  @FragmentScope
  @ContributesAndroidInjector
  fun contributesSettingsFragment(): SettingsFragment
}
