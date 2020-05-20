package com.github.libliboom.epubviewer.util.file

import com.github.libliboom.epub.EPub
import com.github.libliboom.epub.outline.opf.NavigationControlXml

object EPubUtils {

    fun getContentsSrcFileName(filePath: String): String {
        return filePath.split("#")[0]
    }

    fun getNavMap(ePub: EPub): Map<String, NavigationControlXml.NavPoint> {
        return ePub.opf.ncx.navMap.toMap()
    }

    fun getNcx(ePub: EPub): NavigationControlXml {
        return ePub.opf.ncx
    }

    fun getHref(ePub: EPub): String {
        return ePub.opf.guide.href
    }

    fun getCover(ePub: EPub): String {
        return ePub.opf.cover.src
    }

    fun getOepbsPath(ePub: EPub): String {
        return ePub.opf.oebpsPath
    }
}