package com.github.libliboom.epub.outline.opf

import com.github.libliboom.common.parser.HtmlParser
import com.github.libliboom.epub.common.Constant.TAG_NAV_POINT
import com.google.gson.Gson

/**
 * Ncx stand for Navigation Control file for XML.
 * The example file name is toc.ncx.
 */
class NavigationControlXml(ncxPath: String) {

  var navMap = mutableMapOf<String, NavPoint>()

  init {
    registerNavigationMap(ncxPath)
  }

  private fun registerNavigationMap(ncxPath: String) {
    val gson = Gson()
    val map = HtmlParser().parseNavMap(ncxPath, TAG_NAV_POINT)
    for (point in map) {
      val navPoint = gson.fromJson(point.value, NavPoint::class.java)
      navMap[navPoint.playOrder] = navPoint
    }
  }

  /**
   * Precondition is the attributes and children nodes of navPoint are all fixed.
   */
  data class NavPoint(val id: String, val playOrder: String, val navlabelText: String, val contentSrc: String)
}
