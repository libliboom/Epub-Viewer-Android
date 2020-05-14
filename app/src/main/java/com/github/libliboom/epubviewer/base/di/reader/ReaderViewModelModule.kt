package com.github.libliboom.epubviewer.base.di.reader

import androidx.lifecycle.ViewModel
import com.github.libliboom.epubviewer.base.di.ViewModelKey
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReaderViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(EPubReaderViewModel::class)
    abstract fun bindEPubReaderViewModel(viewModel: EPubReaderViewModel): ViewModel
}