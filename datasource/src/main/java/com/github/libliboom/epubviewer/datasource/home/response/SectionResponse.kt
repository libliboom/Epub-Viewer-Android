package com.github.libliboom.epubviewer.datasource.home.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SectionResponse(
  @Json(name = "sections")
  val sections: List<Section?>? = listOf()
) {
  @JsonClass(generateAdapter = true)
  data class Section(
    @Json(name = "books")
    val books: List<Book?>? = listOf(),
    @Json(name = "name")
    val name: String? = ""
  ) {
    @JsonClass(generateAdapter = true)
    data class Book(
      @Json(name = "author")
      val author: String? = "",
      @Json(name = "image")
      val image: String? = "",
      @Json(name = "language")
      val language: String? = "",
      @Json(name = "link")
      val link: String? = "",
      @Json(name = "original_publication")
      val original_publication: String? = "None",
      @Json(name = "release_date")
      val release_date: String? = "",
      @Json(name = "title")
      val title: String? = ""
    )
  }
}
