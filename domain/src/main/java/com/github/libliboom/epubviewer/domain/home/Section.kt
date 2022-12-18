package com.github.libliboom.epubviewer.domain.home

interface Section

data class RankSection(
  val name: String,
  val catalogs: List<Catalog>
) : Section

data class Catalog(
  val title: String,
  val link: String
)

data class LinearRowSection(
  val name: String,
  val books: List<Book>
) : Section

data class LinearColumnSection(
  val name: String,
  val books: List<Book>
) : Section

data class Book(
  val id: Int,
  val imageUrl: String,
  val language: String,
  val author: String,
  val link: String,
  val originalPublication: String,
  val releaseDate: String,
  val title: String,
)
