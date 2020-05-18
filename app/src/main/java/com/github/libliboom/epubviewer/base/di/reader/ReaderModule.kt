package com.github.libliboom.epubviewer.base.di.reader

import com.github.libliboom.epubviewer.main.recycler.adapter.ContentsAdapter
import com.github.libliboom.epubviewer.reader.fragment.EPubReaderFragment
import dagger.Module
import dagger.Provides

@Module
class ReaderModule {

    @ReaderScope
    @Provides
    fun provideAdapter(): ContentsAdapter {
        return ContentsAdapter()
    }

    @ReaderScope
    @Provides
    fun provideFragment(): EPubReaderFragment {
        return EPubReaderFragment()
    }
}