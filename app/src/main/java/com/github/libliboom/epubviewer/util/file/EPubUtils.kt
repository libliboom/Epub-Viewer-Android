package com.github.libliboom.epubviewer.util.file

import com.github.libliboom.common.io.FileUtils
import com.github.libliboom.epub.EPub

object EPubUtils {

  const val DELIMITER_NTH = "#!"

  fun getNavMap(ePub: EPub) = ePub.opf.ncx.navMap.toMap()

  fun getNcx(ePub: EPub) = ePub.opf.ncx

  fun getHref(ePub: EPub) = ePub.opf.guide.href

  fun getCover(ePub: EPub) = ePub.opf.cover.src

  fun getOebpsPath(ePub: EPub) = ePub.opf.oebpsPath

  fun getMode(mode: Boolean) = if (mode) "horizontal" else "vertical"

  fun getUri(pageInfo: Pair<String, Int>) =
    FileUtils.getFileUri(pageInfo.first) + "${DELIMITER_NTH}${pageInfo.second}"
}
