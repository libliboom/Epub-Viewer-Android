package com.github.libliboom.common.io

import com.github.libliboom.common.Constant
import com.github.libliboom.common.const.Resource
import com.github.libliboom.common.const.Resource.Companion.CONTENT_OPF_FILE_PATH
import com.github.libliboom.common.io.FileUtils.getOutputDir
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files

internal class ZipFileUtilsTest {

  @Test
  fun extract() {
    ZipFileUtils.extract(ePubFile, destFilePath)
  }

  @Test
  fun givenName_whenExtractFile_thenSpecificFile() {
    // Arrange

    // Action
    ZipFileUtils.extract(ePubFile, destFilePath, filename)
    val actual = FileUtils.exist(getOutputDir() + filename)

    // Assertion
    assertEquals(true, actual)
  }

  @Test
  fun givenCondition_whenSearchFile_thenFilteredFilelist() {
    // Arrange

    // Action
    val actual = ZipFileUtils.findFiles(ePubFile) { it.contains(".opf") }[0]

    // Assertion
    assertEquals(filename, actual)
  }

  companion object {
    private const val filename = CONTENT_OPF_FILE_PATH
    private lateinit var ePubFile: String
    private lateinit var destFilePath: String

    @JvmStatic
    @BeforeAll
    fun setup() {
      ePubFile = FileUtils.getResourceDir() + Resource.EPUB_FILE_NAME_01
      destFilePath = getOutputDir()
    }

    @JvmStatic
    @AfterAll
    fun cleanup() {
      val keepFileList = Constant.getKeepFilelist()
      val outputPath = getOutputDir()
      val filePaths = mutableListOf<String>()
      File(outputPath).walk().forEach {
        filePaths.add(it.path)
      }

      val arr = filePaths.stream()
        .filter { path ->
          keepFileList.stream()
            .noneMatch { file -> path.contains(file) }
        }
        .toArray { size -> arrayOfNulls<String>(size) }

      for (f in arr) {
        if (File(f).isDirectory) continue
        Files.delete(File(f).toPath())
      }
    }
  }
}
