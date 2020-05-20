package com.github.libliboom.epub

import com.github.libliboom.epub.io.robinary.MetaRoBinary
import com.github.libliboom.epub.outline.ocf.OpenContainerFormat
import com.github.libliboom.epub.outline.opf.OpenPackageFormat
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
class EPub(filePath: String, decompressedPath: String) {
    // TODO: 2020/05/14 check all path if the last char is file separator or not
    private val filePath = filePath
    private val decompressedPath = decompressedPath + File.separator

    lateinit var meta: MetaRoBinary
    lateinit var pagination: PageRoBinary
    lateinit var ocf: OpenContainerFormat
    lateinit var opf: OpenPackageFormat

    init {
        try {
            meta = MetaRoBinary(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        initOpenContainerFormat()
        decompress()
        initOpenPackageFormat()
        initPagination()
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
        val filelist = ArrayList<String>()
        for (src in opf.ncx.navMap.values) {
            filelist += (opf.oebpsPath + src.contentSrc)
        }
        pagination = PageRoBinary(filelist)
    }
}