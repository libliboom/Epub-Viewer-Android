package com.github.libliboom.epubviewer.ui.home

import androidx.lifecycle.ViewModel
import com.github.libliboom.epubviewer.domain.home.usecase.HomeUseCase
import com.github.libliboom.epubviewer.presentation.home.HomePresenter
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action

class HomeViewModel : ViewModel() {

  private val presenter by lazy { HomePresenter(usecase) }

  private lateinit var usecase: HomeUseCase

  val state by lazy { presenter.state }

  fun bind(usecase: HomeUseCase) {
    this.usecase = usecase
  }

  fun dispatch(action: Action) {
    presenter.dispatch(action)
  }
}
