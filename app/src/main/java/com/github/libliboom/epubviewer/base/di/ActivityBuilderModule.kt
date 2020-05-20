package com.github.libliboom.epubviewer.base.di

import com.github.libliboom.epubviewer.base.di.launcher.BookshelfFragmentBuilderModule
import com.github.libliboom.epubviewer.base.di.launcher.BookshelfModule
import com.github.libliboom.epubviewer.base.di.launcher.BookshelfScope
import com.github.libliboom.epubviewer.base.di.launcher.BookshelfViewModelModule
import com.github.libliboom.epubviewer.base.di.reader.ReaderFragmentBuilderModule
import com.github.libliboom.epubviewer.base.di.reader.ReaderModule
import com.github.libliboom.epubviewer.base.di.reader.ReaderScope
import com.github.libliboom.epubviewer.base.di.reader.ReaderViewModelModule
import com.github.libliboom.epubviewer.main.activity.BookshelfActivity
import com.github.libliboom.epubviewer.main.activity.ContentsActivity
import com.github.libliboom.epubviewer.main.activity.SettingsActivity
import com.github.libliboom.epubviewer.reader.activity.ReaderActivity
import com.github.libliboom.epubviewer.reader.activity.EPubReaderActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @BookshelfScope
    @ContributesAndroidInjector(modules = [
        BookshelfModule::class, BookshelfViewModelModule::class, BookshelfFragmentBuilderModule::class])
    abstract fun contributeBookshelfActivity(): BookshelfActivity

    @ReaderScope
    @ContributesAndroidInjector(modules = [ReaderViewModelModule::class])
    abstract fun contributeReaderActivity(): ReaderActivity

    @ReaderScope
    @ContributesAndroidInjector(modules = [
        ReaderModule::class, ReaderViewModelModule::class, ReaderFragmentBuilderModule::class])
    abstract fun contributeEPubReaderActivity(): EPubReaderActivity

    @ReaderScope
    @ContributesAndroidInjector(modules = [
        ReaderModule::class, ReaderViewModelModule::class, ReaderFragmentBuilderModule::class])
    abstract fun contributeContentsActivity(): ContentsActivity

    @ReaderScope
    @ContributesAndroidInjector(modules = [
        ReaderModule::class, ReaderViewModelModule::class, ReaderFragmentBuilderModule::class])
    abstract fun contributeSettingsActivity(): SettingsActivity
}