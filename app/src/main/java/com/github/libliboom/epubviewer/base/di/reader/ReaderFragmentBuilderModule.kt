package com.github.libliboom.epubviewer.base.di.reader

import com.github.libliboom.epubviewer.main.fragment.ContentsFragment
import com.github.libliboom.epubviewer.reader.fragment.EPubReaderFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReaderFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeEPubReaderFragment(): EPubReaderFragment

    @ContributesAndroidInjector
    abstract fun contributeContentsFragment(): ContentsFragment
}