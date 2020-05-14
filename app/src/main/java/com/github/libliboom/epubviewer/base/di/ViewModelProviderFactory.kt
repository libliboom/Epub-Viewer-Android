package com.github.libliboom.epubviewer.base.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelProviderFactory : ViewModelProvider.NewInstanceFactory {

    private var viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>

    @Inject
    constructor(viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) {
        this.viewModels = viewModels
    }

    override fun <T : ViewModel?> create(kclass: Class<T>): T {
        var viewModel = viewModels[kclass]
        viewModel ?: run {
            for ((key, value) in viewModels) {
                if (kclass.isAssignableFrom(key)) {
                    viewModel = value
                    break
                }
            }
        }

        try {
            return viewModel?.get() as T ?: throw IllegalArgumentException("Unknown class $kclass")
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}