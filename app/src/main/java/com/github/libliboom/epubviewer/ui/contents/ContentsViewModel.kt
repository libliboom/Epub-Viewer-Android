package com.github.libliboom.epubviewer.ui.contents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.libliboom.epubviewer.presentation.contents.ContentsPresenter
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.Action
import kotlinx.coroutines.launch

class ContentsViewModel : ViewModel() {

  private val presenter by lazy { ContentsPresenter() }

  fun bind(binder: ContentsViewBinder) {
    viewModelScope.launch {
      presenter.state.collect { binder.represent(it) }
    }
  }

  fun dispatch(action: Action) {
    presenter.dispatch(action)
  }
}
