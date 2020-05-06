package com.github.libliboom.epub

import com.github.libliboom.epub.outline.ocf.OpenContainerFormat
import com.github.libliboom.epub.io.robinary.MetaRoBinary
import com.github.libliboom.utils.const.Resource
import com.github.libliboom.utils.io.FileUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class OcfTest {

    private lateinit var instance: OpenContainerFormat

    @BeforeEach
    fun init() {
        val filename = FileUtils.getResourceDir() + Resource.EPUB_FILE_NAME_01
        val meta = MetaRoBinary(filename)
        instance = OpenContainerFormat(meta)
    }

    @Test
    fun validateFormat() {
        val expected = true
        val actual = instance.validateFormat()

        assertEquals(expected, actual)
    }
}