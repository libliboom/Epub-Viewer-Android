package com.github.libliboom.epubviewer.base.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelProviderFactory @Inject constructor(
    private var viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) :
    ViewModelProvider.NewInstanceFactory() {

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
