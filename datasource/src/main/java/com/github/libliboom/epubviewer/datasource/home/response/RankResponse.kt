package com.github.libliboom.epubviewer.datasource.home.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RankResponse(
  @Json(name = "ranks")
  val ranks: List<Rank?>? = listOf()
) {
  @JsonClass(generateAdapter = true)
  data class Rank(
    @Json(name = "catalog")
    val catalog: List<Catalog?>? = listOf(),
    @Json(name = "name")
    val name: String? = ""
  ) {
    @JsonClass(generateAdapter = true)
    data class Catalog(
      @Json(name = "link")
      val link: String? = "",
      @Json(name = "title")
      val title: String? = ""
    )
  }
}
