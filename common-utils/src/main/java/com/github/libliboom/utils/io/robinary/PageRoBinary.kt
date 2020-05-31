package com.github.libliboom.utils.io.robinary

import com.github.libliboom.utils.layout.LayoutManager
import com.github.libliboom.utils.parser.HtmlParser

// TODO: 2020/05/23  change it to be based on memory
// LATER: 2020/05/19 based on file or else
class PageRoBinary(private val filelist: List<String>) : BlobRoBinary() {

    var pageCount = 0
    var pages4Chapter = mutableListOf<Pair<Int, Int>>()

    private val pages4Contents = mutableMapOf<Int, String>()

    init {
        //calculatePage()
    }

    constructor(filelist: List<String>, a: Int,  b: MutableList<Pair<Int, Int>>) : this(filelist) {
        pageCount = a
        pages4Chapter = b
    }

    private fun calculatePage() {
        var sum = 0

        val parser = HtmlParser()
        for ((idx, f) in filelist.withIndex()) {
            pages4Chapter.add(Pair(idx, sum))

            if (idx == 0) {
                for (chunk in LayoutManager.chunked(f)) {
                    pages4Contents[sum++] = chunk
                }
            } else {
                val s = parser.parseText(f)
                for (chunk in s.chunked(PAGE_CHAR_SIZE)) {
                    pages4Contents[sum++] = chunk
                }
            }
        }
        pageCount = sum
    }

    fun getContentsOfPage(pageNumber: Int): String {
        return pages4Contents[pageNumber]!!
    }

    fun getPageOfChapter(filename: String): Pair<Int, Int> {
        var pair = pages4Chapter[0] // default value
        for ((idx, f) in filelist.withIndex()) {
            if (f.contains(filename)) {
                pair = pages4Chapter[idx]
                break
            }
        }

        return pair
    }

    fun getSpineWithNth(page: Int): Pair<Int, Int> {
        val chapter = findChapter(0, pages4Chapter.size - 1, page)
        val nth = page - pages4Chapter[chapter].second// 'cause base on zero
        return Pair(chapter, nth)
    }

    private fun findChapter(l: Int, r: Int, p: Int): Int {
        if (r > l) {
            val mid = (l + (r - 1)) / 2
            val min = pages4Chapter[mid].second
            val max = pages4Chapter[mid + 1].second

            if (p in min..max) {
                if (p == max) {
                    return mid + 1
                }
                return mid
            }

            if (p < min) {
                return findChapter(l, mid - 1, p)
            }

            if (min < p) {
                return findChapter(mid + 1, r, p)
            }
        }

        return l
    }

    companion object {
        const val PAGE_CHAR_SIZE = 850
        const val PAGE_LINE_SIZE = 15
    }
}