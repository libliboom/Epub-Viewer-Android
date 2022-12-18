package com.github.libliboom.epubviewer.repository.home

import com.github.libliboom.epubviewer.domain.home.usecase.IHomeRepository

class HomeRepository(private val datasource: HomeDatasource) : IHomeRepository {
  override fun fetchRank() = datasource.fetchRank()
  override fun fetchSections() = datasource.fetchSections()
}
