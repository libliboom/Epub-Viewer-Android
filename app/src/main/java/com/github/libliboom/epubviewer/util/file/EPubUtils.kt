package com.github.libliboom.epubviewer.util.file

import com.github.libliboom.epub.EPub
import com.github.libliboom.epub.outline.opf.NavigationControlXml

object EPubUtils {

    const val DELIMITER_NTH = "#!"

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

    fun getCustomHead(): String {
        return "<head>\n"+
            "<style type=\"text/css\">\n" +
            "@font-face {\n" +
            "    font-family: MyFont;\n" +
            "    src: url(\"${getFontFile("RobotoMono-Regular")}\")\n" +
            "}\n" +
            "body {\n" +
            "    font-family: MyFont;\n" +
            "    font-size: medium;\n" +
            "    text-align: justify;\n" +
            "}\n" +
            "</style>\n" +
            "</head>"
    }

    private fun getFontFile(fontName: String): String {
        return "file:///android_asset/fonts/$fontName.ttf"
    }
}