package com.github.libliboom.epubviewer.base.di.launcher

import com.github.libliboom.epubviewer.main.fragment.BookshelfFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BookshelfFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeBookshelfFragment(): BookshelfFragment
}