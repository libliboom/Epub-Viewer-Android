package com.github.libliboom.epubviewer.di.module

import com.github.libliboom.epubviewer.di.scope.ActivityScope
import com.github.libliboom.epubviewer.main.activity.BookshelfActivity
import com.github.libliboom.epubviewer.main.activity.ContentsActivity
import com.github.libliboom.epubviewer.main.activity.SettingsActivity
import com.github.libliboom.epubviewer.reader.activity.EPubReaderActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [FragmentModule::class])
interface ActivityModule {

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
