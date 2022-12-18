package com.github.libliboom.epubviewer.datasource.home.mapper

import com.github.libliboom.epubviewer.datasource.home.response.RankResponse
import com.github.libliboom.epubviewer.datasource.home.response.SectionResponse
import com.github.libliboom.epubviewer.domain.home.Book
import com.github.libliboom.epubviewer.domain.home.Catalog
import com.github.libliboom.epubviewer.domain.home.LinearColumnSection
import com.github.libliboom.epubviewer.domain.home.LinearRowSection
import com.github.libliboom.epubviewer.domain.home.RankSection
import com.github.libliboom.epubviewer.domain.home.Section
import com.github.libliboom.epubviewer.domain.home.usecase.GridSection

fun RankResponse.mapToRanks(): List<Section> {
  return mutableListOf<Section>().apply {
    ranks!!.map { rank ->
      val catalogs = rank!!.catalog!!.map { item -> item!!.mapToCatalog() }
      add(
        RankSection(name = rank.name!!, catalogs = catalogs)
      )
    }
  }
}

private fun RankResponse.Rank.Catalog.mapToCatalog(): Catalog {
  return Catalog(
    title = this.title!!,
    link = this.link!!
  )
}

fun SectionResponse.mapToSections(): List<Section> {
  return mutableListOf<Section>().apply {
    sections!!.map { section ->
      val books = section!!.books!!.map { book -> book!!.mapToBook() }
      add(
        when (section.name) {
          "Today" -> LinearRowSection(name = section.name, books = books)
          "Popular" -> GridSection(name = section.name, books = books)
          "Recommend" -> LinearColumnSection(name = section.name, books = books)
          else -> LinearRowSection(name = section.name!!, books = books)
        }
      )
    }
  }
}

private fun SectionResponse.Section.Book.mapToBook(): Book {
  return Book(
    id = 0,
    imageUrl = this.image!!,
    language = this.language!!,
    author = this.author!!,
    link = this.link!!,
    originalPublication = this.original_publication!!,
    releaseDate = this.release_date!!,
    title = this.title!!,
  )
}
