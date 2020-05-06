package com.github.libliboom.utils.io

import com.github.libliboom.utils.const.Resource
import com.github.libliboom.utils.io.FileUtils.getOutputDir
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ZipFileUtilsTest {

    private val filename = "OEBPS/content.opf"

    private lateinit var ePubFile: String
    private lateinit var destFilePath: String


    @BeforeEach
    fun init () {
        ePubFile = FileUtils.getResourceDir() + Resource.EPUB_FILE_NAME_01
        destFilePath = getOutputDir()
    }

    @Test
    fun extract() {
        ZipFileUtils.extract(ePubFile, destFilePath)
    }

    @Test
    fun givenName_whenExtractFile_thenSpecificFile() {
        ZipFileUtils.extract(ePubFile, destFilePath, filename)
        val actual = FileUtils.exist(getOutputDir() + filename)

        assertEquals(true, actual)
    }

    @Test
    fun givenCondition_whenSearchFile_thenFilteredFilelist() {
        val actual = ZipFileUtils.findFiles(ePubFile) { it.contains(".opf") }[0]

        assertEquals(filename, actual)
    }

}