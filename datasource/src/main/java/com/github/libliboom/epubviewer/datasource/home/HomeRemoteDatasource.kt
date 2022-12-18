package com.github.libliboom.epubviewer.datasource.home

import com.github.libliboom.epubviewer.datasource.home.mapper.mapToRanks
import com.github.libliboom.epubviewer.datasource.home.mapper.mapToSections
import com.github.libliboom.epubviewer.datasource.home.response.RankResponse
import com.github.libliboom.epubviewer.datasource.home.response.SectionResponse
import com.github.libliboom.epubviewer.datasource.utils.FetchUtils
import com.github.libliboom.epubviewer.domain.home.Section
import com.github.libliboom.epubviewer.repository.home.HomeDatasource
import com.google.gson.Gson

class HomeRemoteDatasource : HomeDatasource {

  override fun fetchRank(): List<Section> {
    val response = Gson().fromJson(FetchUtils.getRankFromFile(), RankResponse::class.java)
    return response.mapToRanks()
  }

  override fun fetchSections(): List<Section> {
    val response = Gson().fromJson(FetchUtils.getSectionFromFile(), SectionResponse::class.java)
    return response.mapToSections()
  }
}
