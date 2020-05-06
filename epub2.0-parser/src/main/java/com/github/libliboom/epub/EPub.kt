package com.github.libliboom.epub

import com.github.libliboom.epub.outline.ocf.OpenContainerFormat
import com.github.libliboom.epub.outline.opf.OpenPackageFormat
import com.github.libliboom.epub.io.robinary.MetaRoBinary
import com.github.libliboom.utils.io.ZipFileUtils

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

    private val filePath = filePath
    private val decompressedPath = decompressedPath

    private lateinit var meta: MetaRoBinary
    private lateinit var ocf: OpenContainerFormat
    private lateinit var opf: OpenPackageFormat

    init {
        try {
            meta = MetaRoBinary(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        initOpenContainerFormat()
        decompress()
        initOpenPackageFormat()
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
}