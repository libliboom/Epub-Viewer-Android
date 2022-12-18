package com.github.libliboom.epubviewer.domain.home.usecase

import com.github.libliboom.epubviewer.domain.home.Section

interface IHomeRepository {
  fun fetchRank(): List<Section>
  fun fetchSections(): List<Section>
}
