package com.github.libliboom.utils.io.robinary

import com.github.libliboom.utils.parser.HtmlParser

// TODO: 2020/05/23  change it to be based on memory
// LATER: 2020/05/19 based on file or else
class PageRoBinary(private val filelist: List<String>) : BlobRoBinary() {

    var pageCount = 0

    private val pages4Chapter = mutableListOf<Pair<Int, Int>>()

    init {
        calculatePage(PAGE_SIZE)
    }

    private fun calculatePage(size: Int) {
        var sum = 0

        val parser = HtmlParser()
        for ((idx, f) in filelist.withIndex()) {
            pages4Chapter.add(Pair(idx, sum))
            val s = parser.parseBody(f)
            sum += if ((s.length / size == 0)) {
                (s.length / size)
            } else {
                (s.length / size) + 1
            }
        }

        pageCount = sum
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

    fun getChapterWithNth(page: Int): Pair<Int, Int> {
        val chapter = findChapter(0, pages4Chapter.size - 1, page)
        val nth = page - pages4Chapter[chapter].second// 'cause base on zero
        return Pair(chapter, nth)
    }

    private fun findChapter(l: Int, r: Int, p: Int): Int {
        if (r > l) {
            val mid = (l + (r - 1)) / 2
            val min = pages4Chapter[mid].second
            val max = pages4Chapter[mid+1].second

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
        const val PAGE_SIZE = 1000
    }
}