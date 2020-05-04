package com.github.libliboom.utils.parser

import com.github.libliboom.utils.const.Resource.Companion.CONTAINER_FILE_PATH
import com.github.libliboom.utils.io.FileUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

internal class XmlParserTest {

    private lateinit var instance: XmlParser
    private lateinit var filename: String

    @BeforeEach
    fun init() {
        instance = XmlParser()
        filename = FileUtils.getOutputDir() + CONTAINER_FILE_PATH
    }

    @Test
    fun convert2Document() {
        //instance.parse(filename)
    }

    @Test
    fun parse() {
        val handler = SaxParserHandler()
        instance.parse(filename, handler)

        println(handler.fullPath)
    }

    private inner class SaxParserHandler : DefaultHandler() {

        var fullPath: String? = null
        var mediaType: String? = null

        override fun startElement(
            uri: String?,
            localName: String?,
            qName: String?,
            attributes: Attributes?
        ) {
            if ("rootfile" == qName) {
                fullPath = attributes?.getValue("full-path")
                mediaType = attributes?.getValue("full-path")
            }
        }
    }
}