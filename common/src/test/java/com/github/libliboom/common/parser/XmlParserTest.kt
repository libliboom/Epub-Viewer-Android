package com.github.libliboom.common.parser

import com.github.libliboom.common.const.Resource.Companion.CONTAINER_FILE_PATH
import com.github.libliboom.common.io.FileUtils
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

internal class XmlParserTest {

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

  companion object {
    private lateinit var instance: XmlParser
    private lateinit var filename: String

    @JvmStatic
    @BeforeAll
    fun setup() {
      instance = XmlParser()
      filename = FileUtils.getOutputDir() + CONTAINER_FILE_PATH
    }
  }
}
