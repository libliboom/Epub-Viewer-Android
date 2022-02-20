package com.github.libliboom.epub.parser

import com.github.libliboom.epub.common.Constant.ATTRS_FULL_PATH
import com.github.libliboom.epub.common.Constant.ATTRS_MEDIA_TYPE
import com.github.libliboom.epub.common.Constant.QUALIFIED_NAME_ROOT_FILE
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

class SaxParserHandler : DefaultHandler() {

  private var fullPath: String? = null
  private var mediaType: String? = null

  override fun startElement(
    uri: String?,
    localName: String?,
    qName: String?,
    attributes: Attributes?
  ) {
    if (QUALIFIED_NAME_ROOT_FILE == qName) {
      fullPath = attributes?.getValue(ATTRS_FULL_PATH)
      mediaType = attributes?.getValue(ATTRS_MEDIA_TYPE)
    }
  }
}
