package com.github.libliboom.utils.io

import com.github.libliboom.utils.const.Resource.Companion.EPUB_FILE_NAME_01
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

internal class FileUtilsTest {

    @Test
    fun sampleTest02() {
        val path = FileUtils.getResourceDir() + EPUB_FILE_NAME_01

        val expected = true
        val actual = FileUtils.exist(path)

        assertEquals(expected, actual)
    }
}