package com.github.libliboom.epubviewer.di.module

import com.github.libliboom.epubviewer.di.scope.FragmentScope
import com.github.libliboom.epubviewer.main.fragment.BookshelfFragment
import com.github.libliboom.epubviewer.main.fragment.ContentsFragment
import com.github.libliboom.epubviewer.main.fragment.SettingsFragment
import com.github.libliboom.epubviewer.reader.fragment.EPubReaderFragment
import com.github.libliboom.epubviewer.reader.view.ReaderMeasureFragment
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
