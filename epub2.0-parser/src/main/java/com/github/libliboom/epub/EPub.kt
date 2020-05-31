package com.github.libliboom.epub

import com.github.libliboom.epub.io.robinary.MetaRoBinary
import com.github.libliboom.epub.outline.ocf.OpenContainerFormat
import com.github.libliboom.epub.outline.opf.OpenPackageFormat
import com.github.libliboom.utils.io.FileUtils
import com.github.libliboom.utils.io.ZipFileUtils
import com.github.libliboom.utils.io.robinary.PageRoBinary
import java.io.File

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
class EPub(filePath: String, decompressedPath: String) {
    // TODO: 2020/05/14 check all path if the last char is file separator or not
    private val filePath = filePath
    private val decompressedPath = decompressedPath + File.separator

    lateinit var meta: MetaRoBinary
    lateinit var pagination: PageRoBinary
    lateinit var ocf: OpenContainerFormat
    lateinit var opf: OpenPackageFormat

    // TODO: 2020/05/23 find a way how to handle run time Exception
    init {
        try {
            meta = MetaRoBinary(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun load(): EPub {
        try {
            initOpenContainerFormat()
            decompress()
            initOpenPackageFormat()
            //initPagination()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    fun pagination(filelist: List<String>, a: Int,  b: MutableList<Pair<Int, Int>>) {
        pagination = PageRoBinary(filelist, a, b)
    }

    private fun initOpenContainerFormat() {
        ocf = OpenContainerFormat(meta)
        if (!ocf.validateFormat()) {
            throw Exception("ERROR: INVALID OCF")
        }
    }

    private fun decompress() {
        ZipFileUtils.extract(filePath, decompressedPath)
    }

    private fun initOpenPackageFormat() {
        opf = OpenPackageFormat(meta, decompressedPath)
    }

    private fun initPagination() {
        val filelist = getSpineList()
        pagination = PageRoBinary(filelist)
    }

    fun getSpineList(): ArrayList<String> {
        val filelist = ArrayList<String>()
        for (src in opf.ncx.navMap.values) {
            filelist += (opf.oebpsPath + src.contentSrc)
        }

        val list = mutableListOf<String>()
        for (f in filelist) {
            list.add(FileUtils.getFileName(f))
        }

        return list.distinct() as ArrayList<String>
    }
}