package com.github.libliboom.epubviewer.domain.home.usecase

class HomeUseCase(private val repository: IHomeRepository) {
  fun getRanks() = repository.fetchRank()
  fun getSections() = repository.fetchSections()
}
