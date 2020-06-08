package com.github.libliboom.epub

import com.github.libliboom.epub.io.robinary.MetaRoBinary
import com.github.libliboom.epub.outline.ocf.OpenContainerFormat
import com.github.libliboom.epub.outline.opf.OpenPackageFormat
import com.github.libliboom.utils.io.FileUtils
import com.github.libliboom.utils.io.ZipFileUtils
import com.github.libliboom.utils.io.robinary.PageRoBinary

/**
 * EPUB stands for Electronic publication
 *
 * Analyze step:
 * - Validate OCF
 * - Decompress epub
 * - create OPF
 * - calculate page blob
 * ...
 */

// TODO: 2020/05/27 Tidy up legacy code for layoutManager
class EPub(private val filePath: String, private val decompressedPath: String) {

    lateinit var pagination: PageRoBinary
    lateinit var ocf: OpenContainerFormat
    lateinit var opf: OpenPackageFormat

    private lateinit var meta: MetaRoBinary

    // REVIEW: 2020/06/05 Exception
    init {
        try {
            meta = MetaRoBinary(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // REVIEW: 2020/06/05 Exception
    fun load(): EPub {
        try {
            initOpenContainerFormat()
            decompress()
            initOpenPackageFormat()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    fun pagination(filelist: List<String>, a: Int, b: MutableList<Pair<Int, Int>>) {
        pagination = PageRoBinary(filelist, a, b)
    }

    fun getSpineList(): ArrayList<String> {
        val filelist = ArrayList<String>()
        for (src in opf.ncx.navMap.values) {
            filelist += (opf.oebpsPath + src.contentSrc)
        }

        val list = mutableListOf<String>()
        for (f in filelist) {
            list.add(FileUtils.getFileNameFromUri(f))
        }

        return list.distinct() as ArrayList<String>
    }

    private fun initOpenContainerFormat() {
        ocf = OpenContainerFormat(meta)
        if (ocf.validateFormat().not()) {
            throw Exception("ERROR: INVALID OCF")
        }
    }

    private fun decompress() {
        ZipFileUtils.extract(filePath, decompressedPath)
    }

    private fun initOpenPackageFormat() {
        opf = OpenPackageFormat(meta, decompressedPath)
    }
}
