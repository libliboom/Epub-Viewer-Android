package com.github.libliboom.epubviewer.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.libliboom.epubviewer.domain.settings.SettingsUsecase
import com.github.libliboom.epubviewer.presentation.settings.SettingsPresenter
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.Action
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

  private val presenter by lazy { SettingsPresenter(usecase) }

  private lateinit var usecase: SettingsUsecase

  fun bind(usecase: SettingsUsecase) {
    this.usecase = usecase
  }

  fun bind(binder: SettingsViewBinder) {
    viewModelScope.launch {
      presenter.state.collect { binder.represent(it) }
    }
  }

  fun dispatch(action: Action) {
    presenter.dispatch(action)
  }
}
