package com.github.libliboom.epubviewer.util.file

import com.github.libliboom.epub.EPub
import com.github.libliboom.epub.outline.opf.NavigationControlXml

object EPubUtils {

    const val DELIMITER_NTH = "#!"

    fun getNavMap(ePub: EPub) = ePub.opf.ncx.navMap.toMap()

    fun getNcx(ePub: EPub) = ePub.opf.ncx

    fun getHref(ePub: EPub) = ePub.opf.guide.href

    fun getCover(ePub: EPub) = ePub.opf.cover.src

    fun getOebpsPath(ePub: EPub) = ePub.opf.oebpsPath

    fun getMode(mode: Boolean) = if (mode) "horizontal" else "vertical"
}
