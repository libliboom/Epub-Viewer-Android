package com.github.libliboom.epubviewer.base.di.launcher

import androidx.lifecycle.ViewModel
import com.github.libliboom.epubviewer.base.di.ViewModelKey
import com.github.libliboom.epubviewer.main.viewmodel.BookshelfViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BookshelfViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BookshelfViewModel::class)
    abstract fun bindBookshelfViewModel(viewModel: BookshelfViewModel): ViewModel
}
