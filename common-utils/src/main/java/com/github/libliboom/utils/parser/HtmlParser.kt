package com.github.libliboom.utils.parser

import net.htmlparser.jericho.Element
import net.htmlparser.jericho.HTMLElementName
import net.htmlparser.jericho.MasonTagTypes
import net.htmlparser.jericho.MicrosoftConditionalCommentTagTypes
import net.htmlparser.jericho.PHPTagTypes
import net.htmlparser.jericho.Source
import java.net.URL

/**
 * @TODO Refactoring those functions as generalization.
 */
class HtmlParser {

    fun parseMetadata(filename: String, tag: String = "metadata"): List<String> {
        var source = createSource(filename)
        val meta = source.getAllStartTags(tag)

        val contents = mutableListOf<String>()
        for (e in meta[0].childElements[0].childElements) {
            if (!e.name.startsWith("dc:")) continue
            contents.add(
                StringBuilder().append(e.name.substring(3))
                    .append(":").append(e.content.toString()).toString()
            )
        }

        return contents
    }

    fun parseManifest(filename: String, tag: String = "manifest"): Map<Int, String> {
        var source = createSource(filename)
        val manifest = source.getAllStartTags(tag)

        val jsonMap = mutableMapOf<Int, String>()
        for ((idx, item) in manifest[0].childElements[0].childElements.withIndex()) {
            if (item.name == "!--") continue
            val sb = StringBuilder()
            for (attr in item.attributes) {
                sb.addJsonElement(attr.name, attr.value)
            }

            jsonMap[idx] = sb.encloseJson()
        }

        return jsonMap
    }

    fun parseSpine(filename: String, tag: String = "spine"): Map<Int, String> {
        var source = createSource(filename)
        val spine = source.getAllStartTags(tag)

        val jsonMap = mutableMapOf<Int, String>()
        for ((idx, item) in spine[0].childElements[0].childElements.withIndex()) {
            if (item.name == "!--") continue
            val sb = StringBuilder()
            for (attr in item.attributes) {
                sb.addJsonElement(attr.name, attr.value)
            }

            jsonMap[idx] = sb.encloseJson()
        }

        return jsonMap
    }

    fun parseNavMap(filename: String, tag: String = "navPoint"): Map<Int, String> {
        var source = createSource(filename)
        val jsonMap = mutableMapOf<Int, String>()
        val navPoints = source.getAllStartTags(tag)
        for ((idx, navPoint) in navPoints.withIndex()) {
            val sb = StringBuilder()
            for (attr in navPoint.attributes) {
                sb.addJsonElement(attr.name, attr.value)
            }
            for (c in navPoint.childElements[0].childElements) {
                val cname = c.name
                if (c.childElements.size != 0) {
                    for (cc in c.childElements) {
                        sb.addJsonElement(cname + cc.name.capitalize(), cc.content.toString())
                    }
                }
                for (attr in c.attributes) {
                    sb.addJsonElement(cname + attr.name.capitalize(), attr.value)
                }
            }

            jsonMap[idx] = sb.encloseJson()
        }

        return jsonMap.toMap()
    }

    fun parseGuide(filename: String, tag: String = "guide"): String {
        var source = createSource(filename)
        val guide = source.getAllStartTags(tag)

        val sb = StringBuilder()
        for ((idx, item) in guide[0].childElements[0].childElements.withIndex()) {
            if (item.name == "!--") continue
            for (attr in item.attributes) {
                sb.addJsonElement(attr.name, attr.value)
            }
        }

        return sb.encloseJson()
    }

    fun parseCover(filename: String, tag: String = "img"): String {
        var source = createSource(filename)
        val img = source.getAllStartTags(tag)

        val sb = StringBuilder()
        for (attr in img[0].attributes) {
            sb.addJsonElement(attr.name, attr.value)
        }

        return sb.encloseJson()
    }

    fun isShorterThanCharSize(filename: String, idx: Int, lines: Int, maxChars: Int): Boolean {
        val source = createSource(filename)

        val sb = StringBuilder()

        val end = source.getAllElements(HTMLElementName.P).size
        val segments = source.getAllElements(HTMLElementName.P)

        for (j in idx until end) {
            if (j == idx + lines) break
            sb.append(segments[j].textExtractor.setIncludeAttributes(false))
        }

        return sb.length < maxChars
    }

    fun getElementsCount(filename: String): Int {
        val source = createSource(filename)
        return source.getAllElements(HTMLElementName.P).size
    }

    fun parseSegmentByRange(filename: String, start: Int, len: Int): Pair<Int, String> {
        val source = createSource(filename)
        val size = source.getAllElements(HTMLElementName.P).size
        val end = if (start + len < size) start + len else size

        var curIdx = start
        val sb = StringBuilder()
        for (idx in start until end) {
            sb.append(source.getAllElements(HTMLElementName.P)[idx])
            curIdx = idx
        }

        return Pair(curIdx, sb.toString())
    }

    fun parseTextBySize(filename: String, start: Int, size: Int): Pair<Int, String> {
        val source = createSource(filename)
        val end = source.getAllElements(HTMLElementName.P).size
        val sb = StringBuilder()
        var curIdx = start
        for (idx in start..end) {
            if (sb.length > size) break
            sb.append(source.getAllElements(HTMLElementName.P)[idx].textExtractor.setIncludeAttributes(false))
            curIdx = idx
        }

        return Pair(curIdx, sb.toString())
    }

    fun parseElement(filename: String): String {
        val source = createSource(filename)

        val sb = StringBuilder()

        for (segment in source.getAllElements(HTMLElementName.H2)) {
            sb.append(segment.textExtractor.setIncludeAttributes(true))
        }

        for (segment in source.getAllElements(HTMLElementName.P)) {
            sb.append(segment.textExtractor.setIncludeAttributes(false))
        }

        return sb.toString()
    }

    fun parseText(filename: String): String {
        val source = createSource(filename)

        val sb = StringBuilder()

        for (segment in source.getAllElements(HTMLElementName.H2)) {
            sb.append(segment.textExtractor.setIncludeAttributes(true))
        }

        for (segment in source.getAllElements(HTMLElementName.P)) {
            sb.append(segment.textExtractor.setIncludeAttributes(false))
        }

        return sb.toString()
    }

    fun parseHead(filename: String): String {
        return parseElementOrEmpty(filename, "<head>")
    }

    fun parseBody(filename: String): String {
        return parseElementOrEmpty(filename, "<body>")
    }

    private fun parseElementOrEmpty(filename: String, startTag: String): String {
        val source = createSource(filename)
        val elementList: List<Element> = source.allElements
        for (element in elementList) {
            if (element.startTag.tidy(true) == startTag) {
                return element.toString()
            }
        }

        return "" // default value
    }

    // @TODO: analyze about this configuration
    private fun createSource(filename: String): Source {
        var sourceUrlString = filename
        if (sourceUrlString.indexOf(':') == -1) {
            sourceUrlString = "file:$sourceUrlString"
        }

        MicrosoftConditionalCommentTagTypes.register()
        PHPTagTypes.register()
        PHPTagTypes.PHP_SHORT.deregister()
        MasonTagTypes.register()
        return Source(URL(sourceUrlString))
    }

    private fun StringBuilder.addJsonElement(key: String, value: String) =
        this.append("\"").append(key).append("\":\"").append(value).append("\"").append(",")

    private fun StringBuilder.encloseJson() =
        "{" + this.substring(0, this.length - 1) + "}"
}
