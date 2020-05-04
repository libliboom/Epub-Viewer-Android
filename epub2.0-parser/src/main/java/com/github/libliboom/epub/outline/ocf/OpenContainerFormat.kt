package com.github.libliboom.epub.outline.ocf

import com.github.libliboom.epub.common.Constant.FILE_OF_CONTAINER
import com.github.libliboom.epub.common.Constant.META_INF_CONTAINER
import com.github.libliboom.epub.common.Constant.MIME
import com.github.libliboom.epub.common.Constant.OPF
import com.github.libliboom.epub.common.Constant.VALUE_OF_MIME
import com.github.libliboom.epub.robinary.MetaRoBinary

/**
 * Ocf stand for Open Container Format
 */
class OpenContainerFormat(path: String) {

    private val path = path

    private var meta = MetaRoBinary(path)

    /**
     * check if those exist the following list
     * - mimetype
     * - META_INF/container.xml
     * - OEBPS/Expectations.opf
     */
    fun validateFormat(): Boolean {
        return validateMIME() && validateContainer() && validateOpf()
    }

    private fun validateMIME(): Boolean {
        return VALUE_OF_MIME == meta.getBytes(MIME).toString(Charsets.UTF_8)
    }

    private fun validateContainer(): Boolean {
        return meta.getBytes(META_INF_CONTAINER)
            .toString(Charsets.UTF_8)
            .contains(FILE_OF_CONTAINER)
    }

    private fun validateOpf(): Boolean {
        return meta.getBytes(OPF)
            .toString(Charsets.UTF_8)
            .contains(OPF)
    }
}