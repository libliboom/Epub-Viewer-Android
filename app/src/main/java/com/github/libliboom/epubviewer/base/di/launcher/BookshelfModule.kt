package com.github.libliboom.epubviewer.base.di.launcher

import com.github.libliboom.epubviewer.main.recycler.adapter.BookListAdapter
import dagger.Module
import dagger.Provides

@Module
class BookshelfModule {

    @BookshelfScope
    @Provides
    fun provideAdapter(): BookListAdapter {
        return BookListAdapter()
    }
}