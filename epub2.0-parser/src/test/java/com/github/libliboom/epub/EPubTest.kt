package com.github.libliboom.epub

import com.github.libliboom.utils.const.Resource
import com.github.libliboom.utils.io.FileUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EPubTest {

    private lateinit var ePubFile: String
    private lateinit var destFilePath: String
    private lateinit var instance: EPub

    @BeforeEach
    fun init() {
        ePubFile = FileUtils.getResourceDir() + Resource.EPUB_FILE_NAME_01
        destFilePath = FileUtils.getOutputDir()
        instance = EPub(ePubFile, destFilePath).load()
    }

    @Test
    fun givenEPubFile_whenOpenRequest_thenInitializeIt() {
    }
}
