package com.github.libliboom.epub.io.robinary

import com.github.libliboom.epub.common.Constant.MAGIC_NUMBER
import com.github.libliboom.epub.common.Constant.META_INF_CONTAINER
import com.github.libliboom.epub.common.Constant.MIME
import com.github.libliboom.epub.common.Constant.MIME_TYPE
import com.github.libliboom.epub.common.Constant.OPF
import com.github.libliboom.epub.common.Constant.VALUE_OF_MAGIC_NUMBER
import com.github.libliboom.epub.common.Constant.VALUE_OF_MIME
import com.github.libliboom.epub.common.Constant.VALUE_OF_MIME_TYPE
import com.github.libliboom.common.const.Resource
import com.github.libliboom.common.io.FileUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MetaRoBinaryTest {

  private lateinit var instance: MetaRoBinary

  @BeforeEach
  fun init() {
    val filename = FileUtils.getResourceDir() + Resource.EPUB_FILE_NAME_01
    instance = MetaRoBinary(filename)
  }

  @Test
  fun getBytes() {
    assertEquals(VALUE_OF_MAGIC_NUMBER, instance.getBytes(MAGIC_NUMBER).toString(Charsets.UTF_8))
    assertEquals(VALUE_OF_MIME_TYPE, instance.getBytes(MIME_TYPE).toString(Charsets.UTF_8))
    assertEquals(VALUE_OF_MIME, instance.getBytes(MIME).toString(Charsets.UTF_8))
    assertEquals("META-INF/container.xml", instance.getBytes(META_INF_CONTAINER).toString(Charsets.UTF_8))
    assertEquals("OEBPS/content.opf", instance.getBytes(OPF).toString(Charsets.UTF_8))
  }
}
