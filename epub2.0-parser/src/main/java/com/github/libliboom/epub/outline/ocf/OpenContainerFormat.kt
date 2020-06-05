package com.github.libliboom.epub.outline.ocf

import com.github.libliboom.epub.common.Constant.FILE_OF_CONTAINER
import com.github.libliboom.epub.common.Constant.META_INF_CONTAINER
import com.github.libliboom.epub.common.Constant.MIME
import com.github.libliboom.epub.common.Constant.OPF
import com.github.libliboom.epub.common.Constant.VALUE_OF_MIME
import com.github.libliboom.epub.io.robinary.MetaRoBinary

/**
 * OCF stand for Open Container Format.
 */
class OpenContainerFormat(private val meta: MetaRoBinary) {

    fun validateFormat() = validateMIME() && validateContainer() && validateOpf()

    private fun validateMIME() = VALUE_OF_MIME == meta.getBytes(MIME).toString(Charsets.UTF_8)

    private fun validateContainer() = meta.getBytes(META_INF_CONTAINER)
        .toString(Charsets.UTF_8)
        .contains(FILE_OF_CONTAINER)

    private fun validateOpf() = meta.getBytes(OPF)
        .toString(Charsets.UTF_8)
        .contains(OPF)
}
