package com.github.libliboom.utils.layout

import com.github.libliboom.utils.io.robinary.PageRoBinary.Companion.PAGE_CHAR_SIZE
import com.github.libliboom.utils.io.robinary.PageRoBinary.Companion.PAGE_LINE_SIZE
import com.github.libliboom.utils.parser.HtmlParser

object LayoutManager {

    // TODO: 2020/05/24 issue with a file that has content link and content together
    // TODO: 2020/05/24 infinity loop if paragraph size is more then PAGE_CHAR_SIZE
    fun chunked(filename: String): List<String> {
        val parser = HtmlParser()

        var chunks = mutableListOf<String>()

        var loopProtectionCount = 0
        var idx = 0
        var before = 0
        val count = parser.getElementsCount(filename)
        while (idx < count) {
            if (loopProtectionCount++ > 100) throw Exception()
            if (parser.isShorterThanCharSize(filename, idx, PAGE_LINE_SIZE, PAGE_CHAR_SIZE)) {
                val cur = parser.parseSegmentByRange(filename, idx, PAGE_LINE_SIZE).first
                val c = parser.parseSegmentByRange(filename, idx, PAGE_LINE_SIZE).second
                if (cur == before) break
                chunks.add(c)
                idx = cur
                before = idx
            } else {
                val cur = parser.parseTextBySize(filename, idx, PAGE_CHAR_SIZE).first
                val c = parser.parseTextBySize(filename, idx, PAGE_CHAR_SIZE).second
                if (cur == before) break
                chunks.add(c)
                idx = cur
            }
        }

        return chunks
    }
}
