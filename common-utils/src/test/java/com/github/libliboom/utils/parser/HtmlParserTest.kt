package com.github.libliboom.utils.parser

import com.github.libliboom.utils.const.Resource
import com.github.libliboom.utils.io.FileUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class HtmlParserTest {

    private lateinit var instance: HtmlParser
    private lateinit var filename: String

    @BeforeEach
    fun init() {
        instance = HtmlParser()
        filename = FileUtils.getOEBPSDir() + Resource.CONTENT_OPF_FILE_NAME
    }

    @Test
    fun displayAllElements() {
        instance.displayAllElements(filename)
    }

    @Test
    fun findSpecificTags() {
        instance.findSpecificTags(filename)
    }

    @Test
    fun extractText() {
        instance.extractText(filename)
    }

    @Test
    fun renderToText() {
        instance.renderToText(filename)
    }

    @Test
    fun encoding() {
        instance.encoding(filename)
    }
}